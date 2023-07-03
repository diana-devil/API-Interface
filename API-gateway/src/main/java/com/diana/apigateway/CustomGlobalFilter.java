package com.diana.apigateway;


import com.diana.apicommon.model.entity.InterfaceInfo;
import com.diana.apicommon.model.entity.User;
import com.diana.apicommon.service.InnerInterfaceInfoService;
import com.diana.apicommon.service.InnerUserInterfaceInfoService;
import com.diana.apicommon.service.InnerUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.diana.apiclientstarter.utils.StringUtils.getSign;



/**
 * @ClassName CustomGlobalFilter
 * @Date 2023/4/4 17:07
 * @Author diane
 * @Description 全局过滤
 * @Version 1.0
 */
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

    /**
     * 白名单
     */
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 用户发送请求到 API 网关

        // 2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());

        // 3. 控制访问-（黑白名单）
        // 如果白名单不包含请求地址，就拒绝访问
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            // 将没有权限封装起来
            return handleNoAuth(response, "无权限！");
        }

        // 4. 用户鉴权（判断 accessKey, secretKey 是否合法）
        User invokeUser = null;
        try {
            invokeUser = validAuth(request.getHeaders());
        } catch (Exception e) {
            // 如果有异常，就是没有权限，直接返回
            return handleNoAuth(response, "用户信息错误！");
        }

        // 5. 请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
           log.info("url , method 参数不正确！");
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response, "请求的接口不存在！");
        }


        // 6.0 检测是否有请求次数
        Long userId = invokeUser.getId();
        Long interfaceId = interfaceInfo.getId();
        int count = 0;
        try {
            count = innerUserInterfaceInfoService.getCountByUserIdAndInterfaceInfoId(userId, interfaceId);
        } catch (Exception e) {
            log.error("参数错误，没有这个对应关系", e);
            return handleNoAuth(response, "您还没有开通该接口的调用哦！");
        }
        if (count <= 0) {
            log.info("用户id:{}, 接口id:{},剩余调用次数为0!!", userId, interfaceId);
            return handleNoAuth(response, "剩余调用次数为0！请及时续费");
        }

        // 6.1 请求转发，调用模拟接口
        return handleResponse(exchange, chain, interfaceId, userId);
    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 如果是响应式编程
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 响应日志
                                        // 读取响应数据，构建日志，并打印
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 日志打印
                                        log.info("响应结果：" + data);

                                        // 8.调用成功，次数 + 1  rpc远程调用
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 9. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        // 不是响应式编程
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 降级处理返回数据
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }



    /**
     * 没有权限的处理方法
     * @param response
     * @return
     */
    private Mono<Void> handleNoAuth(ServerHttpResponse response, String str) {
        // 3.1 设置响应状态码 403
        response.setStatusCode(HttpStatus.FORBIDDEN);
        DataBufferFactory bufferFactory = response.bufferFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        // 要写入的数据对象，会自动转为json格式
        DataBuffer wrap = null;
        try {
            wrap = bufferFactory.wrap(objectMapper.writeValueAsBytes(str));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DataBuffer finalWrap = wrap;
        // 将提示信息 写在网页上
        return response.writeWith(Mono.fromSupplier(() -> finalWrap));

        // 3.2 直接结束方法
        // return response.setComplete();
    }


    @Override
    public int getOrder() {
        return -1;
    }



    /**
     * 没有权限，会抛出异常
     *      根据 accessKey 查出用户信息， 从用户信息中 解析出 secretKey
     *      key 不要把secretKey 在服务器中传递
     * @param headers 请求头
     */
    public User validAuth(HttpHeaders headers) {
        // 做请求校验
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");

        // 权限校验
        // 1.标识校验
        User invokeUser = innerUserService.getInvokeUser(accessKey);
        if (invokeUser == null) {
            throw new RuntimeException("标识不正确，没有权限！");
        }

        // 2.随机数校验  ---  实际，把随机数存到redis或者数据库中，拿这个nonce与库中的对比，这里就不做校验了
        // 3.时间戳检验  --- 不能超过5分钟
        long curTime = System.currentTimeMillis() / 1000;
        // 时间差值-- 差了多少秒
        long diff = curTime - Long.parseLong(timestamp);
        if (diff > 5 * 60) {
            throw new RuntimeException("超时，没有权限！");
        }

        // 4.签名校验
        // 根据 accessKey 查出用户信息， 从用户信息中 解析出 secretKey；；； 不要把secretKey 在服务器中传递
        String secretKey = invokeUser.getSecretKey();
        String serverSign = getSign(body, secretKey);
        if (!serverSign.equals(sign)) {
            throw new RuntimeException("签名不正确，没有权限！");
        }

        return invokeUser;
    }


}

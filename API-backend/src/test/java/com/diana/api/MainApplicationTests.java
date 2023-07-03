package com.diana.api;

import com.diana.api.mapper.UserInterfaceInfoMapper;
import com.diana.api.model.entity.UserInterfaceInfo;
import com.diana.api.model.vo.InterfaceInfoVO;
import com.diana.api.service.InterfaceInfoService;
import com.diana.apicommon.model.entity.InterfaceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主类测试
 *
 * @author diana
 * 
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    UserInterfaceInfoMapper mapper;

    @Resource
    InterfaceInfoService service;


    @Test
    void test1() {
        // int limit = 3;
        // // 聚合接口调用次数
        // List<UserInterfaceInfo> userInterfaceInfos = mapper.listTopInvokeInterfaceInfo(limit);
        // List<InterfaceInfoVO> interfaceInfoVOList = userInterfaceInfos.stream().map(s -> {
        //     // 根据 接口id 查询接口信息
        //     InterfaceInfo interfaceInfo = service.getById(s.getInterfaceInfoId());
        //     // 新建 vo对象，并copy 接口信息
        //     InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        //     BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        //     interfaceInfoVO.setTotalNum(s.getTotalNum());
        //     return interfaceInfoVO;
        // }).collect(Collectors.toList());
    }





}

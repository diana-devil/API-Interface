package com.diana.apiInterface.controller;

import com.diana.apiclientstarter.model.User;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;



/**
 * @ClassName NameController
 * @Date 2023/3/30 16:41
 * @Author diane
 * @Description 查询名字接口
 * 提供三个模拟接口
 *
 * 1. GET 接口
 * 2. POST 接口（URL 传参）
 * 3. POST 接口 （Restful)
 * @Version 1.0
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        System.out.println("Get-你的名字：" + name);
        return "Get-你的名字：" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        // url 传参
        return "Post-你的名字：" + name;
    }

    /**
     *  模拟接口，打印用户名称
     *   接口不需要做任何的校验，，算是一个独立业务；；；校验交给api-backend的网关完成
     * @param user user对象
     * @param request http请求
     * @return
     */
    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        // json传参
        return "用户名称：" + user.getUserName();
    }
}

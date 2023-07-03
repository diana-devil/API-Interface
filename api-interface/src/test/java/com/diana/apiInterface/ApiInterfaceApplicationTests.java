package com.diana.apiInterface;

import com.diana.apiclientstarter.client.Client;
import com.diana.apiclientstarter.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    /*// 注入starter 内部的客户端
    @Resource
    private Client apiClent;

    // 测试starter内部的方法
    @Test
    void testStarterMethods() {
        System.out.println(apiClent.getNameByGet("diana"));
        System.out.println(apiClent.getNameByPost("diana"));
        System.out.println(apiClent.getUsernameByPost(new User("diana")));
    }*/

}

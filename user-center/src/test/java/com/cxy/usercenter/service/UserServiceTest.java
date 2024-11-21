package com.cxy.usercenter.service;

import com.cxy.usercenter.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserService userService;

    @Test
    public void testAddUser(){

        User user = new User();
        user.setUserName("testyaoyao");
        user.setUserAccount("123456");
        user.setAvartarUrl("");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");


        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }
}
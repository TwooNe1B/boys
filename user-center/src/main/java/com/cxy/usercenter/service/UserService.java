package com.cxy.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxy.usercenter.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author glh
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-04 15:29:05
*/
public interface UserService extends IService<User> {


    /**
     * 用户注释
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userResigter(String userAccount ,String userPassword,String checkPassword);

    User doLogin(String userAccount, String userPassword, HttpServletRequest request);
}

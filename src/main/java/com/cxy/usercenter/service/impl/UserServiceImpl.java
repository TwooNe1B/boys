package com.cxy.usercenter.service.impl;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxy.usercenter.domain.User;
import com.cxy.usercenter.mapper.UserMapper;
import com.cxy.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @author glh
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-04 15:29:05
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录态的键
     */
    private static final String USER_LOGIN_STATE ="userLoginState";
    @Override
    public long userResigter(String userAccount, String userPassword, String checkPassword) {

        //1.校验
        //三个值是否为null或者undifine
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)) {
            return -1;
        }
        if (userAccount.length() <4){
            return  -1;
        }

        if (userPassword.length()<8 || checkPassword .length() <8){
            return -1;
        }

        //账户不能包含特殊字符

        //账户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count >0){
            return -1;
        }

        //2.加密
        SM2 sm2 = SmUtil.sm2();
        // 公钥加密，私钥解密
        String encryptPassword = sm2.encryptBcd(userPassword, KeyType.PublicKey);
        //String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptPassword, KeyType.PrivateKey));
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            return -1;
        }
        return 0;
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {


        //1.校验
        //三个值是否为null或者undifine
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            return null;
        }
        if (userAccount.length() <4){
            return  null;
        }

        if (userPassword.length()<8){
            return null;
        }


        //
        SM2 sm2 = SmUtil.sm2();
        // 公钥加密，私钥解密
        String encryptPassword = sm2.encryptBcd(userPassword, KeyType.PublicKey);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAcount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            return null;
        }

        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);

        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(0L);
        safetyUser.setUserName("");
        safetyUser.setUserAccount("");
        safetyUser.setAvartarUrl("");
        safetyUser.setGender(0);
        safetyUser.setUserPassword("");
        safetyUser.setPhone("");
        safetyUser.setEmail("");
        safetyUser.setUserStatus(0);
        safetyUser.setCreatTime(new Date());
        safetyUser.setUpdataTime(new Date());
        safetyUser.setIsDelete(0);
        safetyUser.setMap(new HashMap<String,String>());


        return user;
    }

    @Override
    public List<User> serachUsersByTags(List<String> tagNameList){

        if (CollectionUtils.isEmpty((tagNameList))){
            //抛出异常

        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //拼接and查询
        //like ’%java‘ and like ‘%C%'
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags",tagName);
        }

        List<User> users = userMapper.selectList(queryWrapper);
        return null;
    }
}





package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.UserInfo;

import java.util.List;

public interface LoginService{
    //查询所有用户信息
    List<UserInfo> getAllUserInfo();
    //根据用户名查询用户信息
    UserInfo getUserInfoByNameOrEmail(String userName);
    //根据邮箱查询用户信息
    UserInfo getUserInfoByEmail(String registerEmail);


}

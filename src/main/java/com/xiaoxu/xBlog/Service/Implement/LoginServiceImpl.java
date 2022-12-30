package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.LoginService;
import com.xiaoxu.xBlog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private UserService userService;

    @Override
    public List<UserInfo> getAllUserInfo() {
        List<UserInfo> users = userService.list();
        return users;
    }

    @Override
    public UserInfo getUserInfoByNameOrEmail(String userName) {
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(userName!=null||!userName.equals(""),UserInfo::getUserName,userName).or().eq(userName!=null||!userName.equals(""),UserInfo::getUserEmail,userName);
        UserInfo user = userService.getOne(qw);
        return user;
    }

    @Override
    public UserInfo getUserInfoByEmail(String registerEmail) {
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(registerEmail!=null||!registerEmail.equals(""),UserInfo::getUserEmail,registerEmail);
        UserInfo user = userService.getOne(qw);
        return user;
    }

}

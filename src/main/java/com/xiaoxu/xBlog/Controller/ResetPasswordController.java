package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.UserService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @GetMapping("/showUserNameOrEmail")
    public ReturnResults hasUserByNameOrEmail(String userNameOrEmail){
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(!StringUtils.isEmpty(userNameOrEmail),UserInfo::getUserName,userNameOrEmail).or().eq(!StringUtils.isEmpty(userNameOrEmail),UserInfo::getUserEmail,userNameOrEmail);
        UserInfo userInfo = userService.getOne(qw);
        if (userInfo == null) {
            return ReturnResults.error("错误，该用户不存在");
        }
        redisTemplate.opsForValue().set("resetUser",userInfo,5, TimeUnit.MINUTES);
        return ReturnResults.success(userInfo);
    }

    @GetMapping("/updateUserPass")
    @Transactional
    public ReturnResults updateUserPass(String userPass,String userEmail){
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UserInfo::getUserEmail,userEmail);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserPass(userPass);
        boolean b = userService.update(userInfo,qw);
        return b?ReturnResults.success("更新成功"):ReturnResults.error("更新失败");
    }
}

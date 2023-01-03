package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.LoginService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/login")
    public ReturnResults showUserInfo(String loginName, String loginPass) {
        UserInfo userInfo = loginService.getUserInfoByNameOrEmail(loginName);
        if (userInfo == null) {
            return ReturnResults.error("用户不存在，请检查用户名称是否正确.");
        }
        if (userInfo.getState()!= 0){
            return ReturnResults.error("该用户已被冻结，请联系管理员解除");
        }
        if (userInfo.getUserPass().equals(loginPass)) {
            redisTemplate.opsForValue().set("user",userInfo);
            return ReturnResults.success(userInfo);
        }
        return ReturnResults.error("用户名或密码错误，请仔细检查其是否正确");
    }

    @GetMapping("/logout")
    public ReturnResults logout(){
        redisTemplate.delete("user");
        return ReturnResults.success("已安全退出登录");
    }


}

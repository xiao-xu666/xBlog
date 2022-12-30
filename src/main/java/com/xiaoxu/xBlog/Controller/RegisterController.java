package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.LoginService;
import com.xiaoxu.xBlog.Service.UserService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @GetMapping("/showUserName")
    public ReturnResults hasUser(String registerName){
        UserInfo userInfo = loginService.getUserInfoByNameOrEmail(registerName);
        if (userInfo == null) {
            return ReturnResults.success("用户不存在，可以创建");
        }
        return ReturnResults.error("用户已存在，不能创建");
    }
    @GetMapping("/showUserEmail")
    public ReturnResults hasEmail(String registerEmail){
        UserInfo userInfo = loginService.getUserInfoByEmail(registerEmail);
        if (userInfo == null) {
            return ReturnResults.success("该邮箱不存在，可以绑定");
        }
        return ReturnResults.error("该邮箱已存在绑定用户，不能使用");
    }

    @PostMapping("/insertRegisterInfo")
    @Transactional
    public ReturnResults insertUserInfo(@RequestBody UserInfo user){
        boolean save = userService.save(user);
        return save?ReturnResults.success("注册成功!"):ReturnResults.error("注册失败!");
    }

}

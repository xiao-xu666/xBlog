package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.UserService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/updateUserBasicsInfo")
    @Transactional
    public ReturnResults updateUserBasicsInfo(UserInfo userInfo){
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UserInfo::getUserId,userInfo.getUserId());
        boolean b = userService.update(userInfo,qw);
        return b?ReturnResults.success("修改成功"):ReturnResults.error("更新失败");
    }

    @GetMapping("/updateUserPass")
    @Transactional
    public ReturnResults updateUserPass(UserInfo userInfo){
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UserInfo::getUserId,userInfo.getUserId());
        boolean b = userService.update(userInfo,qw);
        return b?ReturnResults.success("修改成功"):ReturnResults.error("修改失败");
    }

    @GetMapping("/updateUserEmail")
    @Transactional
    public ReturnResults updateUserEmail(UserInfo userInfo){
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UserInfo::getUserId,userInfo.getUserId());
        boolean b = userService.update(userInfo, qw);
        return b?ReturnResults.success("修改成功"):ReturnResults.error("修改失败");
    }

    @GetMapping("/showUserCount")
    public ReturnResults showUserCount(){
        int count = userService.count();
        return ReturnResults.success(count);
    }

    @GetMapping("/showUserPage")
    public ReturnResults showUserPage(Integer currentPage,Integer pageSize,String searchContent){
        Page<UserInfo> userInfoPage = null;
        userInfoPage = (Page<UserInfo>) redisTemplate.opsForValue().get("user_showUserPage_"+currentPage+"_"+pageSize+"_"+searchContent);
        if (userInfoPage != null) {
            return ReturnResults.success(userInfoPage);
        }
        userInfoPage = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(searchContent)) {
            qw.like(UserInfo::getUserName,searchContent).or().like(UserInfo::getUserEmail,searchContent);
        }
        qw.orderByDesc(UserInfo::getUserRegdate);
        userService.page(userInfoPage,qw);
        redisTemplate.opsForValue().set("user_showUserPage_"+currentPage+"_"+pageSize+"_"+searchContent,userInfoPage,5, TimeUnit.HOURS);
        return ReturnResults.success(userInfoPage);
    }

    @PostMapping("/updateUserInfo")
    public ReturnResults updateUserInfo(@RequestBody UserInfo userInfo){
        LambdaQueryWrapper<UserInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UserInfo::getUserId,userInfo.getUserId());
        boolean b = userService.update(userInfo,qw);
        if (b){
            Set<String> keys = redisTemplate.keys("user_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("修改成功"):ReturnResults.error("更新失败");
    }

    @DeleteMapping("/delete")
    public ReturnResults deleteUserInfo(Integer userId){
        boolean b = userService.removeByChangeState(userId);
        if (b){
            Set<String> keys = redisTemplate.keys("user_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("删除成功"):ReturnResults.error("删除失败");
    }
}

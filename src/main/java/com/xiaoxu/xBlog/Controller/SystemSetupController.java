package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Entities.SystemSetup;
import com.xiaoxu.xBlog.Service.SystemSetupService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/systemSetup")
public class SystemSetupController {
    @Autowired
    private SystemSetupService setupService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/updateSystemSetup")
    public ReturnResults updateSystemSetup(@RequestBody SystemSetup systemSetup){
        systemSetup.setSystemSetUpId(1);
        boolean b = setupService.updateById(systemSetup);
        if(b){
            redisTemplate.delete("systemSetup");
        }
        return b?ReturnResults.success("更新成功"):ReturnResults.error("更新失败");
    }
}

package com.xiaoxu.xBlog.Controller;

import com.xiaoxu.xBlog.Entities.BackstageMenuInfo;
import com.xiaoxu.xBlog.Service.BackstageMenuService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/backStageMenuInfo")
public class BackstageMenuInfoController {

    @Autowired
    private BackstageMenuService backstageMenuService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public ReturnResults showAllMenu(Integer usertype){
        List<BackstageMenuInfo> menu = null;
        menu = (List<BackstageMenuInfo>) redisTemplate.opsForValue().get("backStageMenuInfo"+usertype);
        if (menu != null){
            return ReturnResults.success(menu);
        }
        menu = backstageMenuService.showAllMenuWithChild(usertype);
        if (menu == null) {
            return ReturnResults.error("菜单查找失败");
        }
        redisTemplate.opsForValue().set("backStageMenuInfo"+usertype,menu,5, TimeUnit.HOURS);
        return ReturnResults.success(menu);
    }
}

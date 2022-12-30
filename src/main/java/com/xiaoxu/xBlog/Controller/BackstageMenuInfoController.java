package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxu.xBlog.Entities.BackstageMenuInfo;
import com.xiaoxu.xBlog.Service.BackstageMenuService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backStageMenuInfo")
public class BackstageMenuInfoController {

    @Autowired
    private BackstageMenuService backstageMenuService;

    @GetMapping
    public ReturnResults showAllMenu(Integer usertype){
        List<BackstageMenuInfo> menu = backstageMenuService.showAllMenuWithChild(usertype);
        if (menu == null) {
            return ReturnResults.error("菜单查找失败");
        }
        return ReturnResults.success(menu);
    }
}

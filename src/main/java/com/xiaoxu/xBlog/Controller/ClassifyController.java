package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxu.xBlog.Entities.ClassifyInfo;
import com.xiaoxu.xBlog.Service.ClassifyService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/classify")
public class ClassifyController {
    @Autowired
    private ClassifyService classifyService;

    @GetMapping("/showClassifyInfoByClassifyId")
    public ReturnResults showClassifyInfoByClassifyId(Integer classifyId){
        LambdaQueryWrapper<ClassifyInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ClassifyInfo::getClassifyId,classifyId);
        List<ClassifyInfo> list = classifyService.list(qw);
        return ReturnResults.success(list);
    }
}

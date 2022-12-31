package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxu.xBlog.Entities.ClassifyInfo;
import com.xiaoxu.xBlog.Service.ClassifyService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/classify")
public class ClassifyController {
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/showClassifyInfoByClassifyId")
    public ReturnResults showClassifyInfoByClassifyId(Integer classifyId){
        List<ClassifyInfo> list = null;
        list = (List<ClassifyInfo>) redisTemplate.opsForValue().get("classify_showClassifyInfoByClassifyId_"+classifyId);
        if (list != null) {
            return ReturnResults.success(list);
        }
        LambdaQueryWrapper<ClassifyInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ClassifyInfo::getClassifyId,classifyId);
        list = classifyService.list(qw);
        redisTemplate.opsForValue().set("classify_showClassifyInfoByClassifyId_"+classifyId,list,5, TimeUnit.HOURS);
        return ReturnResults.success(list);
    }

    @PostMapping("/insert")
    public ReturnResults insertClassifyInfo(@RequestBody ClassifyInfo classifyInfo){
        boolean b = classifyService.save(classifyInfo);
        if (b) {
            Set<String> keys = redisTemplate.keys("classify_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("插入成功"):ReturnResults.error("插入失败");
    }

    @DeleteMapping("/delete")
    public ReturnResults deleteClassifyInfo(Integer classifyId){
        try {
            classifyService.removeByIdWithChecked(classifyId);
            Set<String> keys = redisTemplate.keys("classify_" + "*");
            redisTemplate.delete(keys);
            return ReturnResults.success("删除成功!");
        } catch (Exception e) {
            return ReturnResults.error(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ReturnResults updateClassifyInfo(@RequestBody ClassifyInfo classifyInfo){
        boolean b = classifyService.updateById(classifyInfo);
        if (b) {
            Set<String> keys = redisTemplate.keys("classify_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("修改成功"):ReturnResults.error("修改失败");
    }
}

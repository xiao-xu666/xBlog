package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.UrlInfo;
import com.xiaoxu.xBlog.Service.UrlService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/url")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/showAllUrlInfo")
    public ReturnResults showAllUrlInfo(Integer urlType){
        List<UrlInfo> urls = null;
        urls = (List<UrlInfo>) redisTemplate.opsForValue().get("url_showAllUrlInfo_"+urlType);
        if (urls != null) {
            return ReturnResults.success(urls);
        }
        LambdaQueryWrapper<UrlInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(UrlInfo::getUrlPass,2);
        qw.eq(UrlInfo::getUrlType,urlType);
        urls = urlService.list(qw);
        redisTemplate.opsForValue().set("url_showAllUrlInfo_"+urlType,urls,5, TimeUnit.HOURS);
        return ReturnResults.success(urls);
    }

    @PostMapping("/insert")
    @Transactional
    public ReturnResults insertUrl(@RequestBody UrlInfo info){
        boolean b = urlService.save(info);
        if(b){
            Set<String> keys = redisTemplate.keys("url_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("成功"):ReturnResults.error("失败");
    }

    @GetMapping("/showUrlPage")
    public ReturnResults showUrlPage(Integer currentPage,Integer pageSize,String searchContent,Integer auditContent){
        Page<UrlInfo> pageInfo = null;
        pageInfo = (Page<UrlInfo>) redisTemplate.opsForValue().get("url_showUrlPage_"+currentPage+"_"+pageSize+"_"+searchContent+"_"+auditContent);
        if (pageInfo != null) {
            return ReturnResults.success(pageInfo);
        }
        LambdaQueryWrapper<UrlInfo> qw = new LambdaQueryWrapper<>();
        if (currentPage != null && pageSize != null){
            pageInfo = new Page<>(currentPage,pageSize);
        }else {
            pageInfo = new Page<>();
        }
        if (!StringUtils.isEmpty(searchContent)){
            qw.like(UrlInfo::getUrlName,searchContent).or().like(UrlInfo::getUrlReferral,searchContent).or().like(UrlInfo::getUrlAddres,searchContent);
        }
        if (auditContent != null){
            qw.eq(UrlInfo::getUrlPass,auditContent);
        }
        urlService.page(pageInfo,qw);
        redisTemplate.opsForValue().set("url_showUrlPage_"+currentPage+"_"+pageSize+"_"+searchContent+"_"+auditContent,pageInfo,5,TimeUnit.HOURS);
        return ReturnResults.success(pageInfo);
    }

    @GetMapping("/updateUrlPass")
    public ReturnResults updateUrlPass(Integer urlId,Integer urlPass){
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setUrlId(urlId);
        urlInfo.setUrlPass(urlPass);
        boolean b = urlService.updateById(urlInfo);
        if(b){
            Set<String> keys = redisTemplate.keys("url_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("更新成功"):ReturnResults.error("更新失败");
    }

    @DeleteMapping("/delete")
    public ReturnResults deleteUrlInfo(Integer urlId){
        boolean b = urlService.removeById(urlId);
        if(b){
            Set<String> keys = redisTemplate.keys("url_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("删除成功"):ReturnResults.error("删除失败");
    }

}

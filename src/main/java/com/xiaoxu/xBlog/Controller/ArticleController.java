package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.ArticleService;
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
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ArticleService articleService;

    @GetMapping("/showArticleCountByUserId")
    public ReturnResults showArticleCountByUserId(){
        Integer count = null;
        count = (Integer) redisTemplate.opsForValue().get("article_showArticleCountByUserId");
        if (count != null) {
            return ReturnResults.success(count);
        }
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get("user");
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleInfo::getUserId,user.getUserId());
        qw.orderByDesc(ArticleInfo::getPublishTime);
        count = articleService.count(qw);
        redisTemplate.opsForValue().set("article_showArticleCountByUserId",count,5,TimeUnit.HOURS);
        return ReturnResults.success(count);
    }
    @GetMapping("/showAllArticleInfo")
    public ReturnResults showAllArticleInfo(){
        List<ArticleInfo> list = null;
        list = (List<ArticleInfo>) redisTemplate.opsForValue().get("article_showAllArticleInfo");
        if (list != null) {
            return ReturnResults.success(list);
        }
        try {
            list = articleService.showAllArticleInfo();
            redisTemplate.opsForValue().set("article_showAllArticleInfo",list,5,TimeUnit.HOURS);
            return ReturnResults.success(list);
        }catch (Exception e){
            return ReturnResults.error(e.getMessage());
        }
    }

    @GetMapping("/showArticleCount")
    public ReturnResults showArticleCount(){
        Integer count = null;
        count = (Integer) redisTemplate.opsForValue().get("article_showArticleCount");
        if (count != null) {
            return ReturnResults.success(count);
        }
        count = articleService.count();
        redisTemplate.opsForValue().set("article_showArticleCount",count,5,TimeUnit.HOURS);
        return ReturnResults.success(count);
    }

    @GetMapping("/page")
    public ReturnResults showPageArticles(Integer currentPage,Integer pageSize,String keyword,Integer userType,Integer userId,Integer articlePass,String articleTitle){
        Page<ArticleInfo> pageInfo = null;
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        pageInfo = (Page<ArticleInfo>) redisTemplate.opsForValue().get("article_page_" + currentPage + "_" + pageSize + "_" + keyword + "_" + userType + "_" + userId + "_" + articlePass + "_" + articleTitle);
        if(pageInfo != null){
            return ReturnResults.success(pageInfo);
        }
        if (userId != null){
            if (currentPage != null && pageSize != null){
                pageInfo = new Page<>(currentPage,pageSize);
            }else {
                pageInfo = new Page<>();
            }
            if (userType == 0) {
                qw.eq(articlePass!=null,ArticleInfo::getArticlePass,articlePass).like(!StringUtils.isEmpty(articleTitle),ArticleInfo::getArticleTitle,articleTitle);
                articleService.page(pageInfo,qw);
                redisTemplate.opsForValue().set("article_page_"+currentPage+"_"+pageSize+"_"+keyword+"_"+userType+"_"+userId+"_"+articlePass+"_"+articleTitle,pageInfo,5,TimeUnit.HOURS);
                return ReturnResults.success(pageInfo);
            }
            qw.eq(ArticleInfo::getUserId,userId).eq(articlePass!=null,ArticleInfo::getArticlePass,articlePass).like(!StringUtils.isEmpty(articleTitle),ArticleInfo::getArticleTitle,articleTitle);
            articleService.page(pageInfo,qw);
            redisTemplate.opsForValue().set("article_page_"+currentPage+"_"+pageSize+"_"+keyword+"_"+userType+"_"+userId+"_"+articlePass+"_"+articleTitle,pageInfo,5,TimeUnit.HOURS);
            return ReturnResults.success(pageInfo);
        }else {
            if (currentPage != null && pageSize != null) {
                pageInfo = new Page<>(currentPage, pageSize);
            } else {
                pageInfo = new Page<>();
            }
            qw.eq(ArticleInfo::getArticlePass,2);
            qw.like(!StringUtils.isEmpty(keyword), ArticleInfo::getArticleTitle, keyword).or().like(!StringUtils.isEmpty(keyword), ArticleInfo::getArticleTitle, keyword);
            articleService.page(pageInfo, qw);
            redisTemplate.opsForValue().set("article_page_"+currentPage+"_"+pageSize+"_"+keyword+"_"+userType+"_"+userId+"_"+articlePass+"_"+articleTitle,pageInfo,5,TimeUnit.HOURS);
            return ReturnResults.success(pageInfo);
        }
    }

    @GetMapping("/byClassifyId")
    public ReturnResults showPageArticlesByClassifyId(Integer currentPage,Integer pageSize,Integer classifyId,String keyword){
        Page<ArticleInfo> pageInfo = null;
        pageInfo = (Page<ArticleInfo>) redisTemplate.opsForValue().get("article_byClassifyId_" + currentPage + "_" + pageSize + "_" + classifyId + "_" + keyword);
        if (pageInfo != null){
            return ReturnResults.success(pageInfo);
        }
        pageInfo = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(classifyId!=null,ArticleInfo::getArticleClassifyId,classifyId);
        qw.like(!StringUtils.isEmpty(keyword),ArticleInfo::getArticleTitle,keyword).or().like(!StringUtils.isEmpty(keyword),ArticleInfo::getArticleTitle,keyword);
        articleService.page(pageInfo,qw);
        redisTemplate.opsForValue().set("article_byClassifyId_"+currentPage+"_"+pageSize+"_"+classifyId+"_"+keyword,pageInfo,5,TimeUnit.HOURS);
        return ReturnResults.success(pageInfo);
    }

    @GetMapping("/showArticleInfo")
    public ReturnResults showArticleInfoByArticleId(Integer articleId){
        ArticleInfo articleInfo = null;
        articleInfo = (ArticleInfo) redisTemplate.opsForValue().get("article_showArticleInfo_"+articleId);
        if (articleInfo != null) {
            return ReturnResults.success(articleInfo);
        }
        articleInfo= articleService.getById(articleId);
        redisTemplate.opsForValue().set("article_showArticleInfo"+articleId,articleInfo,5, TimeUnit.HOURS);
        return ReturnResults.success(articleInfo);
    }
    @GetMapping("/updateArticleClick")
    @Transactional
    public ReturnResults updateArticleClick(Integer articleId){
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get("user");
        Boolean isClicked = (Boolean) redisTemplate.opsForValue().get("updateArticleClick_userId_"+user.getUserId()+"_"+articleId);
        if (isClicked != null && isClicked == true) {
            return ReturnResults.success("??????????????????????????????????????????????????????");
        }
        ArticleInfo info = articleService.getById(articleId);
        info.setClick(info.getClick()+1);
        if(articleService.updateById(info)){
            redisTemplate.opsForValue().set("updateArticleClick_userId_"+user.getUserId()+"_"+articleId,true,1,TimeUnit.DAYS);
        }else{
            return ReturnResults.error("????????????");
        }
        return ReturnResults.success("????????????");
    }

    @PostMapping("/insertArticleInfo")
    @Transactional
    public ReturnResults insertArticleInfo(@RequestBody ArticleInfo articleInfo){
        boolean b = articleService.save(articleInfo);
        if (b) {
            Set<String> keys = redisTemplate.keys("article_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("????????????"):ReturnResults.error("????????????");
    }

    @PostMapping("/updateArticleInfo")
    @Transactional
    public ReturnResults updateArticleInfo(@RequestBody ArticleInfo articleInfo){
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleInfo::getArticleId,articleInfo.getArticleId());
        boolean b = articleService.update(articleInfo,qw);
        if (b) {
            Set<String> keys = redisTemplate.keys("article_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("????????????"):ReturnResults.error("????????????");
    }

    @GetMapping("/updateArticlePass")
    public ReturnResults updateArticlePass(Integer articleId,Integer articlePass){
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);
        articleInfo.setArticlePass(articlePass);
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleInfo::getArticleId,articleId);
        boolean b = articleService.update(articleInfo,qw);
        if (b) {
            Set<String> keys = redisTemplate.keys("article_" + "*");
            redisTemplate.delete(keys);
        }
        return b?ReturnResults.success("????????????"):ReturnResults.error("????????????");
    }

    @DeleteMapping("/delete")
    public ReturnResults deleteArticleById(Integer articleId){
        try {
            articleService.removeByIdByChecked(articleId);
            Set<String> keys = redisTemplate.keys("article_" + "*");
            redisTemplate.delete(keys);
            return ReturnResults.success("????????????!");
        } catch (Exception e) {
            return ReturnResults.error(e.getMessage());
        }
    }

    @GetMapping("/checkUserPrivileges")
    public ReturnResults checkUserPrivileges(Integer articleId){
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get("user");
        return articleService.checkUserPrivileges(user, articleId);
    }
}

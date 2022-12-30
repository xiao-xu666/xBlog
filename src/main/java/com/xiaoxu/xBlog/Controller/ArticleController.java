package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.ArticleService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get("user");
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleInfo::getUserId,user.getUserId());
        qw.orderByDesc(ArticleInfo::getPublishTime);
        int count = articleService.count(qw);
        return ReturnResults.success(count);
    }

    @GetMapping("/showArticleCount")
    public ReturnResults showArticleCount(){
        int count = articleService.count();
        return ReturnResults.success(count);
    }

    @GetMapping("/page")
    public ReturnResults showPageArticles(Integer currentPage,Integer pageSize,String keyword,Integer userType,Integer userId,Integer articlePass,String articleTitle){
        Page<ArticleInfo> pageInfo = null;
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        if (userId != null){
            if (currentPage != null && pageSize != null){
                pageInfo = new Page<>(currentPage,pageSize);
            }else {
                pageInfo = new Page<>();
            }
            if (userType == 0) {
                qw.eq(articlePass!=null,ArticleInfo::getArticlePass,articlePass).like(!StringUtils.isEmpty(articleTitle),ArticleInfo::getArticleTitle,articleTitle);
                articleService.page(pageInfo,qw);
                return ReturnResults.success(pageInfo);
            }
            qw.eq(ArticleInfo::getUserId,userId).eq(articlePass!=null,ArticleInfo::getArticlePass,articlePass).like(!StringUtils.isEmpty(articleTitle),ArticleInfo::getArticleTitle,articleTitle);
            articleService.page(pageInfo,qw);
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
            return ReturnResults.success(pageInfo);
        }
    }

    @GetMapping("/byClassifyId")
    public ReturnResults showPageArticlesByClassifyId(Integer currentPage,Integer pageSize,Integer classifyId,String keyword){
        Page<ArticleInfo> pageInfo = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(classifyId!=null,ArticleInfo::getArticleClassifyId,classifyId);
        qw.like(!StringUtils.isEmpty(keyword),ArticleInfo::getArticleTitle,keyword).or().like(!StringUtils.isEmpty(keyword),ArticleInfo::getArticleTitle,keyword);
        articleService.page(pageInfo,qw);
        return ReturnResults.success(pageInfo);
    }

    @GetMapping("/showArticleInfo")
    public ReturnResults showArticleInfoByArticleId(Integer articleId){
        ArticleInfo articleInfo = articleService.getById(articleId);

        redisTemplate.opsForValue().set("article",articleInfo,5, TimeUnit.MINUTES);
        return ReturnResults.success(articleInfo);
    }
    @GetMapping("/updateArticleClick")
    @Transactional
    public ReturnResults updateArticleClick(Integer articleId){
        ArticleInfo info = articleService.getById(articleId);
        info.setClick(info.getClick()+1);
        articleService.updateById(info);
        return ReturnResults.success("成功");
    }

    @PostMapping("/insertArticleInfo")
    @Transactional
    public ReturnResults insertArticleInfo(@RequestBody ArticleInfo articleInfo){
        boolean b = articleService.save(articleInfo);
        return b?ReturnResults.success("新建成功"):ReturnResults.error("新建失败");
    }

    @PostMapping("/updateArticleInfo")
    @Transactional
    public ReturnResults updateArticleInfo(@RequestBody ArticleInfo articleInfo){
        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleInfo::getArticleId,articleInfo.getArticleId());
        boolean b = articleService.update(articleInfo,qw);
        return b?ReturnResults.success("修改成功"):ReturnResults.error("修改失败");
    }

    @GetMapping("/updateArticlePass")
    public ReturnResults updateArticlePass(Integer articleId,Integer articlePass){
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setArticleId(articleId);
        articleInfo.setArticlePass(articlePass);

        LambdaQueryWrapper<ArticleInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ArticleInfo::getArticleId,articleId);
        boolean b = articleService.update(articleInfo,qw);
        return b?ReturnResults.success("修改成功"):ReturnResults.error("修改失败");
    }

    @DeleteMapping("/delete")
    public ReturnResults deleteArticleById(Integer articleId){
        articleService.removeById(articleId);
        return ReturnResults.success("删除成功");
    }
}

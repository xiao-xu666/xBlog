package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Service.CommentService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/count")
    public ReturnResults count() {
        Integer count = null;
        count = (Integer) redisTemplate.opsForValue().get("comment_count");
        if (count != null) {
            return ReturnResults.success(count);
        }
        count = commentService.count();
        redisTemplate.opsForValue().set("comment_count", count, 5, TimeUnit.HOURS);
        return ReturnResults.success(count);
    }

    @PostMapping("/insertCommentInfo")
    @Transactional
    public ReturnResults insertCommentInfo(@RequestBody CommentInfo info) {
        boolean b = commentService.save(info);
        if (b) {
            Set<String> keys = redisTemplate.keys("comment_" + "*");
            redisTemplate.delete(keys);
        }
        return b ? ReturnResults.success("保存成功") : ReturnResults.error("失败");
    }

    @GetMapping("/page")
    public ReturnResults showPageArticles(Integer currentPage, Integer pageSize, String keyword, Integer userType, Integer userId, String searchContent) {
        Page<CommentInfo> pageInfo = null;
        LambdaQueryWrapper<CommentInfo> qw = null;
        pageInfo = (Page<CommentInfo>) redisTemplate.opsForValue().get("comment_page_" + currentPage + "_" + pageSize + "_" + keyword + "_" + userType + "_" + userId + "_" + searchContent);
        if (pageInfo != null) {
            return ReturnResults.success(pageInfo);
        }
        if (userId != null) {
            if (currentPage != null && pageSize != null) {
                pageInfo = new Page<>(currentPage, pageSize);
            } else {
                pageInfo = new Page<>();
            }
            if (userType == 0) {
                qw = new LambdaQueryWrapper<>();
                qw.like(!StringUtils.isEmpty(searchContent), CommentInfo::getContent, searchContent);
                commentService.page(pageInfo, qw);
                redisTemplate.opsForValue().set("comment_page_" + currentPage + "_" + pageSize + "_" + keyword + "_" + userType + "_" + userId + "_" + searchContent, pageInfo, 5, TimeUnit.HOURS);
                return ReturnResults.success(pageInfo);
            }
            qw = new LambdaQueryWrapper<>();
            qw.eq(CommentInfo::getUserId, userId).like(!StringUtils.isEmpty(searchContent), CommentInfo::getContent, searchContent);
            commentService.page(pageInfo, qw);
            redisTemplate.opsForValue().set("comment_page_" + currentPage + "_" + pageSize + "_" + keyword + "_" + userType + "_" + userId + "_" + searchContent, pageInfo, 5, TimeUnit.HOURS);
            return ReturnResults.success(pageInfo);
        } else {
            if (currentPage != null && pageSize != null) {
                pageInfo = new Page<>(currentPage, pageSize);
            } else {
                pageInfo = new Page<>();
            }
            qw = new LambdaQueryWrapper<>();
            qw.like(!StringUtils.isEmpty(keyword), CommentInfo::getContent, keyword);
            commentService.page(pageInfo, qw);
            redisTemplate.opsForValue().set("comment_page_" + currentPage + "_" + pageSize + "_" + keyword + "_" + userType + "_" + userId + "_" + searchContent, pageInfo, 5, TimeUnit.HOURS);
            return ReturnResults.success(pageInfo);
        }
    }

    @DeleteMapping("/delete")
    @Transactional
    public ReturnResults delete(Integer commentId) {
        try {
            commentService.deleteByCommentIdWithChild(commentId);
            Set<String> keys = redisTemplate.keys("comment_" + "*");
            redisTemplate.delete(keys);
            return ReturnResults.success("删除成功");
        } catch (Exception e) {
            return ReturnResults.error(e.getMessage());
        }
    }

    @DeleteMapping("/deleteByArticleId")
    @Transactional
    public ReturnResults deleteByArticleId(Integer articleId) {
        try {
            LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
            qw.eq(CommentInfo::getArticleId, articleId);
            commentService.deleteWithChild(qw);
            Set<String> keys = redisTemplate.keys("comment_" + "*");
            redisTemplate.delete(keys);
            return ReturnResults.success("删除成功");
        } catch (Exception e) {
            return ReturnResults.success(e.getMessage());
        }
    }
}

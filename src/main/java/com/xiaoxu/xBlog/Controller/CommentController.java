package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.CommentService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/count")
    public ReturnResults count(){
        int count = commentService.count();
        return ReturnResults.success(count);
    }
    @PostMapping("/insertCommentInfo")
    @Transactional
    public ReturnResults insertCommentInfo(@RequestBody CommentInfo info){
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get("user");
        ArticleInfo article = (ArticleInfo) redisTemplate.opsForValue().get("article");
        info.setUserId(user.getUserId());
        boolean b = commentService.save(info);
        return b?ReturnResults.success("保存成功"):ReturnResults.error("失败");
    }

    @GetMapping("/page")
    public ReturnResults showPageArticles(Integer currentPage,Integer pageSize,String keyword,Integer userType,Integer userId,String searchContent){
        Page<CommentInfo> pageInfo = null;
        LambdaQueryWrapper<CommentInfo> qw = null;
        if (userId != null){
            if (currentPage != null && pageSize != null){
                pageInfo = new Page<>(currentPage,pageSize);
            }else {
                pageInfo = new Page<>();
            }
            if (userType == 0) {
                qw = new LambdaQueryWrapper<>();
                qw.like(!StringUtils.isEmpty(searchContent),CommentInfo::getContent,searchContent);
                commentService.page(pageInfo,qw);
                return ReturnResults.success(pageInfo);
            }
            qw = new LambdaQueryWrapper<>();
            qw.eq(CommentInfo::getUserId,userId).like(!StringUtils.isEmpty(searchContent),CommentInfo::getContent,searchContent);
            commentService.page(pageInfo,qw);
            return ReturnResults.success(pageInfo);
        }else {
            if (currentPage != null && pageSize != null) {
                pageInfo = new Page<>(currentPage, pageSize);
            } else {
                pageInfo = new Page<>();
            }
            qw = new LambdaQueryWrapper<>();
            qw.like(!StringUtils.isEmpty(keyword), CommentInfo::getContent, keyword);
            commentService.page(pageInfo, qw);
            return ReturnResults.success(pageInfo);
        }
    }

    @DeleteMapping("/delete")
    @Transactional
    public ReturnResults delete(Integer commentId){
        commentService.deleteByCommentIdWithChild(commentId);
        return ReturnResults.success("删除成功");
    }

    @DeleteMapping("/deleteByArticleId")
    @Transactional
    public ReturnResults deleteByArticleId(Integer articleId){
        LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentInfo::getArticleId,articleId);
        commentService.remove(qw);
        return ReturnResults.success("删除成功");
    }
}

package com.xiaoxu.xBlog.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxu.xBlog.Entities.*;
import com.xiaoxu.xBlog.Service.*;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import com.xiaoxu.xBlog.Utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class CommonController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemSetupService setupService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/showAllClassifyInfo")
    public ReturnResults showAllClassifyInfo(){
        List<ClassifyInfo> classifyInfos = null;
        classifyInfos = (List<ClassifyInfo>) redisTemplate.opsForValue().get("classify_showAllClassifyInfo");
        if (classifyInfos != null) {
            return ReturnResults.success(classifyInfos);
        }
        classifyInfos = classifyService.list();
        redisTemplate.opsForValue().set("classify_showAllClassifyInfo",classifyInfos,5, TimeUnit.HOURS);
        return ReturnResults.success(classifyInfos);
    }
    @GetMapping("/showCommentByArticleId")
    public ReturnResults showCommentByArticleId(Integer articleId){
        List<CommentInfo> commentInfos = null;
        commentInfos = (List<CommentInfo>) redisTemplate.opsForValue().get("comment_showCommentByArticleId_"+articleId);
        if (commentInfos != null) {
            return ReturnResults.success(commentInfos);
        }
        LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentInfo::getArticleId,articleId);
        qw.eq(CommentInfo::getParentId,0);
        List<CommentInfo> list = commentService.list(qw);
        commentInfos = list.stream().map(item -> {
            item.setChild(commentService.getChildById(item.getCommentId()));
            LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserInfo::getUserId, item.getUserId());
            UserInfo user = userService.getOne(queryWrapper);
            item.setUserType(user.getUserType());
            item.setUserName(user.getUserName());
            item.setUserRegdate(user.getUserRegdate());
            item.setUserIcon(user.getUserIcon());
            item.setUserEmail(user.getUserEmail());
            return item;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set("comment_showCommentByArticleId_"+articleId,commentInfos,5,TimeUnit.HOURS);
        return ReturnResults.success(commentInfos);
    }

    @GetMapping("/updateArticleNumberByClassifyId")
    @Transactional
    public ReturnResults updateArticleNumberByClassifyId(Integer classifyId){
        ClassifyInfo classifyInfo = classifyService.getById(classifyId);
        classifyInfo.setArticleNumber(classifyInfo.getArticleNumber()+1);
        classifyService.updateById(classifyInfo);
        return ReturnResults.success("更新成功");
    }

    @GetMapping("/removeArticleNumberByClassifyId")
    @Transactional
    public ReturnResults removeArticleNumberByClassifyId(Integer classifyId){
        LambdaQueryWrapper<ClassifyInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ClassifyInfo::getClassifyId,classifyId);
        ClassifyInfo classifyInfo = classifyService.getOne(qw);
        classifyInfo.setArticleNumber(classifyInfo.getArticleNumber()-1);
        classifyService.update(classifyInfo,qw);
        return ReturnResults.success("更新成功");
    }

    @PostMapping("/thumbnailUpload")
    public ReturnResults<String> thumbnailUpload(MultipartFile file) throws IOException {
        String fileUrl = UploadUtils.uploadImage(file);
        return ReturnResults.success(fileUrl);
    }

    @PostMapping("/articleImgUpload")
    public ReturnResults<String> articleImgUpload(MultipartFile files) throws IOException {
        String fileUrl = UploadUtils.uploadImage(files);
        return ReturnResults.success(fileUrl);
    }

    @GetMapping("/showAllSystemSetup")
    public ReturnResults showAllSystemSetup(){
        List<SystemSetup> setups = null;
        setups = (List<SystemSetup>) redisTemplate.opsForValue().get("systemSetup");
        if (setups != null) {
            return ReturnResults.success(setups);
        }
        setups = setupService.getAllSystemSetup();
        redisTemplate.opsForValue().set("systemSetup",setups,5,TimeUnit.HOURS);
        return ReturnResults.success(setups);
    }
}

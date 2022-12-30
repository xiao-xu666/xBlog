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

    @GetMapping("/showAllArticleInfo")
    public ReturnResults showAllArticleInfo(){
        List<ArticleInfo> list = articleService.list();
        return ReturnResults.success(list);
    }
    @GetMapping("/logout")
    public ReturnResults logout(){
        redisTemplate.delete("user");
        return ReturnResults.success("已安全退出登录");
    }
    @GetMapping("/showAllClassifyInfo")
    public ReturnResults showAllClassifyInfo(){
        List<ClassifyInfo> classifyInfos = classifyService.list();
        return ReturnResults.success(classifyInfos);
    }
    @GetMapping("/showCommentByArticleId")
    public ReturnResults showCommentByArticleId(Integer articleId){
        LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentInfo::getArticleId,articleId);
        qw.eq(CommentInfo::getParentId,0);
        List<CommentInfo> list = commentService.list(qw);
        List<CommentInfo> commentInfos = list.stream().map(item -> {
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
        return ReturnResults.success(commentInfos);
    }

    @GetMapping("/updateArticleNumberByClassifyId")
    @Transactional
    public ReturnResults updateArticleNumberByClassifyId(Integer classifyId){
        LambdaQueryWrapper<ClassifyInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(ClassifyInfo::getClassifyId,classifyId);
        ClassifyInfo classifyInfo = classifyService.getOne(qw);
        classifyInfo.setArticleNumber(classifyInfo.getArticleNumber()+1);
        classifyService.update(classifyInfo,qw);
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
        List<SystemSetup> setups = setupService.getAllSystemSetup();
        return ReturnResults.success(setups);
    }
}

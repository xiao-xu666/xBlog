package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.CommentInfoDAO;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Service.CommentService;
import com.xiaoxu.xBlog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentInfoDAO, CommentInfo> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public List<CommentInfo> getChildById(Integer commentId) {
        LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentInfo::getParentId,commentId).orderByDesc(CommentInfo::getCommentDate);
        List<CommentInfo> list = this.list(qw);
        List<CommentInfo> commentInfos = list.stream().map(item -> {
            item.setChild(this.getChildById(item.getCommentId()));
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
        return commentInfos;
    }

    @Override
    public void deleteByCommentIdWithChild(Integer commentId) {
        List<CommentInfo> child = getChildById(commentId);
        child.forEach(item->{
            LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
            qw.eq(CommentInfo::getCommentId,item.getCommentId());
            deleteByCommentIdWithChild(item.getCommentId());
            remove(qw);
        });
        removeById(commentId);
    }


}

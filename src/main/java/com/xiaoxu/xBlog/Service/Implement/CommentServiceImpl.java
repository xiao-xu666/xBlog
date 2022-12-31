package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.CommentInfoDAO;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Entities.UserInfo;
import com.xiaoxu.xBlog.Exception.CustomerException;
import com.xiaoxu.xBlog.Service.CommentService;
import com.xiaoxu.xBlog.Service.UserService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Inet4Address;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentInfoDAO, CommentInfo> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public List<CommentInfo> getChildById(Integer commentId) {
        LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentInfo::getParentId, commentId).orderByDesc(CommentInfo::getCommentDate);
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
    @Transactional
    public void deleteByCommentIdWithChild(Integer commentId) throws Exception{
        List<CommentInfo> child = getChildById(commentId);
        for (CommentInfo item : child) {
            LambdaQueryWrapper<CommentInfo> qw = new LambdaQueryWrapper<>();
            qw.eq(CommentInfo::getCommentId, item.getCommentId());
            deleteByCommentIdWithChild(item.getCommentId());
            if(!remove(qw)){
                throw new RuntimeException("业务处理异常");
            }
        }
        if (!removeById(commentId)) {
            throw new RuntimeException("系统出现异常,删除失败!");
        }
    }

    @Override
    @Transactional
    public void deleteWithChild(LambdaQueryWrapper<CommentInfo> qw1) throws Exception{
        List<CommentInfo> commentInfos = this.list(qw1);
        if (commentInfos != null && commentInfos.size() > 0) {
            List<Integer> idList = commentInfos.stream().map(item -> item.getCommentId()).collect(Collectors.toList());
            for (Integer id : idList) {
                this.deleteByCommentIdWithChild(id);
            }
        } else {
            throw new RuntimeException("业务异常,该文章下没有评论");
        }


    }


}

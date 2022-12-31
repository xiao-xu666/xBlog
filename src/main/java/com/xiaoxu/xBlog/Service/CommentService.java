package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Exception.CustomerException;
import com.xiaoxu.xBlog.Utils.ReturnResults;

import java.util.List;

public interface CommentService extends IService<CommentInfo> {

    List<CommentInfo> getChildById(Integer commentId);

    void deleteByCommentIdWithChild(Integer commentId)throws Exception;

    void deleteWithChild(LambdaQueryWrapper<CommentInfo> qw)throws Exception;
}

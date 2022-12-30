package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.CommentInfo;

import java.util.List;

public interface CommentService extends IService<CommentInfo> {

    List<CommentInfo> getChildById(Integer commentId);

    void deleteByCommentIdWithChild(Integer commentId);
}

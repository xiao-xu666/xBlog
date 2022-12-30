package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReviewInfo implements Serializable {

    private static final long serialVersionUID = 4L;

    @TableId
    private Integer reviewId; // 评论编号
    private Integer articleId; // 文章代号
    private Integer userId; // 评论用户编号
    private String reviewContent; // 评论内容
    private Date reviewDate; // 评论日期
}

package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CommentInfo implements Serializable {

    private static final long serialVersionUID = 8L;

    @TableId
    private Integer commentId;           // 评论id
    private String content;              // 评论内容
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentDate;            // 评论日期
    private Integer articleId; // 文章编号   这里考虑到实际情况下：因为blog本身可能很大，在前后端传输数据时会影响效率，所以comment没必要再包含文章id
    private Integer userId; // 评论用户编号
    private Integer parentId; // 父评论的id没有则为0

    @TableField(exist = false)
    private List<CommentInfo> child = new ArrayList<>(); // 本评论下的子评论
    @TableField(exist = false)
    private Integer userType; // 用户类型  1：管理员 2：普通用户 默认普通用户
    @TableField(exist = false)
    private String userName; // 用户名
    @TableField(exist = false)
    private String userEmail; // 邮箱
    @TableField(exist = false)
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date userRegdate; // 注册时间
    @TableField(exist = false)
    private String userSignature; // 个性签名
    @TableField(exist = false)
    private String userIcon; // 个人头像
}

package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ArticleInfo implements Serializable {

    private static final long serialVersionUID = 11L;
    /**
     * 文章自身属性
     **/
    @TableId
    private Integer articleId; // 文章编号
    private Integer userId; // 编写用户编号
    private String userName; // 编写用户名称
    private String articleTitle; // 文章标题
    private Integer articleClassifyId; // 文章分类id
    private String articleClassifyName; // 文章分类名称
    private String articleDase; // 文章描述
    private String articleImgLitimg; // 文章缩略图
    private String articleContent; // 文章内容
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime; // 发表时间
    private Integer articlePass; // 审核状态  2通过
    private Integer articleState; // 文章状态 1所有人 2仅自己
    private Integer commentState;// 评论状态 1开启 2关闭
    private Integer click; // 阅读次数
    private Integer review; // 评论次数
    /**
     * 实体关系
     **/
    @TableField(exist = false)
    private List<CommentInfo> comments = new ArrayList<>();    // 博客评论（一对多）
}

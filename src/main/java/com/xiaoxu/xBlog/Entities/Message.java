package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 6L;

    @TableId
    private Integer messageId; // 留言编号
    private String messageName; // 留言昵称
    private String messageQQ; // 留言QQ
    private String content; // 留言内容
    private String replyContent; // 回复留言内容
    private Date messageDate; // 留言日期
}

package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "urlinfo")
public class UrlInfo implements Serializable {

    private static final long serialVersionUID = 2L;

    @TableId
    private Integer urlId; // 链接编号
    private String urlName; // 网站名称
    private String urlAddres; // 网站地址
    private String urlReferral; // 网站介绍
    private String urlLitimg;// 网站缩略图
    private String webmasterEmail; // 站长邮箱
    private Integer urlPass; // 后台审核是否通过 1待审核 2审核通过 3审核失败
    private Integer urlType; // 链接类型  1：学习网站 2：友情链接
}

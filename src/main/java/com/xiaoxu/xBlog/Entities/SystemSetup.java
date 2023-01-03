package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "systemsetup")
public class SystemSetup implements Serializable {

    private static final long serialVersionUID = 3L;

    @TableId
    private Integer systemSetUpId; //id
    private String stickArticle;// '置顶',
    private String allArticle;// '文章展示',
    private String featuredArticle;// '左侧精选文章',
    private String technologyArticle;// '左侧技术专区文章',
    private String resourceArticle;// '右侧资源专区文章',
    private String advertising1;// '左侧广告图1',
    private String advertisingLink1;// '左侧广告链接1',
    private String advertising2;// '右侧广告图1',
    private String advertisingLink2;// '左侧广告链接1',
    private String advertising3;// '右侧广告图2',
    private String advertisingLink3;// '左侧广告链接1'

    private Boolean effects01; // 特效1
    private Boolean effects02; // 特效2
}

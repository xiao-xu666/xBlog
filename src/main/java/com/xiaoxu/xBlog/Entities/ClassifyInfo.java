package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClassifyInfo implements Serializable {

    private static final long serialVersionUID = 9L;

    @TableId
    private Integer classifyId; // 分类编号
    private String classifyName; // 分类名称
    private Integer articleNumber; // 文章数量
    private String color1;// 渐变色1,
    private String color2;// 渐变色2
    private String classifyIntroduce; // 分类介绍
}

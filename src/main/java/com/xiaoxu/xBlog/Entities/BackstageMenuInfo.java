package com.xiaoxu.xBlog.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BackstageMenuInfo implements Serializable {

    private static final long serialVersionUID = 10L;

    @TableId
    private Integer id; // 菜单id
    private String name; // 菜单名称
    private String chineseName; //菜单中文名称
    private String title; // 菜单标题
    private String path; // 菜单路径
    private String icon; // 菜单图标
    private Integer parentMenuId; // 父菜单id
    private Integer menuStatus; // 菜单是否对普通用户可见
    @TableField(exist = false)
    private List<BackstageMenuInfo> childMenu;  // 子菜单
}

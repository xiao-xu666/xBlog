package com.xiaoxu.xBlog.Entities;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "userinfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer userId; // 用户编号
    private Integer userType; // 用户类型  0：管理员 1：普通用户 默认普通用户
    private String userName; // 用户名
    private String userPass; // 密码
    private String userEmail; // 邮箱
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date userRegdate; // 注册时间
    private String userSignature; // 个性签名
    private String userIcon; // 个人头像
    private Integer state; //状态  0:正常  1: 冻结
}

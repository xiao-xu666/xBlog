package com.xiaoxu.xBlog.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxu.xBlog.Entities.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDAO extends BaseMapper<UserInfo> {
}

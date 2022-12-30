package com.xiaoxu.xBlog.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentInfoDAO extends BaseMapper<CommentInfo> {
}

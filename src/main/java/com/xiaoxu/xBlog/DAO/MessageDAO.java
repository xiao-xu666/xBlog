package com.xiaoxu.xBlog.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxu.xBlog.Entities.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDAO extends BaseMapper<Message> {
}

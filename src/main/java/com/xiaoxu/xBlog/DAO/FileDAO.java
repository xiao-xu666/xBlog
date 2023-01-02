package com.xiaoxu.xBlog.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxu.xBlog.Entities.FileDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDAO extends BaseMapper<FileDTO> {
}

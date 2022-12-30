package com.xiaoxu.xBlog.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxu.xBlog.Entities.PageBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PageBeanDAO extends BaseMapper<PageBean> {
}

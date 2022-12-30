package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.PageBeanDAO;
import com.xiaoxu.xBlog.Entities.PageBean;
import com.xiaoxu.xBlog.Service.PageBeanService;
import org.springframework.stereotype.Service;

@Service
public class PageBeanServiceImpl extends ServiceImpl<PageBeanDAO, PageBean> implements PageBeanService {
}

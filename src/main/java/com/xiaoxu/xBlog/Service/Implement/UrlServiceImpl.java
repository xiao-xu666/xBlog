package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.UrlInfoDAO;
import com.xiaoxu.xBlog.Entities.UrlInfo;
import com.xiaoxu.xBlog.Service.UrlService;
import org.springframework.stereotype.Service;

@Service
public class UrlServiceImpl extends ServiceImpl<UrlInfoDAO, UrlInfo> implements UrlService {
}

package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.ArticleInfoDAO;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Service.ArticleService;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleInfoDAO, ArticleInfo> implements ArticleService {

}

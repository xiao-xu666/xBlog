package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.ReviewInfoDAO;
import com.xiaoxu.xBlog.Entities.ReviewInfo;
import com.xiaoxu.xBlog.Service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewInfoDAO, ReviewInfo> implements ReviewService {
}

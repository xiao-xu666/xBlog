package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.ClassifyInfoDAO;
import com.xiaoxu.xBlog.Entities.ClassifyInfo;
import com.xiaoxu.xBlog.Service.ClassifyService;
import org.springframework.stereotype.Service;

@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyInfoDAO, ClassifyInfo> implements ClassifyService {
}

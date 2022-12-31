package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.ClassifyInfoDAO;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Entities.ClassifyInfo;
import com.xiaoxu.xBlog.Service.ArticleService;
import com.xiaoxu.xBlog.Service.ClassifyService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyInfoDAO, ClassifyInfo> implements ClassifyService {

    @Autowired
    private ArticleService articleService;

    @Override
    @Transactional
    public void removeByIdWithChecked(Integer classifyId) throws Exception{
        LambdaQueryWrapper<ArticleInfo> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(ArticleInfo::getArticleClassifyId,classifyId);
        Integer count = articleService.count(qw1);
        //检查当前分类下是否有文章，有文章则不能删除
        if (count != null && count > 0) {
            throw new RuntimeException("当前分类下还有文章，不能删除!");
        }else {
            if(!this.removeById(classifyId)){
                throw new RuntimeException("系统出错了,删除失败!");
            }
        }
    }

}

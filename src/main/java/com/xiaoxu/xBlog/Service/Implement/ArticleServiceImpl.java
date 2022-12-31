package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.ArticleInfoDAO;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Entities.ClassifyInfo;
import com.xiaoxu.xBlog.Entities.CommentInfo;
import com.xiaoxu.xBlog.Service.ArticleService;
import com.xiaoxu.xBlog.Service.ClassifyService;
import com.xiaoxu.xBlog.Service.CommentService;
import com.xiaoxu.xBlog.Utils.ReturnResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleInfoDAO, ArticleInfo> implements ArticleService {

    @Autowired
    private CommentService commentService;
    @Override
    @Transactional
    public void removeByIdByChecked(Integer articleId) throws Exception{
        LambdaQueryWrapper<CommentInfo> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(CommentInfo::getArticleId,articleId);
        List<CommentInfo> commentInfos = commentService.list(qw1);
        //检查当前文章下是否有评论，有评论则先删除评论
        if (commentInfos != null && commentInfos.size() > 0) {
            List<Integer> idList = commentInfos.stream().map(item->item.getCommentId()).collect(Collectors.toList());
            if(!commentService.removeByIds(idList)){
                throw new RuntimeException("系统异常,删除失败");
            }

        }
        if(!this.removeById(articleId)){
            throw new RuntimeException("系统异常,删除失败");
        }
    }
}

package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.ArticleInfo;
import com.xiaoxu.xBlog.Utils.ReturnResults;

public interface ArticleService extends IService<ArticleInfo> {

    void removeByIdByChecked(Integer articleId) throws Exception;
}

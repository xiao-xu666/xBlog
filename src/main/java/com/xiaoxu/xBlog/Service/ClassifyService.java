package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.ClassifyInfo;
import com.xiaoxu.xBlog.Utils.ReturnResults;

import java.util.List;

public interface ClassifyService extends IService<ClassifyInfo> {
    void removeByIdWithChecked(Integer classifyId) throws Exception;

}

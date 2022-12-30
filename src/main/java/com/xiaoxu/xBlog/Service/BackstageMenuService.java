package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.BackstageMenuInfo;

import java.util.List;

public interface BackstageMenuService extends IService<BackstageMenuInfo> {
    List<BackstageMenuInfo> showAllMenuWithChild(Integer usertype);
}

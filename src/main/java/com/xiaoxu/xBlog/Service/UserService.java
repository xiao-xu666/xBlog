package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.UserInfo;

public interface UserService extends IService<UserInfo> {

    boolean removeByChangeState(Integer userId);
}

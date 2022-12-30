package com.xiaoxu.xBlog.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxu.xBlog.Entities.SystemSetup;

import java.util.List;

public interface SystemSetupService extends IService<SystemSetup> {

    public List<SystemSetup> getAllSystemSetup();

}

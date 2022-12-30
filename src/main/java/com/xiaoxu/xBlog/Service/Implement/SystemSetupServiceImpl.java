package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.SystemSetupDAO;
import com.xiaoxu.xBlog.Entities.SystemSetup;
import com.xiaoxu.xBlog.Service.SystemSetupService;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemSetupServiceImpl extends ServiceImpl<SystemSetupDAO, SystemSetup> implements SystemSetupService {


    @Override
    public List<SystemSetup> getAllSystemSetup() {
        List<SystemSetup> systemSetups = this.list();
        return systemSetups;
    }


}

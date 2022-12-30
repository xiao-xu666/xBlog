package com.xiaoxu.xBlog.Service.Implement;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxu.xBlog.DAO.BackstageMenuInfoDAO;
import com.xiaoxu.xBlog.Entities.BackstageMenuInfo;
import com.xiaoxu.xBlog.Service.BackstageMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BackstageMenuServiceImpl extends ServiceImpl<BackstageMenuInfoDAO, BackstageMenuInfo> implements BackstageMenuService {
    @Override
    @Transactional
    public List<BackstageMenuInfo> showAllMenuWithChild(Integer usertype) {
        Map<Integer,BackstageMenuInfo> menu = new HashMap<>();
        List<BackstageMenuInfo> menuInfoList = null;
        if (usertype != 0){
            LambdaQueryWrapper<BackstageMenuInfo> qw = new LambdaQueryWrapper<>();
            qw.eq(BackstageMenuInfo::getMenuStatus,usertype);
            menuInfoList = this.list(qw);
        }else{
            menuInfoList = this.list();
        }
        for (BackstageMenuInfo item: menuInfoList) {
            List<BackstageMenuInfo> children = new ArrayList<>();
            if(item.getParentMenuId() == 0){
                item.setChildMenu(new ArrayList<>());
                menu.put(item.getId(), item);
            }else{
                BackstageMenuInfo menuInfo = menu.get(item.getParentMenuId());
                List<BackstageMenuInfo> childMenu = menuInfo.getChildMenu();
                childMenu.add(item);
                menuInfo.setChildMenu(childMenu);
                menu.put(item.getParentMenuId(),menuInfo);
            }
        }
        List<BackstageMenuInfo> menuInfos = menu.values().stream().collect(Collectors.toList());
        return menuInfos;
    }
}

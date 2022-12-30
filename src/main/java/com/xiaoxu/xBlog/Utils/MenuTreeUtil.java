package com.xiaoxu.xBlog.Utils;

import com.xiaoxu.xBlog.Entities.BackstageMenuInfo;

import java.util.ArrayList;
import java.util.List;


public class MenuTreeUtil {
    /**
     * 所有的菜单
     */
    private static List<BackstageMenuInfo> allList = null;

    /**
     * 转换为树形结构
     * @param list 所有的节点
     * @return 树结构菜单
     */
    public static List<BackstageMenuInfo> toTree(List<BackstageMenuInfo> list) {
        allList = new ArrayList<>(list);

        // 获取所有的一级菜单，父菜单 id 为 0
        List<BackstageMenuInfo> roots = new ArrayList<>();

        // 遍历
        for (BackstageMenuInfo menuTreeVO: list) {
            if (menuTreeVO.getParentMenuId() == 0) {
                roots.add(menuTreeVO);
            }
        }

        // 删除一级菜单
        allList.removeAll(roots);

        // 对每一个一级菜单添加二级菜单
        for (BackstageMenuInfo menuTreeVO: roots) {
            // 设置子菜单
            menuTreeVO.setChildMenu(getCurrentChildrenMenu(menuTreeVO));
        }
        return roots;
    }

    /**
     * 通过父菜单获取子菜单列表
     * @param parentMenu 父菜单
     * @return 子菜单列表
     */
    private static List<BackstageMenuInfo> getCurrentChildrenMenu(BackstageMenuInfo parentMenu) {
        // 判断当前节点是否已经存在子结点
        List<BackstageMenuInfo> childMenuList;
        if (parentMenu.getChildMenu() == null) {
            childMenuList = new ArrayList<>();;
        } else {
            childMenuList = parentMenu.getChildMenu();
        }
        // 遍历所有的菜单，除了一级菜单，之前删过
        for (BackstageMenuInfo childMenu: allList) {
            if (parentMenu.getId() == childMenu.getParentMenuId()) {
                // 某个菜单的父菜单 id 等于当前菜单，这个菜单就是子菜单
                childMenuList.add(childMenu);
            }
        }
        allList.removeAll(childMenuList);

        return childMenuList;
    }
}

package com.kkIntegration.ossd.service.menu.impl;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.common.utils.CommonUtils;
import com.kkIntegration.ossd.dao.menu.MenuDAO;
import com.kkIntegration.ossd.entity.menu.MainMenuEntity;
import com.kkIntegration.ossd.entity.menu.MenuEntity;
import com.kkIntegration.ossd.entity.menu.MenuNoUrlEntity;
import com.kkIntegration.ossd.entity.menu.OneMenuEntity;
import com.kkIntegration.ossd.service.menu.MenuService;
import com.kkIntegration.ossd.utils.MenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: ckk
 * create: 2019-12-31 13:30
 */
@Service(value = "menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuDAO menuDAO;


    @Override
    public List<MenuNoUrlEntity> selectAllMenuNotUrl() {
        return menuDAO.selectAllMenuNotUrl();
    }

    @Override
    public Map<String, OneMenuEntity> selectAllMenu() {
        List<MenuEntity> menus = menuDAO.selectAllMenu();
        // 菜单数量不大，可直接暴力循环组合数据
        // 后续 ： 加缓存为数据库减压
        Map<String, OneMenuEntity> allMenus = new HashMap<>();
        for (MenuEntity m : menus) {
            if (m.getMenu_id().startsWith("all&")) {
                continue;
            } else {
                String[] mnos = m.getMenu_id().split("&")[0].split("-"); // 00.01.01, 01 , 0
                String[] nos = mnos[0].split("\\."); //  00 , 01, 01
                OneMenuEntity current = null;
                // 只有 00 时候
                if (nos.length == 1) {
                    if (!allMenus.containsKey(mnos[1])) {
                        allMenus.put(mnos[1], new OneMenuEntity());
                    }
                    allMenus.get(mnos[1]).setMenu(m);
                } else {
                    // 是否存在一级菜单
                    if (!allMenus.containsKey(nos[1])) {
                        allMenus.put(nos[1], new OneMenuEntity());
                    }
                    current = allMenus.get(nos[1]);

                    for (int i = 2, len = nos.length; i < len; i++) {

                        if (!current.getSon_menus().containsKey(nos[i])) {
                            current.getSon_menus().put(nos[i], new OneMenuEntity());
                        }

                        current = current.getSon_menus().get(nos[i]);

                        if (i + 1 == len) {
                            if (!current.getSon_menus().containsKey(mnos[1])) {
                                current.getSon_menus().put(mnos[1], new OneMenuEntity());
                            }

                            //current = current.getSon_menus().get(mnos[0]);
                        }
                    }
                    current.getSon_menus().put(mnos[1], new OneMenuEntity(m));
                }

            }
        }

        return allMenus;
    }

    @Override
    public PageEntity getLimitMenu(int currentSize, int pageSize) {
        int totalSize = menuDAO.totalSize();
        PageEntity pageEntity = new PageEntity();
        // data数据
        pageEntity.setDataList(menuDAO.selectMenuLimit((currentSize - 1) * pageSize, pageSize));
        // 当前位置
        pageEntity.setCurrentSize(currentSize);
        // 每页数量
        pageEntity.setPageSize(pageSize);
        // 是否最后一页
        pageEntity.setLast(currentSize * pageSize >= totalSize);
        // 总的个数
        pageEntity.setTotalSize(totalSize);
        return pageEntity;
    }

    /**
     * 获取主菜单
     *
     * @return
     */
    @Override
    public List<MainMenuEntity> getMainMenu() {
        List<MainMenuEntity> mainMenuEntities = new ArrayList<>();
        List<MenuEntity> entities = menuDAO.getMainMenu();
        for (MenuEntity entity : entities) {
            if (!entity.getMenu_id().startsWith("all"))
                mainMenuEntities.add(new MainMenuEntity(getMenuNo(entity.getMenu_id()), entity.getMenu_name()));
        }
        return mainMenuEntities;
    }

    @Override
    public List<MainMenuEntity> getParMenu() {
        List<MainMenuEntity> mainMenuEntities = new ArrayList<>();
        List<MenuEntity> entities = menuDAO.selectAllMenu();
        for (MenuEntity entity : entities) {
            if (!entity.getMenu_id().startsWith("all"))
                mainMenuEntities.add(new MainMenuEntity(getMenuNo(entity.getMenu_id()), entity.getMenu_name()));
        }
        return mainMenuEntities;
    }

    @Override
    public List<MainMenuEntity> getSonMenu(String no) {
        List<MainMenuEntity> mainMenuEntities = new ArrayList<>();
        List<MenuEntity> entities = menuDAO.getSonMenu(no + "-%");
        for (MenuEntity entity : entities) {
            if (!entity.getMenu_id().startsWith("all")) {
                mainMenuEntities.add(new MainMenuEntity(getMenuNo(entity.getMenu_id()), entity.getMenu_name()));
            }
        }
        return mainMenuEntities;
    }

    @Override
    public void insertMenu(String parentNo, String menuName, int type, String menuUrl) {
        // 插入新的菜单
        MenuEntity menuEntity = new MenuEntity();

        menuEntity.setMenu_id(MenuUtils.getMenuId(parentNo, menuDAO.getCountMenu(parentNo + "-%"), type));
        menuEntity.setMenu_name(menuName);
        menuEntity.setMenu_url(menuUrl);
        menuDAO.insertMenu(menuEntity);
        // 判断是否已经存在父标签所有权限
        // 数据库不存在，创建
        int end = parentNo.lastIndexOf(".");
        String parno = parentNo.substring(0, end) + "-" + parentNo.substring(end + 1);
        if (!"00".equals(parentNo) && menuDAO.isExistAllNo("all&" + parno + "-%") == null) {
            MenuEntity perMenu = menuDAO.getPerMenu(parno + "-%");
            MenuEntity allMenuEntity = new MenuEntity();
            allMenuEntity.setMenu_id("all&" + perMenu.getMenu_id().split("&")[0] + "&" + CommonUtils.uuid32Generator());
            allMenuEntity.setMenu_name(perMenu.getMenu_name() + "所有");
            allMenuEntity.setMenu_url("nil");
            menuDAO.insertMenu(allMenuEntity);
        }
    }

    @Override
    public void updateMenuUrl(String new_menu_url, Integer id, String menu_id) {
        menuDAO.updateMenuUrl(new_menu_url, id, menu_id);
    }

    @Override
    public void updateMenuName(String new_menu_name, Integer id, String menu_id) {
        menuDAO.updateMenuName(new_menu_name, id, menu_id);
    }

    @Override
    public void deleteMenu(Integer id, String menu_id) {
        menuDAO.deleteMenu(id, menu_id);
    }

    private String getMenuNo(String menuId) {
        return menuId.split("&")[0].split("-")[0] + "." + menuId.split("&")[0].split("-")[1];
    }


}

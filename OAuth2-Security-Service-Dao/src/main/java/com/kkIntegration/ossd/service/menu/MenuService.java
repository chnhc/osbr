package com.kkIntegration.ossd.service.menu;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.ossd.entity.menu.MainMenuEntity;
import com.kkIntegration.ossd.entity.menu.MenuEntity;
import com.kkIntegration.ossd.entity.menu.MenuNoUrlEntity;
import com.kkIntegration.ossd.entity.menu.OneMenuEntity;

import java.util.List;
import java.util.Map;

public interface MenuService {

    List<MenuNoUrlEntity> selectAllMenuNotUrl();

    Map<String, OneMenuEntity> selectAllMenu();

    PageEntity getLimitMenu(int currentSize, int pageSize);

    List<MainMenuEntity> getMainMenu();

    List<MainMenuEntity> getParMenu();

    List<MainMenuEntity> getSonMenu(String no);

    void insertMenu(String parentNo, String menuName, int type, String menuUrl);

    void updateMenuUrl(String new_menu_url, Integer id, String menu_id);

    void updateMenuName(String new_menu_name, Integer id, String menu_id);

    void deleteMenu(Integer id, String menu_id);

}

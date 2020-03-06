package com.kkIntegration.ossd.dao.menu;

import com.kkIntegration.ossd.entity.menu.MenuEntity;
import com.kkIntegration.ossd.entity.menu.MenuNoUrlEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "menuDAO")
public interface MenuDAO {

    List<MenuNoUrlEntity> selectAllMenuNotUrl();

    List<MenuEntity> selectAllMenu();

    int totalSize();

    List<MenuEntity> selectMenuLimit(Integer form_size, Integer to_size);

    void insertMenu(MenuEntity menuEntity);

    void updateMenuUrl(String new_menu_url, Integer id, String menu_id);

    void updateMenuName(String new_menu_name, Integer id, String menu_id);

    void deleteMenu(Integer id, String menu_id);

    List<MenuEntity> getMainMenu();

    List<MenuEntity> getSonMenu(String like_no);

    Integer getCountMenu(String like_no);

    Integer isExistAllNo(String like_no);

    MenuEntity getPerMenu(String like_no);
}

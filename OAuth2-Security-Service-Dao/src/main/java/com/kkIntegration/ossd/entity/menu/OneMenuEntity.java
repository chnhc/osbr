package com.kkIntegration.ossd.entity.menu;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 单个菜单
 * author: ckk
 * create: 2019-12-31 13:15
 */
@Data
public class OneMenuEntity {

    MenuEntity menu;

    Map<String,OneMenuEntity> son_menus = new HashMap<>();


    public OneMenuEntity() {
    }

    public OneMenuEntity(MenuEntity menu) {
        this.menu = menu;
    }


    public OneMenuEntity(MenuEntity menu, Map<String, OneMenuEntity> son_menus) {
        this.menu = menu;
        this.son_menus = son_menus;
    }
}

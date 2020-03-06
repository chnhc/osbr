package com.kkIntegration.ossd.entity.menu;

import lombok.Data;

/**
 * description: 菜单实体
 * author: ckk
 * create: 2019-12-31 13:20
 */
@Data
public class MainMenuEntity {

    String no ;

    String menu_name ;

    public MainMenuEntity() {

    }

    public MainMenuEntity(String no, String menu_name) {
        this.no = no;
        this.menu_name = menu_name;
    }
}

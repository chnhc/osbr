package com.kkIntegration.ossd.controller;


import com.kkIntegration.common.utils.HttpUtils;
import com.kkIntegration.common.utils.SecurityOauth2Utils;
import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.response.HttpResponse;
import com.kkIntegration.ossd.service.menu.MenuService;
import com.kkIntegration.ossd.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    /**
     * 判断用户是否有访问菜单的权限
     *
     * @param menuId
     * @return
     */
    @PostMapping("/isMenuHaveRole")
    public BaseResponse isMenuHaveRole(@RequestParam("menuId") String menuId) {


        // 菜单id :
        // 1、全菜单 all&开始
        // 2 单菜单

        // 获取菜单序号 1-0-1&..... 或者 all&1-0-1&......
        //String menuPrefix = RoleMenuUtils.getMenuPrefix(menuId);

        // 验证操作流程
        // 1、获取所有的权限
        // 2、通过权限查询对应的菜单权限
        // 3、判断查询菜单和当前菜单权限

        boolean isHaveRole = false;

        // 1、获取用户所有的权限
        Collection<? extends GrantedAuthority> roles = SecurityOauth2Utils.getAllRoles();
        if (roles != null && roles.size() != 0) {
            List<String> roleList = new ArrayList<>();
            for (GrantedAuthority r : roles) {
                roleList.add(r.getAuthority());
            }
            isHaveRole = roleService.isMenuHaveRole(menuId, roleList);

        }

        return HttpUtils.Response(BaseResponseStatus.SUCCESS, isHaveRole);


    }

    @GetMapping("/getAllMenu")
    public BaseResponse getAllMenu() {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, menuService.selectAllMenu());
    }



}

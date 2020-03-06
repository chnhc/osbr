package com.kkIntegration.ossd.controller;

import com.kkIntegration.common.properties.KkIntegrationSecurityProperties;
import com.kkIntegration.common.response.BaseResponse;
import com.kkIntegration.common.response.BaseResponseStatus;
import com.kkIntegration.common.utils.PinYinUtils;
import com.kkIntegration.common.utils.SecurityOauth2Utils;
import com.kkIntegration.ossd.entity.role.AuthRoleForm;
import com.kkIntegration.ossd.entity.role.RoleEntity;
import com.kkIntegration.ossd.service.role.RoleService;
import com.kkIntegration.common.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2020-01-02 14:35
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    KkIntegrationSecurityProperties properties;

    // 用户是否有权限进入后台管理
    // 具有 osbr_BackEnd 权限才能访问成功
    @GetMapping("/intoBackEnd")
    public BaseResponse intoBackEnd(){

        boolean isHaveRole = false;

        // 1、获取用户所有的权限
        Collection<? extends GrantedAuthority> roles = SecurityOauth2Utils.getAllRoles();
        if (roles != null && roles.size() != 0) {
            for (GrantedAuthority r : roles) {
                // 2、判断是否有进入后台权限
                if(properties.getBankEndId().equals(r.getAuthority())){
                    isHaveRole = true;
                    break;
                }
            }
        }

        return HttpUtils.Response(BaseResponseStatus.SUCCESS, isHaveRole);
    }


    /**
     * 插入一个权限
     * @return
     */
    @PostMapping("/addRole")
    public BaseResponse addRole(@RequestParam("role_name") String role_name) {
        String name = role_name.trim();
        if(name.isEmpty() ){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole_id(properties.getPrefixRole()+"_"+ PinYinUtils.getHanziPinYin(name));
        roleEntity.setRole_name(name);
        roleService.insertRole(roleEntity);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 更新 权限id
     * @param new_role_id
     * @param id
     * @param role_id
     * @return
     */
    @PostMapping("/updateRoleId")
    public BaseResponse updateRoleId(@RequestParam("new_role_id") String new_role_id, @RequestParam("id") int id,  @RequestParam("role_id") String role_id) {

        if(new_role_id.trim().isEmpty() || role_id.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        roleService.updateRoleId(new_role_id, id, role_id);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 更新 权限名称
     * @param new_role_name
     * @param id
     * @param role_id
     * @return
     */
    @PostMapping("/updateRoleName")
    public BaseResponse updateRoleName(@RequestParam("new_role_name") String new_role_name, @RequestParam("id") int id,  @RequestParam("role_id") String role_id) {

        if(new_role_name.trim().isEmpty() || role_id.trim().isEmpty()){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        roleService.updateRoleName(new_role_name, id, role_id);
        return HttpUtils.Response(BaseResponseStatus.SUCCESS);
    }

    /**
     * 获取所有权限对应的关系
     * @return
     */
    @GetMapping("getAllRoles")
    public BaseResponse getAllRoles(){
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, roleService.selectAllRoles());
    }

    /**
     * 获取所有简单类型所有的权限
     * @return
     */
    @GetMapping("getAllSimpleRoles")
    public BaseResponse getAllSimpleRoles(){
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, roleService.selectSimpleAllRoles());
    }

    /**
     * 授权处理 -- 撤权 、 授权
     * @return
     */
    @PostMapping("authRole")
    public BaseResponse authRole(AuthRoleForm authRoleForm){
        if(authRoleForm.getTarget_id().trim().isEmpty() || authRoleForm.getTarget_type().trim().isEmpty() ){
            return HttpUtils.Response(BaseResponseStatus.Verify_Failed);
        }
        return HttpUtils.Response(BaseResponseStatus.SUCCESS,  roleService.authRole(authRoleForm.getTarget_id(), authRoleForm.getTarget_roles(),
                authRoleForm.getTarget_type()));

    }

    /**
     * 获取所有的权限列表 -- 分页
     * @return
     */
    @GetMapping("/getLimitRole")
    public BaseResponse getLimitRole(@RequestParam("current_size") int currentSize, @RequestParam("page_size") int pageSize) {
        return HttpUtils.Response(BaseResponseStatus.SUCCESS, roleService.getLimitRole(currentSize, pageSize));
    }

    /*

            SELECT r.id, r.role_id, r.role_name, rf.fun_id AS fm_id, f.fun_name AS fm_name, 'fun' AS fm_type FROM role r LEFT JOIN role_fun rf ON  r.role_id = rf.role_id LEFT JOIN `function` f ON rf.fun_id = f.fun_id
        UNION
        SELECT r.id, r.role_id, r.role_name, rm.menu_id AS fm_id, m.menu_name AS fm_name, 'menu' AS fm_type FROM role r LEFT JOIN role_menu rm ON  r.role_id = rm.role_id LEFT JOIN menu m ON rm.menu_id = m.menu_id


	SELECT	role_id , fun_id FROM role_fun WHERE role_id = 'osbr_teacher'


	SELECT r.id, r.role_id, r.role_name, rf.fun_id AS fm_id, f.fun_name AS fm_name, 'fun' AS fm_type FROM role r LEFT JOIN role_fun rf ON  r.role_id = rf.role_id LEFT JOIN `function` f ON rf.fun_id = f.fun_id
        UNION
        SELECT r.id, r.role_id, r.role_name, rm.menu_id AS fm_id, m.menu_name AS fm_name, 'menu' AS fm_type FROM role r LEFT JOIN role_menu rm ON  r.role_id = rm.role_id LEFT JOIN menu m ON rm.menu_id = m.menu_id
        LIMIT 0, 2;


        SELECT u.id, u.user_id, u.user_name,r.role_id,r.role_name  FROM `user` u  LEFT JOIN role_user ru ON u.user_id = ru.user_id LEFT JOIN role r ON ru.role_id = r.role_id

    */

}

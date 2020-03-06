package com.kkIntegration.ossd.service.role.impl;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.common.properties.KkIntegrationSecurityProperties;
import com.kkIntegration.common.utils.CommonUtils;
import com.kkIntegration.ossd.dao.role.RoleDAO;
import com.kkIntegration.ossd.entity.role.*;
import com.kkIntegration.ossd.service.role.RoleService;
import com.kkIntegration.ossd.utils.RoleMenuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2019-12-17 15:20
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDAO roleDAO;

    // 整合安全配置
    @Autowired
    KkIntegrationSecurityProperties kkIntegrationSecurityProperties;

    @Override
    public UserRoleEntity selectUserRoleByName(String user_name) {
        return roleDAO.selectUserRoleByName(user_name);
    }

    @Override
    public List<FunRoleEntity> selectFunRole() {
        if (roleDAO == null) {
            return null;
        }
        return roleDAO.selectFunRole();
    }

    /**
     * 判断菜单是否有权限
     *
     * @param menuId 1-0-1&..... 或者 all&1-0-1&......
     * @param roles  权限列表
     * @return 是否有访问菜单权限
     */
    @Override
    public boolean isMenuHaveRole(String menuId, List<String> roles) {

        // 获取 所有权限对应的菜单
        List<String> menus = roleDAO.getAllMenuByRole(roles);
        boolean isHaveRole = false;
        for (String m : menus) {
            // 所有权限
            if (m.startsWith("all&00-00-0&")) {
                isHaveRole = true;
                break;
            }
            // 先做全匹配
            if (menuId.equals(m)) {
                isHaveRole = true;
                break;
            }
            // 如果不是 all& 开头， 判断是否有访问所有的权限
            // 数据库菜单也必须是 all& 开头
            if (!menuId.startsWith("all&") && m.startsWith("all&")) {
                String menuPrefix = menuId.split("&")[0];
                if (RoleMenuUtils.checkStartWithAllRoleMenu(m, menuPrefix)) {
                    isHaveRole = true;
                    break;
                }
            }
        }


        return isHaveRole;
    }

    /**
     * 插入一条权限
     *
     * @param roleEntity
     */
    @Override
    public void insertRole(RoleEntity roleEntity) {
        roleDAO.insertRole(roleEntity);
    }

    /**
     * 更新 权限id
     *
     * @param new_role_id
     * @param id
     * @param role_id
     */
    @Override
    public void updateRoleId(String new_role_id, Integer id, String role_id) {
        roleDAO.updateRoleId(new_role_id, id, role_id);
    }


    /**
     * 更新 权限名称
     *
     * @param new_role_name
     * @param id
     * @param role_id
     */
    @Override
    public void updateRoleName(String new_role_name, Integer id, String role_id) {
        roleDAO.updateRoleName(new_role_name, id, role_id);
    }

    /**
     * 删除权限
     *
     * @param id
     * @param role_id
     */
    @Override
    public void deleteRole(Integer id, String role_id) {
        roleDAO.deleteRole(id, role_id);
    }

    @Override
    public List<OneRolesEntity> selectAllRoles() {
        return roleDAO.selectAllRoles();
    }

    @Override
    public List<SimpleRoleEntity> selectSimpleAllRoles() {
        return roleDAO.selectSimpleAllRoles();
    }

    @Override
    public boolean authRole(String target_id, List<String> target_roles, String target_type) {
        if ("fun".equals(target_type)) {
            List<String> funRoleEntities = roleDAO.selectFunRoleByRoleID(target_id);
            // 判断 funRoleEntities 里面不存在 ，删除数据库信息
            for(String s : funRoleEntities){
                if(!target_roles.contains(s)){
                    // 删除方法 s
                    roleDAO.deleteRoleFun(target_id, s);
                    kkIntegrationSecurityProperties.getRolePathMap();
                    // 删除节点
                    if(kkIntegrationSecurityProperties.getRolePathMap().containsKey(target_id)){
                        if(kkIntegrationSecurityProperties.getRolePathMap().get(target_id).contains(s)){
                            kkIntegrationSecurityProperties.getRolePathMap().get(target_id).remove(s);
                        }
                    }
                }
            }
            // 判断 target_roles 里面不存在， 添加数据库
            for(String s : target_roles){
                if(!funRoleEntities.contains(s)){
                    // 添加方法 s
                    roleDAO.insertRoleFun(target_id, s);
                    // 添加节点
                    if(kkIntegrationSecurityProperties.getRolePathMap().contains(target_id)){
                        if(!kkIntegrationSecurityProperties.getRolePathMap().get(target_id).contains(s)){
                            kkIntegrationSecurityProperties.getRolePathMap().get(target_id).add(s);
                        }
                    }else{
                        // 不存在，手动添加
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(s);
                        kkIntegrationSecurityProperties.getRolePathMap().put(target_id, paths );
                    }
                }
            }
            return true;
        } else if ("menu".equals(target_type)) {
            List<String> menuRoleEntities = roleDAO.selectMenuRoleByRoleID(target_id);
            for(String s : menuRoleEntities) {
                if (!target_roles.contains(s)) {
                    // 删除菜单 s
                    roleDAO.deleteRoleMenu(target_id, s);
                }
            }
            for(String s : target_roles){
                if(!menuRoleEntities.contains(s)){
                    // 添加菜单 s
                    roleDAO.insertRoleMenu(target_id, s);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public PageEntity getLimitRole(int currentSize, int pageSize) {

        int totalSize = roleDAO.totalSize();
        PageEntity pageEntity = new PageEntity();
        // data数据
        pageEntity.setDataList(roleDAO.selectRoleLimit((currentSize - 1) * pageSize, pageSize));
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
}

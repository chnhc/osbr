package com.kkIntegration.ossd.service.role;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.ossd.entity.role.*;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    UserRoleEntity selectUserRoleByName(String user_name);

    List<FunRoleEntity> selectFunRole();

    boolean isMenuHaveRole(String menuId, List<String> roles);

    void insertRole(RoleEntity roleEntity);

    void updateRoleId(String new_role_id, Integer id, String role_id);

    void updateRoleName(String new_role_name, Integer id, String role_id);

    void deleteRole(Integer id, String role_id);

    List<OneRolesEntity> selectAllRoles();

    List<SimpleRoleEntity> selectSimpleAllRoles();

    boolean authRole(String target_id, List<String> target_roles , String target_type);

    PageEntity getLimitRole(int currentSize, int pageSize);
}

package com.kkIntegration.ossd.dao.role;


import com.kkIntegration.ossd.entity.menu.MenuEntity;
import com.kkIntegration.ossd.entity.role.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "roleDAO")
public interface RoleDAO {

    UserRoleEntity selectUserRoleByName(String user_name);

    List<FunRoleEntity> selectFunRole();

    List<String> selectFunRoleByRoleID(String role_id);

    List<String> selectMenuRoleByRoleID(String role_id);

    List<String> getAllMenuByRole(@Param("roles")List<String> roles);

    void insertRole(RoleEntity roleEntity);

    void updateRoleId(String new_role_id, Integer id, String role_id);

    void updateRoleName(String new_role_name, Integer id, String role_id);

    void deleteRole(Integer id, String role_id);

    void deleteRoleFun(String role_id, String fun_id);

    void insertRoleFun(String role_id, String fun_id);

    void deleteRoleMenu(String role_id, String menu_id);

    void insertRoleMenu(String role_id, String menu_id);

    List<SimpleRoleEntity> selectSimpleAllRoles();

    List<OneRolesEntity> selectAllRoles();

    int totalSize();

    List<OneRolesEntity> selectRoleLimit(Integer form_size, Integer to_size);


}

package com.kkIntegration.ossd.dao.role;


import com.kkIntegration.ossd.entity.role.OneRolesEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "roleUserDAO")
public interface RoleUserDAO {

    int totalSize();

    List<OneRolesEntity> selectRoleUserLimit(Integer form_size, Integer to_size);

    List<String> selectRoleUserByUserID(String user_id);

    void deleteRoleUser(String user_id ,String role_id );

    void insertRoleUser(String user_id ,String role_id );
}

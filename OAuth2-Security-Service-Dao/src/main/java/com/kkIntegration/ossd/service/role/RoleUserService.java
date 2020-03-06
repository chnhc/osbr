package com.kkIntegration.ossd.service.role;

import com.kkIntegration.common.entity.PageEntity;
import com.kkIntegration.ossd.entity.role.FunRoleEntity;
import com.kkIntegration.ossd.entity.role.OneRolesEntity;
import com.kkIntegration.ossd.entity.role.RoleEntity;
import com.kkIntegration.ossd.entity.role.UserRoleEntity;

import java.util.List;

public interface RoleUserService {

    PageEntity getLimitRoleUser(int currentSize, int pageSize);

    boolean authRoleUser(String target_id, List<String> target_roles);


}

package com.kkIntegration.ossd.entity.role;

import lombok.Data;

import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2020-01-02 15:13
 */
@Data
public class OneRolesEntity {

    int id;

    String role_id;

    String role_name;

    List<OneRoleEntity> roles;

}

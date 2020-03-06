package com.kkIntegration.ossd.entity.role;

import lombok.Data;

import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2020-01-02 15:13
 */
@Data
public class OneRoleUsersEntity {

    int id;

    String user_id;

    String user_name;

    List<OneRoleUserEntity> roles;

}

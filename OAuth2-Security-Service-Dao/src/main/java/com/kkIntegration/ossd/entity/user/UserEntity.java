package com.kkIntegration.ossd.entity.user;

import lombok.Data;

/**
 * description: 用户
 * author: ckk
 * create: 2019-04-20 08:18
 */
@Data
public class UserEntity {

    private int id;
    private String user_id;

    private String user_name;
    private String user_pwd;

}

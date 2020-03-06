package com.kkIntegration.ossd.service.user;


import com.kkIntegration.ossd.entity.role.UserRoleEntity;
import com.kkIntegration.ossd.entity.user.UserEntity;

import java.util.List;

public interface UserService {


    List<String> selectUserRole(String user_name);

    String selectUserPwd(String user_name);

    void insertUser();


}

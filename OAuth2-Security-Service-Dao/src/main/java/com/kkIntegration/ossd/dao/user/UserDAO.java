package com.kkIntegration.ossd.dao.user;

import com.kkIntegration.ossd.entity.role.UserRoleEntity;
import com.kkIntegration.ossd.entity.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description: 用户操作
 * author: ckk
 * create: 2019-04-20 08:15
 */
@Repository(value = "userDAO")
public interface UserDAO {

    List<String> selectUserRole(String user_name);

    String selectUserPwd(String user_name);

    void insertUser(UserEntity userEntity);

}

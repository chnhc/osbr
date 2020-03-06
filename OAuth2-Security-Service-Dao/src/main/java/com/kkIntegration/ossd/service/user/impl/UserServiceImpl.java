package com.kkIntegration.ossd.service.user.impl;


import com.kkIntegration.ossd.dao.oauth.NameDAO;
import com.kkIntegration.ossd.dao.user.UserDAO;
import com.kkIntegration.ossd.entity.oauth.Name;
import com.kkIntegration.ossd.entity.role.UserRoleEntity;
import com.kkIntegration.ossd.entity.user.UserEntity;
import com.kkIntegration.ossd.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * description: 用户操作
 * author: ckk
 * create: 2019-04-20 08:24
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserDAO userDAO;

    @Autowired
    private NameDAO nameDAO;

    @Override
    public List<String> selectUserRole(String user_name) {
        return userDAO.selectUserRole(user_name);
    }

    @Override
    public String selectUserPwd(String user_name) {
        return userDAO.selectUserPwd(user_name);
    }

    @Override
    @Transactional
    public void insertUser() {


        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id("my_user_1");
        userEntity.setUser_name("测试1");
        userEntity.setUser_pwd("123123123");

        userDAO.insertUser(userEntity);

        float a=  1/0;
       /* UserEntity userEntity1 = new UserEntity();
        userEntity1.setUser_id("my_user_2");
        userEntity1.setUser_name("测试2");
        userEntity1.setUser_pwd("123123123");

        userDAO.insertUser(userEntity1);*/

        Name name = new Name();
        name.setName("测试2");
        nameDAO.insertName(name);

    }
}

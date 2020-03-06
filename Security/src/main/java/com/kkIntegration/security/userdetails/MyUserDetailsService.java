package com.kkIntegration.security.userdetails;

import com.kkIntegration.ossd.entity.role.UserRoleEntity;
import com.kkIntegration.ossd.service.user.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 自定义UserDetailsService
 * author: ckk
 * create: 2019-03-31 09:41
 */

public class MyUserDetailsService implements UserDetailsService {

    private PasswordEncoder PasswordEncoder;

    private UserService userService;

    public MyUserDetailsService(PasswordEncoder PasswordEncoder , UserService userService ) {
        this.PasswordEncoder = PasswordEncoder;
        this.userService = userService;
    }


    /**
     * @param user 唯一用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        String s = user.trim();
        if(s.isEmpty()){
            // 设置游客权限
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            // ROLE_ANONYMOUS 和 框架 保持一致
            authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));

            return new User(s,
                    "",
                    authorities);
        }
        // 通过用户名 ，查询数据库， 获取权限 密码 用户名
        List<String> userRoleEntity = userService.selectUserRole(s);
        String pwd = userService.selectUserPwd(s);

        // 判断密码不为空
        if(pwd != null && !pwd.isEmpty()) {
            if (PasswordEncoder == null) {
                PasswordEncoder = new BCryptPasswordEncoder();
            }
            // 权限列表
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            // 为用户添加权限
            for(String a: userRoleEntity){
                authorities.add(new SimpleGrantedAuthority(a));
            }
            // 生成用户，交给spring后续判断
            return new User(s,
                    pwd,
                    authorities);
        }else{
            // 设置游客权限
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            // ROLE_ANONYMOUS 和 框架 保持一致
            authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));

            return new User(s,
                    "",
                    authorities);
        }

    }
}


package com.kkIntegration.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 整合 安全配置
 * author: ckk
 * create: 2019-12-17 13:12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kkincegration.security")
public class KkIntegrationSecurityProperties {

    // 存储 权限和方法路径 map< 权限， ArrayList<路径>>
    private ConcurrentHashMap<String, ArrayList<String>> rolePathMap;

    // role 前缀  ， application.yml 更改为: osbr
    private String prefixRole = "role";

    // 后台公开前端调用
    private String frontToEndId = "osbr_FrontToEnd";

    // 进入后台的权限id
    private String bankEndId = "osbr_BackEnd";

    // 是否是后台，开启后：所有登录后台用户，必须有  bankEndId  权限
    private boolean openBackEnd = false;
}

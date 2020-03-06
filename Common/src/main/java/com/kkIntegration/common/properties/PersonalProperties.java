package com.kkIntegration.common.properties;


import com.kkIntegration.common.properties.oatuth.AuthProperties;
import com.kkIntegration.common.properties.tokenStore.TokenStoreProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 个人配置
 */
@Component
@ConfigurationProperties(prefix = "personal")
@Order(1)
public class PersonalProperties {

    /**
     * 认证配置
     */
    private AuthProperties auth = new AuthProperties();

    /**
     * 认证配置
     */
    private TokenStoreProperties tokenStore = new TokenStoreProperties();

    public AuthProperties getAuth() {
        return auth;
    }

    public void setAuth(AuthProperties auth) {
        this.auth = auth;
    }

    public TokenStoreProperties getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(TokenStoreProperties tokenStore) {
        this.tokenStore = tokenStore;
    }
}



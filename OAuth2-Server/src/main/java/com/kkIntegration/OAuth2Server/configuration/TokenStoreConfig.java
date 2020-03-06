package com.kkIntegration.OAuth2Server.configuration;

import com.kkIntegration.OAuth2Common.jdbc.MyJdbcTokenStore;
import com.kkIntegration.OAuth2Server.provider.token.JwtTokenEnhancer;
import com.kkIntegration.OAuth2Server.provider.token.MyJwtAccessTokenConverter;
import com.kkIntegration.common.properties.PersonalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 *
 *  DefaultTokenServices 生成初始认证id DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
 *
 **/
@Configuration
public class TokenStoreConfig {

    @Autowired
    private PersonalProperties personalProperties;

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() throws Exception {

        TokenStore store = null;

        switch (personalProperties.getTokenStore().getTokenStoreType()) {
            case jwt:
                store = new JwtTokenStore(jwtAccessTokenConverter());
                break;
            case jdbc:
                // 系统数据库存储 spring 的 JdbcTemplate
                store = new JdbcTokenStore(dataSource);
                break;
            case my_jdbc:
                // 自定义数据库存储 mybatis
                store = new MyJdbcTokenStore();
                break;
            default:
                store = new InMemoryTokenStore();

        }

        return store;
    }

    @Bean
    @Primary
    @ConditionalOnExpression("'${personal.tokenStore.isNeedJWT}'=='true'")
    @ConditionalOnMissingBean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(personalProperties.getTokenStore().getTokenSigningKey());
        converter.setAccessTokenConverter(new MyJwtAccessTokenConverter());
        return converter;
    }

    @Bean
    @ConditionalOnExpression("'${personal.tokenStore.isNeedJWTExt}'=='true' && '${personal.tokenStore.isNeedJWT}'=='true'")
    public TokenEnhancer jwtTokenEnhancer(){
        return new JwtTokenEnhancer();
    }
}

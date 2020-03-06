package com.kkIntegration.OAuth2Server.configuration;

import com.kkIntegration.OAuth2Common.access.AppAccessDeniedHandler;
import com.kkIntegration.common.properties.KkIntegrationOAuth2Properties;
import com.kkIntegration.OAuth2Common.token.MyTokenServices;
import com.kkIntegration.OAuth2Server.filter.BootBasicAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * description: 认证服务器
 * author: ckk
 * create: 2019-12-15 13:01
 *
 * https://www.cnblogs.com/xifengxiaoma/p/10043173.html
 * https://www.jianshu.com/p/ea0a7d89f5f0
 * https://blog.csdn.net/m0_37834471/article/details/81162121
 */
@Configuration
@EnableAuthorizationServer
@Order(7)
public class OAuthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    // ExceptionTranslationFilter 里的一个规则
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    // AuthorizationEndpoint 请求错误， 会回调此方法
    @Autowired
    private WebResponseExceptionTranslator bootWebResponseExceptionTranslator;

    // 添加一个自定义的判断过滤器
    @Autowired
    private BootBasicAuthenticationFilter filter;

    // OAuth 2 配置
    @Autowired
    KkIntegrationOAuth2Properties kkIntegrationOAuth2Properties;

    // 密码工具，security设置
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 注入权限验证控制器 来支持 password grant type
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 注入userDetailsService，开启refresh_token需要用到
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 设置保存token的方式，一共有五种，这里采用数据库的方式
     */
    @Autowired
    private TokenStore tokenStore;

    // 静态跨域
    @Autowired
    CorsFilter corsFilter;

    @Autowired
    private AppAccessDeniedHandler appAccessDeniedHandler;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.addTokenEndpointAuthenticationFilter(filter);

        security
                .authenticationEntryPoint(authenticationEntryPoint)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                //.accessDeniedHandler(appAccessDeniedHandler)  // accessDenied 无法处理
                .allowFormAuthenticationForClients()
                .addTokenEndpointAuthenticationFilter(corsFilter);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //System.out.println(passwordEncoder.encode("123")); $2a$10$z0dk1gRO7Ymw8zq5UMxwQOg1lmy0wEqdhJuQMFRs1FCCZmedlmokG
        //clients.jdbc(dataSource);
        clients.inMemory()
                .withClient(kkIntegrationOAuth2Properties.getClientId())
                .secret(passwordEncoder.encode(kkIntegrationOAuth2Properties.getClientSecret()))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .scopes("all").accessTokenValiditySeconds(3600).refreshTokenValiditySeconds(2592000)
                .redirectUris("https://www.baidu.com");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 允许 post 和 get
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        //开启密码授权类型
        endpoints.authenticationManager(authenticationManager);
        //自定义登录或者鉴权失败时的返回信息
        endpoints.exceptionTranslator(bootWebResponseExceptionTranslator);
        //要使用refresh_token的话，需要额外配置userDetailsService
        endpoints.userDetailsService( userDetailsService );
        //配置token存储方式
        //endpoints.tokenStore(tokenStore);
        // 覆盖默认的 DefaultTokenServices
        // 使用自定义的TokenServices，
        // tokenServicesOverride = true ,不会使用默认的 createDefaultTokenServices() 方法
        endpoints.tokenServices(defaultTokenServices());

    }


    /**
     *  默认的 AuthorizationServerEndpointsConfigurer # createDefaultTokenServices()
     *  注意 ： redis 自动刷新还未实现
     */
    @Bean
    public MyTokenServices defaultTokenServices() {
        // 使用自定义的 TokenService {@link EMyTokenStore} 添加了刷新的方法
        // 自定义 bearerType
        final MyTokenServices defaultTokenServices = new MyTokenServices(kkIntegrationOAuth2Properties.getBearerType());
        // 默认添加的验证器
        defaultTokenServices.setAuthenticationManager(authenticationManager);
        // token 存储方式
        defaultTokenServices.setTokenStore(tokenStore);
        // 刷新的 refreshToken 可用的时间
        defaultTokenServices.setRefreshTokenValiditySeconds(kkIntegrationOAuth2Properties.getRefreshTokenSeconds());
        // Token 可用的时间
        defaultTokenServices.setAccessTokenValiditySeconds(kkIntegrationOAuth2Properties.getAccessTokenSeconds());
        // 是否支持 token 刷新
        defaultTokenServices.setSupportRefreshToken(true);
        // 是否重用 refreshToken
        defaultTokenServices.setReuseRefreshToken(true);
        // 默认添加的 UserDetailsService
        addUserDetailsService(defaultTokenServices, this.userDetailsService);
        // 返回自定义的TokenServices
        return defaultTokenServices;
    }


    /**
     * 默认的实现  AuthorizationServerEndpointsConfigurer # addUserDetailsService()
     * @param tokenServices
     * @param userDetailsService
     */
    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
                    userDetailsService));
            tokenServices
                    .setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider> asList(provider)));
        }
    }


}

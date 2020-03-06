package com.kkIntegration.OAuth2Resource.configuration;

import com.kkIntegration.OAuth2Resource.filter.RefreshHeadTokenFilter;
import com.kkIntegration.common.properties.KkIntegrationOAuth2Properties;
import com.kkIntegration.OAuth2Common.token.MyTokenServices;
import com.kkIntegration.OAuth2Common.access.AppAccessDeniedHandler;
import com.kkIntegration.OAuth2Resource.provider.authentication.MyTokenExtractor;
import com.kkIntegration.OAuth2Resource.provider.error.BootOAuth2AuthExceptionEntryPoint;
import com.kkIntegration.OAuth2Resource.provider.expression.AppSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

/**
 * description: 资源服务器
 * author: ckk
 * create: 2019-12-15 13:04
 */

@Configuration
@EnableResourceServer
public class OAuthResourceConfigurer extends ResourceServerConfigurerAdapter {

    // 自定义 权限 和 方法 鉴权过滤器
    @Autowired
    FilterSecurityInterceptor filterSecurityInterceptor;

    // 自定义 tokenService
    @Autowired
    private MyTokenServices defaultTokenServices;

    // OAuth 2 配置
    @Autowired
    KkIntegrationOAuth2Properties kkIntegrationOAuth2Properties;

    // 表达式匹配异常
    @Autowired
    private AppSecurityExpressionHandler appSecurityExpressionHandler;

    // 鉴权异常  ExceptionTranslationFilter # handleSpringSecurityException
    @Autowired
    private AppAccessDeniedHandler appAccessDeniedHandler;

 /*   @Bean
    public RefreshHeadTokenFilter refreshHeadTokenFilter(){
        return new RefreshHeadTokenFilter(kkIntegrationOAuth2Properties);
    }*/

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 注意 可以在 OAuth2AuthenticationProcessingFilter 前添加


        // 重新 自定义 权限 和 方法 鉴权过滤器
        http.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class );

        // 添加过滤器 ，在生成刷新token时，更新久的token , 防止错误异常时，newToken 的丢失
        //http.addFilterAfter(refreshHeadTokenFilter(), OAuth2AuthenticationProcessingFilter.class);

        // 必须设置的，所有请求都需要权限
        // OAuth2AuthenticationProcessingFilter
        http.authorizeRequests().anyRequest().authenticated();

    }

    //和鉴权服务有关,springboot2.0新加入部分
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // TODO Auto-generated method stub
        resources
                .tokenServices(defaultTokenServices)
                .expressionHandler(appSecurityExpressionHandler) // 打开HttpSecurity.access @ 加入鉴权方式
                // 权限问题 ： 接收 鉴权失败的信息
                .accessDeniedHandler(appAccessDeniedHandler)
                // 验证问题 ： token 验证失败
                .authenticationEntryPoint(new BootOAuth2AuthExceptionEntryPoint())
                // 在OAuth2AuthenticationProcessingFilter 使用的 token 解析
                .tokenExtractor(new MyTokenExtractor(kkIntegrationOAuth2Properties.getBearerType()))
                .resourceId("*")
                .stateless(true)
        //.tokenStore(tokenStore)
        ;

    }


}

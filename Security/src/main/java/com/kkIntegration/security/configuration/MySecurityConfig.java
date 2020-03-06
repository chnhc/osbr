package com.kkIntegration.security.configuration;

import com.kkIntegration.common.properties.KkIntegrationSecurityProperties;
import com.kkIntegration.ossd.entity.role.FunRoleEntity;
import com.kkIntegration.ossd.service.role.RoleService;
import com.kkIntegration.ossd.service.user.UserService;
import com.kkIntegration.security.authentication.MyAccessDeineHandler;
import com.kkIntegration.security.authentication.MyAuthenticationEntryPoint;
import com.kkIntegration.security.filter.MyFilterSecurityInterceptor;
import com.kkIntegration.security.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * description: 自定义security认证
 * author: ckk
 * create: 2019-03-31 09:49
 * https://blog.csdn.net/u012702547/article/details/89629415
 * https://blog.csdn.net/barlay/article/details/86625331
 * https://blog.csdn.net/sinat_29899265/article/details/80771330
 * https://www.cnblogs.com/dongying/p/6106855.html
 * https://www.cnblogs.com/xifengxiaoma/p/10020960.html 全
 */
@Configuration
@EnableWebSecurity
//来判断用户对某个控制层的方法是否具有访问权限
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(8)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;


    // 整合安全配置
    @Autowired
    KkIntegrationSecurityProperties kkIntegrationSecurityProperties;

    @PostConstruct
    public void postInit(){
        // 初始化 数据
        // 分布式 curd 问题
        initRolePathMap();
    }

    /**
     * 生成一个自定义的UserDetailsServiceImpl的Bean，交给Spring IOC容器
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(passwordEncoder(), userService);
    }

    /**
     * 生成定义一个PasswordEncoder的Bean，配置加密方式，这里生成BCryptPasswordEncoder的Bean对象
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

 /*   @Autowired(required = false)
    PasswordEncoder PasswordEncoder;*/

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 自定义 FilterSecurityInterceptor
     * 将直接使用自己的判断权限，不使用投票器等
     * @return
     */
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        // 投票器 -- 不使用
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter());
        // 自定义FilterSecurityInterceptor
        MyFilterSecurityInterceptor securityInterceptor = new MyFilterSecurityInterceptor(kkIntegrationSecurityProperties);
        // 默认资源 -- 不使用
        securityInterceptor.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>()));
        // 投票管理器 -- 不使用
        securityInterceptor.setAccessDecisionManager(new AffirmativeBased(decisionVoters));
        try {
            securityInterceptor.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置属性 不使用
        // securityInterceptor.afterPropertiesSet();
        // 返回自定义
        return securityInterceptor;
    }


    /**
     * 初始化 权限路径 map
     * 从数据初始化数据
     */
    private void initRolePathMap() {
        // 查询数据库初始化数据
        // 获取到 权限 -- 接口 -- 列表
        List<FunRoleEntity> funRoleEntities = roleService.selectFunRole();
        if(funRoleEntities == null || funRoleEntities.size()==0){
            return;
        }
        ConcurrentHashMap<String, ArrayList<String>> rolePathMap = kkIntegrationSecurityProperties.getRolePathMap();

        for(FunRoleEntity f: funRoleEntities){
            // 存在直接添加
            if(rolePathMap.containsKey(f.getRole_id())){
                rolePathMap.get(f.getRole_id()).add(f.getFun_id());
            }else{
                // 不存在，手动添加
                ArrayList<String> paths = new ArrayList<>();
                paths.add(f.getFun_id());
                rolePathMap.put(f.getRole_id(), paths );
            }
        }


    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService()) // 用户认证
                .passwordEncoder(passwordEncoder()); // 使用加密验证
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/config/**", "/css/**", "/fonts/**", "/img/**", "/js/**");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 添加 覆盖FilterSecurityInterceptor , 实现自定义鉴权
        http.addFilterBefore(filterSecurityInterceptor(),FilterSecurityInterceptor.class );

        // authenticationEntryPoint 处理方式  // accessDenied 无法处理
        // http.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint()).accessDeniedHandler(new MyAccessDeineHandler());


        // 关闭csrf 跨站伪造防护
        http.csrf().disable();


        /*        // 需要认证
        http.formLogin().and()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("admin");


        // api请求都不需要权限认证
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/name/**","/oauth/**")
                .permitAll()
                .antMatchers(HttpMethod.GET,"/name/**","/oauth/**")
                .permitAll();*/


     /*   http

                .formLogin()
                .and()
                .requestMatchers()
                .antMatchers("/dbe/name/**")
                .and()
                .authorizeRequests()    // 定义哪些URL需要被保护、哪些不需要被保护
                //.antMatchers("/oauth/check_token").permitAll()
                //.antMatchers("/dbe/name/**").permitAll()
                .anyRequest()        // 任何请求,登录后可以访问
                .authenticated()
                .and().csrf().disable();*/// 关闭csrf 跨站伪造防护


       /* http
                .requestMatchers().antMatchers(HttpMethod.OPTIONS, "kkSpringBoot2/oauth/token", "/rest/**", "/api/**", "/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()// 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/kkSpringBoot2/oauth/**").permitAll()// 定义哪些URL需要被保护、哪些不需要被保护
                .anyRequest()
                .antMatchers("/dbe/**").hasRole("admin1")
                .antMatchers("/dbe/**").hasRole("admin")
                // 任何请求,登录后可以访问
                .authenticated()
                .and()
                .cors()
                .and().csrf().disable();// 关闭csrf 跨站伪造防护*/
    }
}

/*
    一 URL匹配

        requestMatchers() 配置一个request Mather数组，参数为RequestMatcher 对象，其match 规则自定义,需要的时候放在最前面，对需要匹配的的规则进行自定义与过滤
        authorizeRequests() URL权限配置
        antMatchers() 配置一个request Mather 的 string数组，参数为 ant 路径格式， 直接匹配url
        anyRequest 匹配任意url，无参 ,最好放在最后面

    二 保护URL

        authenticated() 保护UrL，需要用户登录
        permitAll() 指定URL无需保护，一般应用与静态资源文件
        hasRole(String role) 限制单个角色访问，角色将被增加 “ROLE_” .所以”ADMIN” 将和 “ROLE_ADMIN”进行比较. 另一个方法是hasAuthority(String authority)
        hasAnyRole(String… roles) 允许多个角色访问. 另一个方法是hasAnyAuthority(String… authorities)
        access(String attribute) 该方法使用 SPEL, 所以可以创建复杂的限制 例如如access("permitAll"), access("hasRole('ADMIN') and hasIpAddress('123.123.123.123')")
        hasIpAddress(String ipaddressExpression) 限制IP地址或子网

    三 登录login

        formLogin() 基于表单登录
        loginPage() 登录页
        defaultSuccessUrl 登录成功后的默认处理页
        failuerHandler登录失败之后的处理器
        successHandler登录成功之后的处理器
        failuerUrl登录失败之后系统转向的url，默认是this.loginPage + "?error"

    四 登出logout

        logoutUrl 登出url ， 默认是/logout， 它可以是一个ant path url
        logoutSuccessUrl 登出成功后跳转的 url 默认是"/login?logout"
        logoutSuccessHandler 登出成功处理器，设置后会把logoutSuccessUrl 置为null


    https://www.jianshu.com/p/e3a9a8c4876c

 */
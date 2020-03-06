package com.kkIntegration.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication(scanBasePackages = {
        "com.kkIntegration",
        //"com.chu163.jieba"
},exclude = { // 此配置完全无效，原因未知
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})


// 这里 使用了自定义的多数据源，将不再这样子扫描mapper ,请查看  文档：Atomkos-多数据源+事务
/*
    mybatis 扫描包 ( 这是注意点 )
    将所有的 dao 接口添加
    如果没有 dao ，默认略过，不会报错
 */
/*@MapperScan(value = {
        "com.kkIntegration.ossd.dao", // OAuth2 - Security Mapper 接口
        "com.kkIntegration.rfe.dao", // 前端 mapper 接口
        "com.kkIntegration.rbe.dao", // 后台 mapper 接口

})*/

public class Application extends SpringBootServletInitializer {



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    /**
     * 解决前后端分离跨域问题
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}

/*
    Spring 学习 https://www.jianshu.com/p/29ee30689ca5
 */
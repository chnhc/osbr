package com.kkIntegration.mysql.mybatis;

import lombok.Data;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.BeanNameGenerator;

import java.lang.annotation.Annotation;

/**
 * description:
 * author: ckk
 * create: 2019-12-19 14:59
 */
@Data
public class MyScanner {


    private String[] value = new String[]{};

    private String[] basePackages = new String[]{};

    private Class<?>[] basePackageClasses = new Class[]{};

    private Class<? extends BeanNameGenerator> nameGenerator = BeanNameGenerator.class;

    private Class<? extends Annotation> annotationClass = Annotation.class;

    private Class<?> markerInterface = Class.class;

    private String sqlSessionTemplateRef =  "";

    private String sqlSessionFactoryRef =  "";

    private Class<? extends MapperFactoryBean> factoryBean = MapperFactoryBean.class;

}

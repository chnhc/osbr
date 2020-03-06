package com.kkIntegration.mysql.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 自定义扫描器
 * author: ckk
 * create: 2019-12-19 14:45
 */
public class MyMapperScannerRegistrar {

    public static void registerBeanDefinitions(BeanDefinitionRegistry registry ,  ResourceLoader resourceLoader, MyScanner myScanner) {


        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends Annotation> annotationClass = myScanner.getAnnotationClass();
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = myScanner.getMarkerInterface();
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = myScanner.getNameGenerator();
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = myScanner.getFactoryBean();
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setMapperFactoryBeanClass(mapperFactoryBeanClass);
        }

        scanner.setSqlSessionTemplateBeanName(myScanner.getSqlSessionTemplateRef());
        scanner.setSqlSessionFactoryBeanName(myScanner.getSqlSessionFactoryRef());

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : myScanner.getValue()) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : myScanner.getBasePackages()) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : myScanner.getBasePackageClasses()) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

}

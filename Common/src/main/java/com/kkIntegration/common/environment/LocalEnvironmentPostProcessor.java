package com.kkIntegration.common.environment;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 加载外部化配置 （https://docs.spring.io/spring-boot/docs/2.0.4.RELEASE/reference/htmlsingle/#howto-customize-the-environment-or-application-context）
 */
public class LocalEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        Resource path = new ClassPathResource("personal.yml");
        //System.out.println("加载自定义配置 ： "+path);
        PropertySource<?> propertySource = loadYaml(path);
        if(propertySource != null){
            environment.getPropertySources().addLast(propertySource);
        }
    }

    private PropertySource<?> loadYaml(Resource path) {
        if (!path.exists()) {
            return null;
        }
        try {
            return this.loader.load("custom-resource", path).get(0);
        }
        catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to load yaml configuration from " + path, ex);
        }
    }


}

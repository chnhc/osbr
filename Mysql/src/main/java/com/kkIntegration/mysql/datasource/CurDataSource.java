package com.kkIntegration.mysql.datasource;

import java.lang.annotation.*;

/**
 * description: 指定要使用的数据源
 * author: ckk
 * create: 2020-01-09 10:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurDataSource {
    String sourceName() default "";
}

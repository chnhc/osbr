package com.kkIntegration.processor.word.annotation;

import com.kkIntegration.processor.word.WordOneTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意只能用一次
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface WordOneAnnotation {

    // method
    String method() default "";

    // 子接口作用 -- 简短
    String title() default "";

    // 子接口说明 -- 详细说明
    String desc() default "";

    // 返回的实体
    Class<?> response() default String.class;

}

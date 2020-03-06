package com.kkIntegration.processor.word.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意只能用一次
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface WordParamAnnotation {


    // 默认值
    String defaultVal() default "";

    // 说明
    String desc() default "";

}

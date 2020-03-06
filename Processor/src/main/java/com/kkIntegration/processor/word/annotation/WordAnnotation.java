package com.kkIntegration.processor.word.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意，使用在接口上面
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface WordAnnotation {

    /**
     * 分类标签名，接口说明
     * @return
     */
    String title() ;

}

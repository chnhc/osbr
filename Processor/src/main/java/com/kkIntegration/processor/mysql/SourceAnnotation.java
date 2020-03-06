package com.kkIntegration.processor.mysql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意只能用一次
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface SourceAnnotation {
}

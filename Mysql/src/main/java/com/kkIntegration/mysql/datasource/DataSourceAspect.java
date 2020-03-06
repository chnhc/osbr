package com.kkIntegration.mysql.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zab
 * @date 2019/5/31 8:50
 */
@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

    @Pointcut("@annotation(com.kkIntegration.mysql.datasource.CurDataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        CurDataSource ds = method.getAnnotation(CurDataSource.class);
        if (ds != null) {
            DataSourceContextHolder.setDatabaseType(ds.sourceName());
        }

        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clearDataSource();

        }
    }


    @Override
    public int getOrder() {
        return 1;
    }
}

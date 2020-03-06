package com.kkIntegration.mysql.properties;


import lombok.Data;

/**
 * description:
 * author: ckk
 * create: 2019-12-19 13:14
 */
@Data
public class DataSourceEntity {


    String beanName;

    int maxPoolSize;

    int minPoolSize;

    int maxLifetime;

    int loginTimeout;

    int maxIdleTime;

    String testQuery;

    int maintenanceInterval;

    int borrowConnectionTimeout;

    String xaDataSourceClassName;

    String mapperPath;

    String daoPackages;

    XaProperties xaProperties;

}

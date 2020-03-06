package com.kkIntegration.mysql.properties;

import lombok.Data;

import java.util.List;
import java.util.Properties;

/**
 * description:
 * author: ckk
 * create: 2019-12-19 13:18
 */
@Data
public class XaProperties {

    String username;

    String password;

    List<SourcesUrlEntity> urls;

    String driverClassName;
    int initialSize;
    int minIdle;
    int maxActive;
    int maxWait;
    int timeBetweenEvictionRunsMillis;
    int minEvictableIdleTimeMillis;
    boolean testWhileIdle;
    boolean testOnBorrow;
    boolean testOnReturn;
    boolean defaultAutoCommit;
    boolean poolPreparedStatements;
    int maxPoolPreparedStatementPerConnectionSize;
    String filters;


    public Properties getAllXaProperties(int no){
        Properties xaProperties = new Properties();

        xaProperties.put("url", urls.get(no).getUrl());
        xaProperties.put("username", username);
        xaProperties.put("defaultAutoCommit", defaultAutoCommit);
        xaProperties.put("password", password);
        xaProperties.put("driverClassName", driverClassName);
        xaProperties.put("initialSize", initialSize);
        xaProperties.put("minIdle", minIdle);
        xaProperties.put("maxActive", maxActive);
        xaProperties.put("maxWait", maxWait);
        xaProperties.put("timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis);
        xaProperties.put("minEvictableIdleTimeMillis", minEvictableIdleTimeMillis);
        xaProperties.put("testWhileIdle", testWhileIdle);
        xaProperties.put("testOnBorrow", testOnBorrow);
        xaProperties.put("testOnReturn", testOnReturn);
        xaProperties.put("poolPreparedStatements", poolPreparedStatements);
        xaProperties.put("maxPoolPreparedStatementPerConnectionSize", maxPoolPreparedStatementPerConnectionSize);
        xaProperties.put("filters", filters);

        return xaProperties;
    }

}

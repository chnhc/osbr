package com.kkIntegration.mysql.properties;

import com.kkIntegration.processor.mysql.SourceAnnotation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2019-12-19 13:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kkincegration.jta.atomikos")
@SourceAnnotation
public class DataSourceProperties {

    private static String prefix = "kkincegration.jta.atomikos";

    // xa 类型
    private XAType xaType ;

    // 数据源配置
    private List<DataSourceEntity> datasourceList;

    // 手动构建 -- 加载环境中的配置
    public static DataSourceProperties loadEnvironment(OriginTrackedMapPropertySource propertySource){
        String ITEM_FORMAT = "kkincegration.jta.atomikos.datasourceList[%d]";

        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setXaType(XAType.valueOf((String) propertySource.getProperty(prefix+"."+"xaType")));
        List<DataSourceEntity> dataSourceEntities = new ArrayList<>();

        int i = 0;
        while ((propertySource.getProperty(prefix+"."+"datasourceList["+i+"].beanName") !=null)){
            DataSourceEntity dataSourceEntity = new DataSourceEntity();
            dataSourceEntity.setBeanName((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].beanName"));
            dataSourceEntity.setMapperPath((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].mapperPath"));
            dataSourceEntity.setDaoPackages((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].daoPackages"));
            dataSourceEntity.setMaxPoolSize((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].maxPoolSize"));
            dataSourceEntity.setMinPoolSize((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].minPoolSize"));
            dataSourceEntity.setMaxLifetime((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].maxLifetime"));
            dataSourceEntity.setBorrowConnectionTimeout((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].borrowConnectionTimeout"));
            dataSourceEntity.setXaDataSourceClassName((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaDataSourceClassName"));

            XaProperties xaProperties = new XaProperties();
            xaProperties.setUsername((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.username"));
            xaProperties.setPassword((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.password"));
            xaProperties.setDefaultAutoCommit((Boolean) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.defaultAutoCommit"));

            int u = 0;
            List<SourcesUrlEntity> sourcesUrlEntities = new ArrayList<>();
            while ((propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.urls["+u+"].url") !=null)){
                SourcesUrlEntity entity = new SourcesUrlEntity();
                entity.setUrl((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.urls["+u+"].url"));
                entity.setSourceName((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.urls["+u+"].sourceName"));
                sourcesUrlEntities.add(entity);
                u++;
            }
            xaProperties.setUrls(sourcesUrlEntities);

            xaProperties.setDriverClassName((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.driverClassName"));
            xaProperties.setTestWhileIdle((Boolean) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.testWhileIdle"));
            xaProperties.setTestOnBorrow((Boolean) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.testOnBorrow"));
            xaProperties.setTestOnReturn((Boolean) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.testOnReturn"));
            xaProperties.setPoolPreparedStatements((Boolean) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.poolPreparedStatements"));
            xaProperties.setFilters((String) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.filters"));

            xaProperties.setInitialSize((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.initialSize"));
            xaProperties.setMinIdle((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.minIdle"));
            xaProperties.setMaxActive((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.maxActive"));
            xaProperties.setMaxWait((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.maxWait"));
            xaProperties.setTimeBetweenEvictionRunsMillis((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.timeBetweenEvictionRunsMillis"));
            xaProperties.setMinEvictableIdleTimeMillis((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.minEvictableIdleTimeMillis"));
            xaProperties.setMaxPoolPreparedStatementPerConnectionSize((Integer) propertySource.getProperty(prefix+"."+"datasourceList["+i+"].xaProperties.maxPoolPreparedStatementPerConnectionSize"));

            dataSourceEntity.setXaProperties(xaProperties);

            dataSourceEntities.add(dataSourceEntity);
            i++;
        }

        dataSourceProperties.setDatasourceList(dataSourceEntities);
        return dataSourceProperties;
    }

}

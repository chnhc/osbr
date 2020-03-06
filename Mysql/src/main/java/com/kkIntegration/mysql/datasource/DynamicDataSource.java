package com.kkIntegration.mysql.datasource;

import com.kkIntegration.processor.mysql.SourceAnnotation;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * description: 动态数据源切换
 *              主要针对 同个表或者同个库， 同时配置多个数据源
 * author: ckk
 * create: 2019-12-19 12:20
 */
// 进行 xa 对应配置 文件生成
public class DynamicDataSource extends AbstractRoutingDataSource {

    private DataSourceContextHolder contextHolder;

    public DynamicDataSource(DataSourceContextHolder contextHolder){
        this.contextHolder = contextHolder;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.getLocalType();
    }


}


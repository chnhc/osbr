package com.kkIntegration.mysql.datasource;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// 将以轮询方式使用多数据源
public class DataSourceContextHolder {
    // Integer 是我们需要的
    private static final ThreadLocal<String> CONTEXT_HOLDER_DATASOURCE = new ThreadLocal<>();
    // 使用 Atomic 方便以后的动态改变
    private AtomicInteger dataSourceSize = new AtomicInteger();
    private Integer factoryType = null;
    private AtomicInteger currentDB = new AtomicInteger();
    private AtomicBoolean isMultiple = new AtomicBoolean();
    // 用于多数据源的切换名 -- 只做读取，不进行改操作
    // 后续需要动态切换数据等操作，可进行读写锁的操作
    private String[] sourceNames;

    // 初始设置数据
    public DataSourceContextHolder(String[] sourceNames, int dataSourceSize, Integer factoryType){
        this.factoryType = factoryType;
        // 配置了多少个数据源
        this.dataSourceSize.set(dataSourceSize);
        // 是否配置多个数据源
        if(dataSourceSize > 1){
            isMultiple.set(true);
        }
        // 设置当前数据源起始位置 0
        this.currentDB.set(0);
        // 多数据源名
        this.sourceNames = sourceNames;
    }

    // 获取类型
    public String getLocalType(){
        if( CONTEXT_HOLDER_DATASOURCE.get() == null || !isContain(CONTEXT_HOLDER_DATASOURCE.get())){
            CONTEXT_HOLDER_DATASOURCE.set(getDatabaseType());
        }
        return CONTEXT_HOLDER_DATASOURCE.get();
    }

    // 错误值，或者没有设置，则使用轮询方式
    private String getDatabaseType() {
        // 多个数据源
        if(isMultiple.get()){
            if(currentDB.get() == Integer.MAX_VALUE){
                currentDB.set(0);
            }
            // 取余方式轮询获取
            return sourceNames[currentDB.getAndIncrement() % dataSourceSize.get()];
        }else{
            // 单个数据源
            return sourceNames[0];
        }
    }

    // 设置当前线程的 数据源
    public static void setDatabaseType(String sourceName) {
        CONTEXT_HOLDER_DATASOURCE.set(sourceName);
    }

    // 清除
    public static void clearDataSource() {
        CONTEXT_HOLDER_DATASOURCE.remove();
    }

    // 判断是否存在
    private boolean isContain(String sourceName){
        for(String s : sourceNames){
            if(sourceName.equals(s)){
                return true;
            }
        }
        return false;
    }

}

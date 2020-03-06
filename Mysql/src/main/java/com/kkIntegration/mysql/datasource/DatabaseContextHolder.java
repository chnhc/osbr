package com.kkIntegration.mysql.datasource;

/**
 * 数据库动态切换，不是单个数据源
 * 现在项目未使用数据库切换
 * 只使用了动态切换数据源
 */
public class DatabaseContextHolder {
    // Integer 是我们需要的
    private static final ThreadLocal<Integer> CONTEXT_HOLDER = new ThreadLocal<>();
    //private static final Map<Object,DatabaseHolder> databaseMap = new HashMap<>();

    public static void setDatabaseType(int object) {
        //databaseMap.get(object).getDatabaseType()

        CONTEXT_HOLDER.set(object);
    }

    public static int getDatabaseType() {
        if(CONTEXT_HOLDER.get() == null){
            return 1;
        }
        return CONTEXT_HOLDER.get();
    }

}

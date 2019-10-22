package com.resto.shop.web.datasource;

public class DataSourceContextHolderReport {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>(); // 线程本地环境  
  
    // 设置数据源类型  
    public static void setDataSourceName(String dataSourceType) {  
        contextHolder.set(dataSourceType);  
    }  
  
    // 获取数据源类型  
    public static String getDataSourceName() {  
        return (String) contextHolder.get();  
    }  
  
    // 清除数据源类型  
    public static void clearDataSource() {  
        contextHolder.remove();  
    }  
  
}  

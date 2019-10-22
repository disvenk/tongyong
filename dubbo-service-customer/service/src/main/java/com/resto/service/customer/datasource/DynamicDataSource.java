package com.resto.service.customer.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.dubbo.config.annotation.Reference;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.service.DatabaseConfigService;
import com.resto.conf.mybatis.datasource.DataSourceContextHolder;
import com.resto.core.common.OrikaBeanMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DynamicDataSource
        extends AbstractRoutingDataSource implements InitializingBean {

    public static final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

    public DynamicDataSource() {
        this.setTargetDataSources(dataSourceMap);
    }

    @Autowired
    private OrikaBeanMapper mapper;

    @Reference
    DatabaseConfigService databaseConfigService;


    @Override
    protected Object determineCurrentLookupKey() {
        String dataConfigId = DataSourceContextHolder.getDataSourceName();
        if (!dataSourceMap.containsKey(dataConfigId)) {
            DatabaseConfig config = databaseConfigService.selectByBrandId(dataConfigId);
            DruidDataSource druidDataSource = mapper.map(config, DruidDataSource.class);
            dataSourceMap.put(dataConfigId, druidDataSource);
            super.setTargetDataSources(dataSourceMap);
            super.afterPropertiesSet();
        }
        return dataConfigId;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }
}

package com.resto.service.shop.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.resto.api.brand.define.api.BrandApi;
import com.resto.api.brand.dto.DatabaseConfigDto;
import com.resto.core.OrikaBeanMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource
        extends AbstractRoutingDataSource implements InitializingBean {

    public static final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

    public DynamicDataSource() {
        this.setTargetDataSources(dataSourceMap);
    }

    @Autowired
    private OrikaBeanMapper mapper;

    @Autowired
    private BrandApi brandApi;

    @Override
    protected Object determineCurrentLookupKey() {
        String dataconfigId = DataSourceContextHolder.getDataSourceName();
        if (!dataSourceMap.containsKey(dataconfigId)) {
            DatabaseConfigDto config = brandApi.getDBSettingByBrandId(dataconfigId);
            DruidDataSource druidDataSource = mapper.map(config, DruidDataSource.class);
            dataSourceMap.put(dataconfigId, druidDataSource);
            super.setTargetDataSources(dataSourceMap);
            super.afterPropertiesSet();
        }
        return dataconfigId;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }
}

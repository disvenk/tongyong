package com.resto.shop.web.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.service.DatabaseConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean {

	@Resource
	DatabaseConfigService databaseConfigService;

	public static final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

	public DynamicDataSource() {
		this.setTargetDataSources(dataSourceMap);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String dataconfigId = DataSourceContextHolder.getDataSourceName();
		if (!dataSourceMap.containsKey(dataconfigId)) {
			DatabaseConfig config = databaseConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
			DruidDataSource druidDataSource = new DruidDataSource();
			druidDataSource.setUrl(config.getUrl());
			druidDataSource.setUsername(config.getUsername());
			druidDataSource.setPassword(config.getPassword());
			druidDataSource.setDriverClassName(config.getDriverClassName());

			druidDataSource = setDataSource(druidDataSource);
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

	public static DruidDataSource setDataSource(DruidDataSource druidDataSource) {
		druidDataSource.setInitialSize(1);
		druidDataSource.setRemoveAbandoned(true);
		druidDataSource.setRemoveAbandonedTimeout(300);
		druidDataSource.setMinIdle(1);
		druidDataSource.setMaxActive(100);
		druidDataSource.setMaxWait(60000);
		druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
		druidDataSource.setMinEvictableIdleTimeMillis(300000);

		return druidDataSource;
	}
}

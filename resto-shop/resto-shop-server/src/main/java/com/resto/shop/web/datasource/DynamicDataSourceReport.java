package com.resto.shop.web.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.service.DatabaseConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSourceReport extends AbstractRoutingDataSource implements InitializingBean {

	@Resource
	DatabaseConfigService databaseConfigService;

	public static final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

	public DynamicDataSourceReport() {
		this.setTargetDataSources(dataSourceMap);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		String dataconfigId = DataSourceContextHolderReport.getDataSourceName();
		if (!dataSourceMap.containsKey(dataconfigId)) {
			DatabaseConfig config = databaseConfigService.selectByBrandId(DataSourceContextHolder.getDataSourceName());
			DruidDataSource druidDataSource = new DruidDataSource();
			String url = config.getUrl();
			String userName = config.getUsername();
			String passWord = config.getPassword();
			String prodUrl = "rds64fw2qrd8q0eg95nmo.mysql.rds.aliyuncs.com";
			//当前查询为线上数据库，切换查询数据源为只读数据源
			if (url.indexOf(prodUrl) != -1) {
				url = url.replace(prodUrl, "rr-uf68ruwd0571iwmf4o.mysql.rds.aliyuncs.com");
				userName = "viewer";
				passWord = "Vino2016";
			}
			druidDataSource.setUrl(url);
			druidDataSource.setUsername(userName);
			druidDataSource.setPassword(passWord);
			druidDataSource.setDriverClassName(config.getDriverClassName());

			druidDataSource = DynamicDataSource.setDataSource(druidDataSource);

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

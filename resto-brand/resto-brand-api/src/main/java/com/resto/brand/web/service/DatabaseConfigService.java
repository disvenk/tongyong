package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.DatabaseConfig;

import java.util.List;

public interface DatabaseConfigService extends GenericService<DatabaseConfig, String> {

	int insert(DatabaseConfig databaseConfig);

	int update(DatabaseConfig databaseConfig);

	DatabaseConfig selectByBrandId(String dataSourceName);

	DatabaseConfig selectByPrimaryKey(String databaseId);

	List<DatabaseConfig> selectList();
}

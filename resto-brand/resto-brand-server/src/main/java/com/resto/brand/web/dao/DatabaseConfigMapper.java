package com.resto.brand.web.dao;

import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.core.generic.GenericDao;

import java.util.List;

public interface DatabaseConfigMapper  extends GenericDao<DatabaseConfig,String> {
    int deleteByPrimaryKey(String id);

    int insert(DatabaseConfig record);

    int insertSelective(DatabaseConfig record);

    DatabaseConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DatabaseConfig record);

    int updateByPrimaryKey(DatabaseConfig record);

	DatabaseConfig selectByBrandId(String dataSourceName);

    List<DatabaseConfig> selectList();
}

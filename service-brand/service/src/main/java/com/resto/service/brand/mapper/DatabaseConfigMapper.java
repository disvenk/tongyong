package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.DatabaseConfig;
import java.util.List;

public interface DatabaseConfigMapper  extends BaseDao<DatabaseConfig,String> {
    int deleteByPrimaryKey(String id);

    int insert(DatabaseConfig record);

    int insertSelective(DatabaseConfig record);

    DatabaseConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DatabaseConfig record);

    int updateByPrimaryKey(DatabaseConfig record);

	DatabaseConfig selectByBrandId(String dataSourceName);

    List<DatabaseConfig> selectList();
}

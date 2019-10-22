package com.resto.service.brand.service;

import com.resto.api.brand.dto.DatabaseConfigDto;
import com.resto.conf.util.Encrypter;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.core.common.OrikaBeanMapper;
import com.resto.service.brand.entity.DatabaseConfig;
import com.resto.service.brand.mapper.DatabaseConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bruce on 2018-01-02 11:23
 */
@Service
public class DatabaseConfigService extends BaseService<DatabaseConfig,String> {

    @Autowired
    private DatabaseConfigMapper databaseConfigMapper;
    @Autowired
    private OrikaBeanMapper mapper;

    public BaseDao<DatabaseConfig, String> getDao() {
        return databaseConfigMapper;
    }

    public DatabaseConfigDto selectByBrandId(String dataSourceName) {
        DatabaseConfig databaseConfig = databaseConfigMapper.selectByBrandId(dataSourceName);
        //解密
        databaseConfig.setUsername(Encrypter.decrypt(databaseConfig.getUsername()));
        databaseConfig.setPassword(Encrypter.decrypt(databaseConfig.getPassword()));
        return mapper.map(databaseConfig,DatabaseConfigDto.class);
    }

    public DatabaseConfig selectByPrimaryKey(String databaseId) {
        DatabaseConfig databaseConfig = databaseConfigMapper.selectByPrimaryKey(databaseId);
        //解密
        databaseConfig.setUsername(Encrypter.decrypt(databaseConfig.getUsername()));
        databaseConfig.setPassword(Encrypter.decrypt(databaseConfig.getPassword()));
        return databaseConfig;
    }

}

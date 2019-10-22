package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.web.dao.DatabaseConfigMapper;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.service.DatabaseConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class DatabaseConfigServiceImpl extends GenericServiceImpl<DatabaseConfig, String> implements DatabaseConfigService {

    @Resource
    private DatabaseConfigMapper databaseconfigMapper;

    @Override
    public GenericDao<DatabaseConfig, String> getDao() {
        return databaseconfigMapper;
    }

	@Override
	public int insert(DatabaseConfig databaseConfig) {
		//加密
		databaseConfig.setUsername(Encrypter.encrypt(databaseConfig.getUsername()));
		databaseConfig.setPassword(Encrypter.encrypt(databaseConfig.getPassword()));
		return databaseconfigMapper.insertSelective(databaseConfig);
	}

	@Override
	public int update(DatabaseConfig databaseConfig) {
		DatabaseConfig dc = databaseconfigMapper.selectByPrimaryKey(databaseConfig.getId());
		//加密
		if(databaseConfig.getUsername().equals("就不告诉你")){
			databaseConfig.setUsername(dc.getUsername());
		}else{
			databaseConfig.setUsername(Encrypter.encrypt(databaseConfig.getUsername()));
		}
		if(databaseConfig.getPassword().equals("就不告诉你")){
			databaseConfig.setPassword(dc.getPassword());
		}else{
			databaseConfig.setPassword(Encrypter.encrypt(databaseConfig.getPassword()));
		}
		return databaseconfigMapper.updateByPrimaryKeySelective(databaseConfig);
	}

	@Override
	public DatabaseConfig selectByBrandId(String dataSourceName) {
		DatabaseConfig databaseConfig = databaseconfigMapper.selectByBrandId(dataSourceName);
		//解密
		databaseConfig.setUsername(Encrypter.decrypt(databaseConfig.getUsername()));
		databaseConfig.setPassword(Encrypter.decrypt(databaseConfig.getPassword()));
		return databaseConfig;
	}

	@Override
	public DatabaseConfig selectByPrimaryKey(String databaseId) {
		DatabaseConfig databaseConfig = databaseconfigMapper.selectByPrimaryKey(databaseId);
		//解密
		databaseConfig.setUsername(Encrypter.decrypt(databaseConfig.getUsername()));
		databaseConfig.setPassword(Encrypter.decrypt(databaseConfig.getPassword()));
		return databaseConfig;
	}

	@Override
	public List<DatabaseConfig> selectList() {
		List<DatabaseConfig> databaseConfigs = databaseconfigMapper.selectList();
		//解密
		for(int i = 0; i < databaseConfigs.size(); i++){
			DatabaseConfig databaseConfig = databaseConfigs.get(i);
			databaseConfig.setUsername(Encrypter.decrypt(databaseConfig.getUsername()));
			databaseConfig.setPassword(Encrypter.decrypt(databaseConfig.getPassword()));
		}
		return databaseConfigs;
	}




}

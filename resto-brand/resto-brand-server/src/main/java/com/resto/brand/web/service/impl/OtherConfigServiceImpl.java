package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.DataVailedException;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.OtherConfigMapper;
import com.resto.brand.web.model.OtherConfig;
import com.resto.brand.web.service.OtherConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;



/**
 *
 */
@Component
@Service
public class OtherConfigServiceImpl extends GenericServiceImpl<OtherConfig, Long> implements OtherConfigService {

    @Resource
    private OtherConfigMapper otherconfigMapper;

    @Override
    public GenericDao<OtherConfig, Long> getDao() {
        return otherconfigMapper;
    }

	@Override
	public void create(OtherConfig otherConfig) {
		checkOtherConfigNameExits(otherConfig);
		otherConfig.setCreateTime(new Date());
		otherconfigMapper.insert(otherConfig);
	} 
	

	@Override
	public OtherConfig selectOtherConfigName(String otherConfigName) {
		return otherconfigMapper.selectOtherConfigName(otherConfigName);
	}
    
	//判断 其他配置 名称 是否 已存在
	private void checkOtherConfigNameExits(OtherConfig otherConfig) {
		OtherConfig oldOtherConfig = selectOtherConfigName(otherConfig.getConfigName());
		if(oldOtherConfig!=null){
			throw new DataVailedException("配置名称已存在");
		}
	}

}

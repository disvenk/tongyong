package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.WechatConfigMapper;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.WechatConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class WechatConfigServiceImpl extends GenericServiceImpl<WechatConfig, String> implements WechatConfigService {

    @Resource
    private WechatConfigMapper wechatconfigMapper;

    @Override
    public GenericDao<WechatConfig, String> getDao() {
        return wechatconfigMapper;
    }

	@Override
	public WechatConfig selectByBrandId(String brandId) {
		return wechatconfigMapper.selectByBrandId(brandId);
	} 

}

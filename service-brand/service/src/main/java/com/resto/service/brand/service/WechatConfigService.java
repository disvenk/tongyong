package com.resto.service.brand.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.WechatConfig;
import com.resto.service.brand.mapper.WechatConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WechatConfigService extends BaseService<WechatConfig, String> {

    @Autowired
    private WechatConfigMapper wechatconfigMapper;

    public BaseDao<WechatConfig, String> getDao() {
        return wechatconfigMapper;
    }

	public WechatConfig selectByBrandId(String brandId) {
		return wechatconfigMapper.selectByBrandId(brandId);
	}

}

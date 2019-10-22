package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.WechatConfig;

public interface WechatConfigService extends GenericService<WechatConfig, String> {

	WechatConfig selectByBrandId(String brandId);
    
}

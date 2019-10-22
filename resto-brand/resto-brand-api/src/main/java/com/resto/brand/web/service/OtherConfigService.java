package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.OtherConfig;

public interface OtherConfigService extends GenericService<OtherConfig, Long> {
	/**
	 * 添加 其他配置信息
	 * @param otherConfig
	 */
    void create(OtherConfig otherConfig);
    
    OtherConfig selectOtherConfigName(String otherConfigName);
}

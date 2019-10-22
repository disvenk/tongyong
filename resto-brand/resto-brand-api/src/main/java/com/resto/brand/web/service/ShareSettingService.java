package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ShareSetting;

public interface ShareSettingService extends GenericService<ShareSetting, String> {

	List<ShareSetting> selectBrandShareSetting();

	ShareSetting selectByBrandId(String currentBrandId);

	void updateByBrandId(ShareSetting setting);

	/**
	 * 查找有效的分享设置条件：1 已经被管理员开启，2 已经被商家启用
	 * @param brandId
	 * @return
	 */
	ShareSetting selectValidSettingByBrandId(String brandId);
    
}

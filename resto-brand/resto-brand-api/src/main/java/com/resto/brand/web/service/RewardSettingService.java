package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.RewardSetting;

public interface RewardSettingService extends GenericService<RewardSetting, String> {

	void updateSetting(RewardSetting setting);

	RewardSetting selectByBrandId(String currentBrandId);

	RewardSetting selectValidSettingByBrandId(String dataSourceName);
    
}

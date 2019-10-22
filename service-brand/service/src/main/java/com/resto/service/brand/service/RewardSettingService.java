package com.resto.service.brand.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.RewardSetting;
import com.resto.service.brand.mapper.RewardSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RewardSettingService extends BaseService<RewardSetting, String> {

    @Autowired
    private RewardSettingMapper rewardsettingMapper;

    public BaseDao<RewardSetting, String> getDao() {
        return rewardsettingMapper;
    }

	public RewardSetting selectByBrandId(String brandId) {
		return rewardsettingMapper.selectByBrandId(brandId);
	}

}

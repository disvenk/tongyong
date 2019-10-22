package com.resto.brand.web.dao;

import com.resto.brand.web.model.RewardSetting;
import com.resto.brand.core.generic.GenericDao;

public interface RewardSettingMapper  extends GenericDao<RewardSetting,String> {
    int deleteByPrimaryKey(String id);

    int insert(RewardSetting record);

    int insertSelective(RewardSetting record);

    RewardSetting selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RewardSetting record);

    int updateByPrimaryKey(RewardSetting record);

	RewardSetting selectByBrandId(String brandId);
}

package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.RewardSetting;

public interface RewardSettingMapper  extends BaseDao<RewardSetting,String> {
    int deleteByPrimaryKey(String id);

    int insert(RewardSetting record);

    int insertSelective(RewardSetting record);

    RewardSetting selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RewardSetting record);

    int updateByPrimaryKey(RewardSetting record);

	RewardSetting selectByBrandId(String brandId);
}

package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.BonusSetting;
import org.apache.ibatis.annotations.Param;

public interface BonusSettingMapper extends BaseDao<BonusSetting,String> {
    int deleteByPrimaryKey(String id);

    int insert(BonusSetting record);

    int insertSelective(BonusSetting record);

    BonusSetting selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BonusSetting record);

    int updateByPrimaryKey(BonusSetting record);

    BonusSetting selectByChargeSettingId(@Param("chargeSettingId") String chargeSettingId);
}

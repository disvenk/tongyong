package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ChargeSetting;

import java.util.List;

public interface ChargeSettingMapper extends BaseDao<ChargeSetting,String> {
    int deleteByPrimaryKey(String id);

    int insert(ChargeSetting record);

    int insertSelective(ChargeSetting record);

    ChargeSetting selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChargeSetting record);

    int updateByPrimaryKey(ChargeSetting record);

    List<ChargeSetting> selectListByShopId();

    List<ChargeSetting> selectListByShopIdAll();

    List<ChargeSetting> selectListByShopIdAndType(int flag);
}

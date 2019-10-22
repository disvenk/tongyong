package com.resto.shop.web.dao;

import java.util.List;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ChargeSetting;

public interface ChargeSettingMapper  extends GenericDao<ChargeSetting,String> {
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

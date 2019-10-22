package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ChargeSetting;

public interface ChargeSettingService extends GenericService<ChargeSetting, String> {

    List<ChargeSetting> selectListByShopId();

    List<ChargeSetting> selectListByShopIdAll();

    List<ChargeSetting> selectListByShopIdAndType(int flag);
}

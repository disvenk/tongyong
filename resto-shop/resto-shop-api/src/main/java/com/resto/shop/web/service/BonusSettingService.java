package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.BonusSetting;

public interface BonusSettingService extends GenericService<BonusSetting, String> {

    BonusSetting selectByChargeSettingId(String chargeSettingId);
}

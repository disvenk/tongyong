package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.BonusSetting;
import com.resto.service.shop.mapper.BonusSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BonusSettingService extends BaseService<BonusSetting, String> {

    @Autowired
    private BonusSettingMapper bonussettingMapper;

    @Override
    public BaseDao<BonusSetting, String> getDao() {
        return bonussettingMapper;
    }

    public BonusSetting selectByChargeSettingId(String chargeSettingId) {
        return bonussettingMapper.selectByChargeSettingId(chargeSettingId);
    }
}

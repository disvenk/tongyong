package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.BonusSettingMapper;
import com.resto.shop.web.model.BonusSetting;
import com.resto.shop.web.service.BonusSettingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class BonusSettingServiceImpl extends GenericServiceImpl<BonusSetting, String> implements BonusSettingService {

    @Resource
    private BonusSettingMapper bonussettingMapper;

    @Override
    public GenericDao<BonusSetting, String> getDao() {
        return bonussettingMapper;
    }


    @Override
    public BonusSetting selectByChargeSettingId(String chargeSettingId) {
        return bonussettingMapper.selectByChargeSettingId(chargeSettingId);
    }
}

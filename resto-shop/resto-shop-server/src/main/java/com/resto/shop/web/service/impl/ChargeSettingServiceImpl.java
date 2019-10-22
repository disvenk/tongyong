package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ChargeSettingMapper;
import com.resto.shop.web.model.ChargeSetting;
import com.resto.shop.web.service.ChargeSettingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ChargeSettingServiceImpl extends GenericServiceImpl<ChargeSetting, String> implements ChargeSettingService {

    @Resource
    private ChargeSettingMapper chargesettingMapper;

    @Override
    public GenericDao<ChargeSetting, String> getDao() {
        return chargesettingMapper;
    }

    @Override
    public List<ChargeSetting> selectListByShopId() {
        return chargesettingMapper.selectListByShopId();
    }


    @Override
    public List<ChargeSetting> selectListByShopIdAll() {
        return chargesettingMapper.selectListByShopIdAll();
    }

    @Override
    public List<ChargeSetting> selectListByShopIdAndType(int flag) {
        return chargesettingMapper.selectListByShopIdAndType(flag);
    }
}

package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.ChargeSetting;
import com.resto.service.shop.mapper.ChargeSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeSettingService extends BaseService<ChargeSetting, String> {

    @Autowired
    private ChargeSettingMapper chargesettingMapper;

    @Override
    public BaseDao<ChargeSetting, String> getDao() {
        return chargesettingMapper;
    }

    public List<ChargeSetting> selectListByShopId() {
        return chargesettingMapper.selectListByShopId();
    }


    public List<ChargeSetting> selectListByShopIdAll() {
        return chargesettingMapper.selectListByShopIdAll();
    }

    public List<ChargeSetting> selectListByShopIdAndType(int flag) {
        return chargesettingMapper.selectListByShopIdAndType(flag);
    }
}

package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.WetherMapper;
import com.resto.brand.web.model.Wether;
import com.resto.brand.web.service.WetherService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class WetherServiceImpl extends GenericServiceImpl<Wether, Integer> implements WetherService {

    @Resource
    private WetherMapper wetherMapper;

    @Override
    public GenericDao<Wether, Integer> getDao() {
        return wetherMapper;
    }

    @Override
    public Wether selectDateAndShopId(String shopId, String date) {
        return wetherMapper.selectDateAndShopId(shopId,date);
    }
}

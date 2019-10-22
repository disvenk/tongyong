package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ShopModeMapper;
import com.resto.brand.web.model.ShopMode;
import com.resto.brand.web.service.ShopModeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class ShopModeServiceImpl extends GenericServiceImpl<ShopMode, Integer> implements ShopModeService {

    @Resource
    private ShopModeMapper shopmodeMapper;

    @Override
    public GenericDao<ShopMode, Integer> getDao() {
        return shopmodeMapper;
    }

}

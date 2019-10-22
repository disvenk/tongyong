package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ShopTvConfigMapper;
import com.resto.brand.web.model.ShopTvConfig;
import com.resto.brand.web.service.ShopTvConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by carl on 2017/7/14.
 */
@Component
@Service
public class ShopTvConfigServiceImpl extends GenericServiceImpl<ShopTvConfig, Long> implements ShopTvConfigService {

    @Resource
    private ShopTvConfigMapper shopTvConfigMapper;

    @Override
    public GenericDao<ShopTvConfig, Long> getDao() {
        return shopTvConfigMapper;
    }

    @Override
    public ShopTvConfig selectByShopId(String shopId) {
        return shopTvConfigMapper.selectByShopId(shopId);
    }
}

package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.WeShopInfoMapper;
import com.resto.shop.web.model.WeShopInfo;
import com.resto.shop.web.service.WeShopInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class WeShopInfoServiceImpl extends GenericServiceImpl<WeShopInfo, Integer> implements WeShopInfoService {

    @Resource
    private WeShopInfoMapper weshopinfoMapper;

    @Override
    public GenericDao<WeShopInfo, Integer> getDao() {
        return weshopinfoMapper;
    } 

}

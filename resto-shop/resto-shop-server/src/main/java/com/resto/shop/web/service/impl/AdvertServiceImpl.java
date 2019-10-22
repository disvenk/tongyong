package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.AdvertMapper;
import com.resto.shop.web.model.Advert;
import com.resto.shop.web.service.AdvertService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class AdvertServiceImpl extends GenericServiceImpl<Advert, Integer> implements AdvertService {

    @Resource
    private AdvertMapper advertMapper;

    @Override
    public GenericDao<Advert, Integer> getDao() {
        return advertMapper;
    }

	@Override
	public List<Advert> selectListByShopId(String shopId) {
        return advertMapper.selectListByShopId(shopId);
	}


}

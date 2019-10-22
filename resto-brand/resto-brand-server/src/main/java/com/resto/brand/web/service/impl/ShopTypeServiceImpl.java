package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ShopTypeMapper;
import com.resto.brand.web.model.ShopType;
import com.resto.brand.web.service.ShopTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by carl on 2018/04/2018/4/4
 */
@Component
@Service
public class ShopTypeServiceImpl extends GenericServiceImpl<ShopType, Integer> implements ShopTypeService {


    @Resource
    private ShopTypeMapper shopTypeMapper;

    @Override
    public GenericDao<ShopType, Integer> getDao() {
        return shopTypeMapper;
    }

}

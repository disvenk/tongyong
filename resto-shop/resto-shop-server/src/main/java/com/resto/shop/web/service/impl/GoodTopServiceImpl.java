package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.GoodTopMapper;
import com.resto.shop.web.model.GoodTop;
import com.resto.shop.web.service.GoodTopService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 */
@Component
@Service
public class GoodTopServiceImpl extends GenericServiceImpl<GoodTop, Long> implements GoodTopService {

    @Resource
    private GoodTopMapper goodtopMapper;

    @Override
    public GenericDao<GoodTop, Long> getDao() {
        return goodtopMapper;
    }

    @Override
    public int deleteByTodayAndShopId(String brandId, String shopId, int dayMessage, Date date) {
       return goodtopMapper.deleteByTodayAndShopId(brandId,shopId,dayMessage,date);




    }
}

package com.resto.shop.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.SvipChargeItemMapper;
import com.resto.shop.web.model.SvipChargeItem;
import com.resto.shop.web.service.SvipChargeItemService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Service
@Component
public class SvipChargeItemServiceImpl extends GenericServiceImpl<SvipChargeItem, String> implements SvipChargeItemService {

    @Resource
    private SvipChargeItemMapper svipchargeitemMapper;

    @Override
    public GenericDao<SvipChargeItem, String> getDao() {
        return svipchargeitemMapper;
    } 

}

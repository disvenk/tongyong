package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.BadTopMapper;
import com.resto.shop.web.model.BadTop;
import com.resto.shop.web.service.BadTopService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class BadTopServiceImpl extends GenericServiceImpl<BadTop, Long> implements BadTopService {

    @Resource
    private BadTopMapper badtopMapper;

    @Override
    public GenericDao<BadTop, Long> getDao() {
        return badtopMapper;
    } 

}

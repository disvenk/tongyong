package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.OtherConfigDetailedMapper;
import com.resto.brand.web.model.OtherConfigDetailed;
import com.resto.brand.web.service.OtherConfigDetailedService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class OtherConfigDetailedServiceImpl extends GenericServiceImpl<OtherConfigDetailed, Long> implements OtherConfigDetailedService {

    @Resource
    private OtherConfigDetailedMapper otherconfigdetailedMapper;

    @Override
    public GenericDao<OtherConfigDetailed, Long> getDao() {
        return otherconfigdetailedMapper;
    } 

}

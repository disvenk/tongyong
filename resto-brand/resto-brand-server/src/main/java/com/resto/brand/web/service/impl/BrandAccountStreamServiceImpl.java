package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.BrandAccountStreamMapper;
import com.resto.brand.web.model.BrandAccountStream;
import com.resto.brand.web.service.BrandAccountStreamService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class BrandAccountStreamServiceImpl extends GenericServiceImpl<BrandAccountStream, Long> implements BrandAccountStreamService {

    @Resource
    private BrandAccountStreamMapper brandaccountstreamMapper;

    @Override
    public GenericDao<BrandAccountStream, Long> getDao() {
        return brandaccountstreamMapper;
    } 

}

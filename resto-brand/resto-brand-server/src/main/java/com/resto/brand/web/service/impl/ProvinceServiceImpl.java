package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ProvinceMapper;
import com.resto.brand.web.model.Province;
import com.resto.brand.web.service.ProvinceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class ProvinceServiceImpl extends GenericServiceImpl<Province, Integer> implements ProvinceService {

    @Resource
    private ProvinceMapper provinceMapper;

    @Override
    public GenericDao<Province, Integer> getDao() {
        return provinceMapper;
    }


}

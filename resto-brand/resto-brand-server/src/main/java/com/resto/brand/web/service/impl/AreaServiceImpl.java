package com.resto.brand.web.service.impl;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.AreaMapper;
import com.resto.brand.web.model.Area;
import com.resto.brand.web.service.AreaService;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Service
public class AreaServiceImpl extends GenericServiceImpl<Area, Integer> implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public GenericDao<Area, Integer> getDao() {
        return areaMapper;
    } 

}

package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.CityMapper;
import com.resto.brand.web.model.City;
import com.resto.brand.web.service.CityService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class CityServiceImpl extends GenericServiceImpl<City, Integer> implements CityService {

    @Resource
    private CityMapper cityMapper;

    @Override
    public GenericDao<City, Integer> getDao() {
        return cityMapper;
    }


    @Override
    public List<City> selectByProvinceId(Integer provinceId) {
        return cityMapper.selectByProvinceId(provinceId);
    }

}

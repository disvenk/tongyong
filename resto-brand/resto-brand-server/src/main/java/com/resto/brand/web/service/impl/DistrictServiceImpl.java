package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.DistrictMapper;
import com.resto.brand.web.model.District;
import com.resto.brand.web.service.DistrictService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, Integer> implements DistrictService {

    @Resource
    private DistrictMapper districtMapper;

    @Override
    public GenericDao<District, Integer> getDao() {
        return districtMapper;
    }


    @Override
    public List<District> selectByCityId(Integer cityId) {
        return districtMapper.selectByCityId(cityId);
    }


}

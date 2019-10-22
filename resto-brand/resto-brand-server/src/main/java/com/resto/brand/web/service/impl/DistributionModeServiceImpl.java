package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.DistributionModeMapper;
import com.resto.brand.web.model.DistributionMode;
import com.resto.brand.web.service.DistributionModeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class DistributionModeServiceImpl extends GenericServiceImpl<DistributionMode, Integer> implements DistributionModeService {

    @Resource
    private DistributionModeMapper distributionmodeMapper;

    @Override
    public GenericDao<DistributionMode, Integer> getDao() {
        return distributionmodeMapper;
    } 

}

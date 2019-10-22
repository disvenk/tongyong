package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.DistributionTimeMapper;
import com.resto.shop.web.model.DistributionTime;
import com.resto.shop.web.service.DistributionTimeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class DistributionTimeServiceImpl extends GenericServiceImpl<DistributionTime, Integer> implements DistributionTimeService {

    @Resource
    private DistributionTimeMapper distributiontimeMapper;

    @Override
    public GenericDao<DistributionTime, Integer> getDao() {
        return distributiontimeMapper;
    }

    @Override
    public List<DistributionTime> selectListByShopId(String currentShopId) {
        return distributiontimeMapper.selectListByShopId(currentShopId);
    } 

}

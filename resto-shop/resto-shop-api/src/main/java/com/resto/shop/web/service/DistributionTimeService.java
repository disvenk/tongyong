package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.DistributionTime;

public interface DistributionTimeService extends GenericService<DistributionTime, Integer> {

    List<DistributionTime> selectListByShopId(String currentShopId);
    
}

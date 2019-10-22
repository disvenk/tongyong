package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.DeliveryPoint;

public interface DeliveryPointService extends GenericService<DeliveryPoint, Integer> {

    List<DeliveryPoint> selectListById(String currentShopId);
    
}

package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeOrderDetail;


public interface WeOrderDetailService extends GenericService<WeOrderDetail, Integer> {
    WeOrderDetail  selectWeOrderByShopIdAndTime(String shopId,String createTime);

    void insertDaydata(String currentBrandId,String brandName);

    void deleteYesterDayData(String currentBrandId);
}

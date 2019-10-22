package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.GoodTop;

import java.util.Date;

public interface GoodTopService extends GenericService<GoodTop, Long> {


    int deleteByTodayAndShopId(String brandId, String shopId, int dayMessage,Date date);



}

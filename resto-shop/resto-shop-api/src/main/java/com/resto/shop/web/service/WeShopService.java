package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.WeShop;

import java.util.List;

public interface WeShopService extends GenericService<WeShop, Integer> {

     List<WeShop> selectWeShopListByBrandIdAndTime(String brandId,String createTime);

     WeShop selectWeShopByShopIdAndTime(String shopId,String createTime);
}

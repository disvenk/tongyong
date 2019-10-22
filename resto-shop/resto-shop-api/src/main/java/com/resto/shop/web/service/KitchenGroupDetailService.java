package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.KitchenGroupDetail;

import java.util.List;

public interface KitchenGroupDetailService extends GenericService<KitchenGroupDetail, Integer> {

    List<KitchenGroupDetail> selectKitchenGroupDetailByShopId(String shopDetailId);
}

package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.KitchenGroup;

import java.util.List;

public interface KitchenGroupService extends GenericService<KitchenGroup, Integer> {

    List<KitchenGroup> selectKitchenGroupByShopDetailId(String shopDetailId);

    List<KitchenGroup> selectKitchenGroupByShopDetailId(String brandId,String shopDetailId);

    List<KitchenGroup> selectKitchenGroupByShopDetailIdByStatus(String shopDetailId);

     void insertSelective(KitchenGroup kitchenGroup);

     void insertKitchenGroupDetail(KitchenGroup kitchenGroup);

     int delete(String shopDetailId,Integer kitchenGroupId);
}

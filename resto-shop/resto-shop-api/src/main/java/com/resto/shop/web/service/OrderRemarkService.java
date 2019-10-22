package com.resto.shop.web.service;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.OrderRemark;

import java.util.List;

public interface OrderRemarkService extends GenericService<OrderRemark, String>{

    List<OrderRemark> selectOrderRemarkByShopId(String shopId);

    List<JSONObject> getShopOrderRemark(String shopId);

    void deleteByBoOrderRemarkId(String boOrderRemarkId);

    /**
     * 根据 店铺ID 查询店铺下的所有  OrderRemark  数据
     * Pos2.0 数据拉取接口			By___lmx
     * @param shopId
     * @return
     */
    List<OrderRemark> selectOrderRemarkListByShopId(String shopId);
}

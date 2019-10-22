package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.PlatformOrderDetail;

import java.util.List;

public interface PlatformOrderDetailService extends GenericService<PlatformOrderDetail, String> {
    /**
     *  根据第三方平台的订单ID查询订单详情
     * @param platformOrderId
     * @return
     */
    List<PlatformOrderDetail> selectByPlatformOrderId(String platformOrderId);

    void meituanOrderDetail(String orderId,String order);
}

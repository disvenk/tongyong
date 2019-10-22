package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.OrderBefore;
import com.resto.shop.web.model.OrderItem;

import java.util.List;

/**
 * Created by KONATA on 2017/11/1.
 */
public interface OrderBeforeService extends GenericService<OrderBefore, Long> {

    OrderBefore getOrderNoPay(String tableNumber, String shopId,String customerId);

    List<OrderItem> getOrderBefore(String tableNumber,String shopId,String customerId);

    void updateState(String orderId,Integer state);

}

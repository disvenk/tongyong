package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.model.*;

/**
 * Created by carl on 2016/11/14.
 */
public interface LogBaseService extends GenericService<LogBase, String> {

    void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, Order order, Integer type);

    void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, Article article, Integer type, Integer number);

    void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, String desc, Integer type);

    void insertLogBaseInfoState(ShopDetail shopDetail, Customer customer, ChargeOrder chargeOrder, Integer type);
}

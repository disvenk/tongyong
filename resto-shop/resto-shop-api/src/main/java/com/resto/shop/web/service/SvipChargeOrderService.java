package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.model.ChargeOrder;
import com.resto.shop.web.model.SvipChargeItem;
import com.resto.shop.web.model.SvipChargeOrder;

public interface SvipChargeOrderService extends GenericService<SvipChargeOrder, String> {
    public SvipChargeOrder createChargeOrder(String settingId, String customerId, String shopId, String brandId);

    public void chargeorderWxPaySuccess(SvipChargeItem svipChargeItem);
    
}

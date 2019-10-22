package com.resto.shop.web.service;

import java.math.BigDecimal;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.model.ChargeLog;

public interface ChargeLogService extends GenericService<ChargeLog, String>{

	void insertChargeLogService(String operationPhone, String customerPhone, BigDecimal chargeMoney, ShopDetail shopDetail, String chargeOrderId);
}

package com.resto.shop.web.service;

import java.util.List;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.RechargeLogDto;
import com.resto.shop.web.model.ChargePayment;

public interface ChargePaymentService extends GenericService<ChargePayment, String> {
	
	List<ChargePayment> selectPayList(String beginDate,String endDate);
	
	RechargeLogDto selectRechargeLog(String beginDate,String endDate,String brandId);
    

	RechargeLogDto selectShopRechargeLog(String beginDate,String endDate,String shopId);

    ChargePayment selectPayData(String shopId);

    ChargePayment selectByChargeOrderId(String chargeOrderId);
}

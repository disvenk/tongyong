package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.dao.ChargeLogMapper;
import com.resto.shop.web.model.ChargeLog;
import com.resto.shop.web.service.ChargeLogService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Component
@Service
public class ChargeLogServiceImpl extends GenericServiceImpl<ChargeLog, String> implements ChargeLogService{

	@Resource
	private ChargeLogMapper chargeLogMapper;
	
	@Override
	public GenericDao<ChargeLog, String> getDao() {
		return chargeLogMapper;
	}

	@Override
	public void insertChargeLogService(String operationPhone, String customerPhone, BigDecimal chargeMoney, ShopDetail shopDetail, String chargeOrderId) {
		ChargeLog chargeLog = new ChargeLog();
		chargeLog.setId(ApplicationUtils.randomUUID());
		chargeLog.setOperationPhone(operationPhone);
		chargeLog.setCustomerPhone(customerPhone);
		chargeLog.setChargeMoney(chargeMoney);
		chargeLog.setShopDetailId(shopDetail.getId());
		chargeLog.setShopName(shopDetail.getName());
		chargeLog.setCreateTime(new Date());
		chargeLog.setChargeOrderId(chargeOrderId);
		chargeLogMapper.insertChargeLogService(chargeLog);
	}
}

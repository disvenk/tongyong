package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.ChargeLog;

public interface ChargeLogMapper extends GenericDao<ChargeLog, String>{

	void insertChargeLogService(ChargeLog chargeLog);
}

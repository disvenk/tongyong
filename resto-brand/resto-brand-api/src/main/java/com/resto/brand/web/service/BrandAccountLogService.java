package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandAccountLog;

import java.math.BigDecimal;
import java.util.List;

public interface BrandAccountLogService extends GenericService<BrandAccountLog, Long> {

	List<BrandAccountLog> selectListByBrandId(String brandId);

	/**
	 * 更新账户余额并记下日志
	 * @param brandAccountLog
	 * @param accountId
	 * @param jifeiMoney
	 * @return
	 */
	void updateBrandAccountAndLog(BrandAccountLog brandAccountLog,Integer accountId, BigDecimal jifeiMoney);

	List<BrandAccountLog> selectListByBrandIdAndTime(String beginDate,String endDate,String brandId);

	BrandAccountLog selectOneBySerialNumAndBrandId(String serialNumber,String brandId);

}

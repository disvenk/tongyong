package com.resto.shop.web.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.dto.RechargeLogDto;
import com.resto.shop.web.model.ChargePayment;

public interface ChargePaymentMapper  extends GenericDao<ChargePayment,String> {
    int deleteByPrimaryKey(String id);

    int insert(ChargePayment record);

    int insertSelective(ChargePayment record);

    ChargePayment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChargePayment record);

    int updateByPrimaryKeyWithBLOBs(ChargePayment record);

    int updateByPrimaryKey(ChargePayment record);

	RechargeLogDto selectRechargeLog(@Param("begin")Date begin,@Param("end")Date end,@Param("brandId")String brandId);
	
	RechargeLogDto selectShopRechargeLog(@Param("begin")Date begin,@Param("end")Date end,@Param("shopId")String shopId);

    ChargePayment selectPayData(@Param("shopId") String shopId);

    ChargePayment selectByChargeOrderId(String chargeOrderId);
}

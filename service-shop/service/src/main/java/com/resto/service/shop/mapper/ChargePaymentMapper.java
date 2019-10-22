package com.resto.service.shop.mapper;

import com.resto.api.brand.dto.RechargeLogDto;
import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ChargePayment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ChargePaymentMapper extends BaseDao<ChargePayment,String> {
    int deleteByPrimaryKey(String id);

    int insert(ChargePayment record);

    int insertSelective(ChargePayment record);

    ChargePayment selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChargePayment record);

    int updateByPrimaryKeyWithBLOBs(ChargePayment record);

    int updateByPrimaryKey(ChargePayment record);

	List<ChargePayment> selectPayList(@Param("begin") Date begin, @Param("end") Date end);

	RechargeLogDto selectRechargeLog(@Param("begin") Date begin, @Param("end") Date end, @Param("brandId") String brandId);

	RechargeLogDto selectShopRechargeLog(@Param("begin") Date begin, @Param("end") Date end, @Param("shopId") String shopId);

    ChargePayment selectPayData(@Param("shopId") String shopId);

    ChargePayment selectByChargeOrderId(String chargeOrderId);
}

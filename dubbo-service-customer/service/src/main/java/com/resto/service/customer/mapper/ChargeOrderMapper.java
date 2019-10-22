package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.ChargeOrder;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ChargeOrderMapper extends MyMapper<ChargeOrder> {
    void refundCharge(BigDecimal payValue, String id);
    void refundMoney(BigDecimal charge,BigDecimal reward, String id);
    void refundReward(BigDecimal payValue, String id);
    List<ChargeOrder> selectByDateAndShopId(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("shopId") String shopId);
    List<ChargeOrder> selectByDateAndBrandId(@Param("beginDate") Date begin, @Param("endDate") Date end,@Param("brandId") String brandId);
    List<Map<String, Object>> selectByShopToDay(Map<String, Object> selectMap);
    List<ChargeOrder> selectListByDateAndShopId(@Param("beginDate") Date begin, @Param("endDate") Date end,@Param("shopId") String id);
    List<ChargeOrder> selectByCustomerIdAndBrandId(@Param("customerId") String customerId, @Param("brandId") String brandId);
    List<Map<String, Object>> getChargeSumInfo(Map<String, Object> selectMap);
    List<String> selectCustomerChargeOrder(List<String> customerIds);
    BigDecimal selectTotalBalanceByTimeAndShopId(@Param("monthBegin") Date monthBegin, @Param("monthEnd") Date monthEnd,@Param("shopId") String shopId);
    List<ChargeOrder> selectRemainderReturn();
    List<ChargeOrder> selectByCustomerIdNotChangeIdList(String customerId);
    BigDecimal selectByCustomerIdNotChangeId(String customerId);
}
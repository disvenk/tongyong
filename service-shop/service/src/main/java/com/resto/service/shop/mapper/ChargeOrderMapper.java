package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.ChargeOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ChargeOrderMapper extends BaseDao<ChargeOrder,String> {

    int insert(ChargeOrder record);

    int updateByPrimaryKey(ChargeOrder record);

    ChargeOrder selectFirstBalanceOrder(String customerId);

    void updateBalance(@Param("id")String id, @Param("useCharge")BigDecimal useCharge, @Param("useReward")BigDecimal useReward);

    BigDecimal selectByCustomerIdNotChangeId(String customerId);

    List<ChargeOrder> selectByCustomerIdNotChangeIdList(String customerId);

    BigDecimal selectTotalBalance(String customerId);

    void refundCharge(BigDecimal payValue, String id);

    void refundMoney(BigDecimal charge,BigDecimal reward, String id);

    void refundReward(BigDecimal payValue, String id);

    List<ChargeOrder> selectByCustomerIdAndBrandId(@Param("customerId") String customerId, @Param("brandId") String brandId);

}

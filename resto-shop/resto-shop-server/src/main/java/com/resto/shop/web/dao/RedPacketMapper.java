package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.dto.ShareMoneyDto;
import com.resto.shop.web.model.RedPacket;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedPacketMapper extends GenericDao<RedPacket,String> {

    RedPacket selectFirstRedPacket(@Param("customerId") String customerId,@Param("redType") Integer redType);

    void updateRedRemainderMoney(Map<String, Object> param);

    /**
     * 取消订单返还红包
     * @param payValue
     * @param Id
     */
    void refundRedPacket(@Param("payValue")BigDecimal payValue, @Param("Id") String Id);

    List<ShareMoneyDto> selectShareMoneyList(@Param(value = "customerId")String customerId,@Param(value = "currentPage") Integer currentPage,@Param(value = "showCount") Integer showCount);

    List<RedPacket> selectByStateZero();
}

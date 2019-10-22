package com.resto.service.shop.mapper;

import com.resto.api.brand.dto.RedPacketDto;
import com.resto.api.shop.dto.ShareMoneyDto;
import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.RedPacket;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedPacketMapper extends BaseDao<RedPacket,String> {

    RedPacket selectFirstRedPacket(@Param("customerId") String customerId, @Param("redType") Integer redType);

    void updateRedRemainderMoney(Map<String, Object> param);

    List<RedPacketDto> selectRedPacketLog(Map<String, Object> selectMap);

    Map<String, Object> selectUseRedOrder(Map<String, Object> selectMap);

    /**
     * 取消订单返还红包
     * @param payValue
     * @param Id
     */
    void refundRedPacket(@Param("payValue") BigDecimal payValue, @Param("Id") String Id);

    List<ShareMoneyDto> selectShareMoneyList(@Param(value = "customerId") String customerId, @Param(value = "currentPage") Integer currentPage, @Param(value = "showCount") Integer showCount);
}

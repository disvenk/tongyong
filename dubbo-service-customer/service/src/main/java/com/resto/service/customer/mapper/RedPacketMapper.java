package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.RedPacket;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface RedPacketMapper extends MyMapper<RedPacket> {
    void refundRedPacket(@Param("payValue")BigDecimal payValue, @Param("Id") String Id);
}
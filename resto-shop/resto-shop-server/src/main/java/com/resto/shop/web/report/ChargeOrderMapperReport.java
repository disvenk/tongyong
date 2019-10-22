package com.resto.shop.web.report;

import com.resto.brand.web.dto.ChargeTotalDto;
import com.resto.brand.web.dto.RechargeLogDto;
import com.resto.brand.web.dto.RedPacketDto;
import com.resto.brand.web.dto.WeChatBill;
import com.resto.shop.web.model.ChargeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ChargeOrderMapperReport{

    List<ChargeOrder>  shopChargeCodes(@Param("shopDetailId")String shopDetailId,@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

    RechargeLogDto selectRechargeLog(@Param("begin")Date begin, @Param("end")Date end, @Param("brandId")String brandId);

    RechargeLogDto selectShopRechargeLog(@Param("begin")Date begin,@Param("end")Date end,@Param("shopId")String shopId);

    List<ChargeOrder> selectMonthDto(Map<String, Object> selectMap);

    List<RedPacketDto> selectChargeRedPacket(Map<String, Object> selectMap);

    List<WeChatBill> selectChargeWeChatBill(@Param("beginDate")String beginDate,@Param("endDate")String endDate,@Param("shopId")String shopId);

    ChargeTotalDto selectChargeTotal();
}

package com.resto.shop.web.report;


import com.resto.brand.web.dto.CouponDto;
import com.resto.brand.web.dto.CouponInfoDto;
import com.resto.brand.web.dto.UseCouponOrderDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CouponMapperReport{

    List<CouponDto> selectCouponDto(Map<String, Object> selectMap);

    List<CouponInfoDto> selectCouponInfoList(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("newCustomCouponId") String newCustomCouponId);

    List<UseCouponOrderDto> selectUseCouponOrders(@Param("couponIds") List<String> couponIds, @Param("isNewCustomId") Integer isNewCustomId, @Param("useBeginDate") String useBeginDate, @Param("useEndDate") String useEndDate);
}

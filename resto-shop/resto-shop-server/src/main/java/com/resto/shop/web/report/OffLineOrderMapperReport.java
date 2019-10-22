package com.resto.shop.web.report;

import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.model.OffLineOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OffLineOrderMapperReport{

    List<OrderNumDto> selectOrderNumByTimeAndBrandId(@Param("brandId") String brandId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 查询当月下的所有线下pos订单
     * @param shopId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OffLineOrder> selectlistByTimeSourceAndShopId(@Param("shopId") String shopId,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate,@Param("source") Integer source);

}

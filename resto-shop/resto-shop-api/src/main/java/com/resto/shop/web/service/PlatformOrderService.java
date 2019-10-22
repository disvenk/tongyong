package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.MeiTuanOrderDto;
import com.resto.brand.web.dto.PlatformReportDto;
import com.resto.shop.web.model.PlatformOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PlatformOrderService extends GenericService<PlatformOrder, String> {
    /**
     *  根据第三方平台的订单ID查询订单详情
     * @param platformOrderId
     * @return
     */
    PlatformOrder selectByPlatformOrderId(String platformOrderId,Integer type);



    /**
     * 美团外卖新订单
     * @param orderDto
     * @return
     */
    void meituanNewOrder(MeiTuanOrderDto orderDto);

    /**
     * 根据店铺id,开始日期和结束日期查询第三方外卖统计
     * @param beginDate
     * @param endDate
     * @param shopDetailId
     * @return
     */
    PlatformReportDto selectByshopDetailId(String beginDate, String endDate, String shopDetailId);

    /**
     * 根据店铺id,开始日期和结束日期查询第三方外卖统计(调用存储过程)
     * @param beginDate
     * @param endDate
     * @param shopDetailId
     * @return
     */
    PlatformReportDto proc_shopdetailId(String beginDate, String endDate, String shopDetailId);

    /**
     * 根据店铺id,开始日期和结束日期查询店铺外卖订单详情
     * @param beginDate
     * @param endDate
     * @param shopDetailId
     * @return
     */
    List<PlatformOrder> selectshopDetailIdList(String beginDate, String endDate, String shopDetailId);

    /**
     * 根据三方品台订单编号,查询该订单具体信息
     * @param platformOrderId
     * @return
     */
    List<PlatformOrder> getPlatformOrderDetailList(String platformOrderId);

    /**
     * 异常打印第三方外卖异常订单
     * @param currentShopId
     * @param date
     * @return
     */
    List<PlatformOrder> selectPlatFormErrorOrderList(String currentShopId, Date date);

    BigDecimal sumPlatformOrderPrice(String shopId,String beginDate,String endDate);
}

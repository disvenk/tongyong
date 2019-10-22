package com.resto.shop.web.report;

import com.resto.brand.web.dto.PlatformReportDto;
import com.resto.shop.web.model.PlatformOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PlatformOrderMapperReport{

    //调用存储过程查询
    PlatformReportDto proc_shopdetailId(Date beginDate, Date endDate, String shopDetailId);

    //根据店铺id，查询订单详情
    List<PlatformOrder> selectshopDetailIdList(Map map);

    //根据三方品台订单id查询该订单详细信息
    List<PlatformOrder> getPlatformOrderDetailList(String platformOrderId);

}

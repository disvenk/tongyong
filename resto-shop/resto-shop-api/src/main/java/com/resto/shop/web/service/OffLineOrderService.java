package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.dto.UnderLineOrderDto;
import com.resto.shop.web.model.OffLineOrder;

import java.util.Date;
import java.util.List;

public interface OffLineOrderService extends GenericService<OffLineOrder, String> {
    OffLineOrder  selectByTimeSourceAndShopId(Integer source,String shopId);

    List<OffLineOrder> selectByShopIdAndTime(String id, Date beginDate, Date endDate);

    List<OffLineOrder> selectlistByTimeSourceAndShopId(String id, Date begin, Date end, int offlinePos);

    List<OffLineOrder> selectlistByTimeSourceAndShopId(String id, String beginTime, String endTime, int offlinePos);


    /**
     * 查询一段时间内的订单的各项之和
     * @param offlinePos
     * @param id
     * @param todayBegin
     * @param todayEnd
     * @return
     */
    OffLineOrder selectSumByTimeSourceAndShopId(int offlinePos, String id, Date todayBegin, Date todayEnd);

	OffLineOrder selectByTimeSourceAndShopId(int offlinePos, String id, Date dateBegin, Date dateEnd);

    /**
     * 查询线下店铺交易笔数
     * @param currentBrandId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OrderNumDto> selectOrderNumByTimeAndBrandId(String currentBrandId, String beginDate, String endDate);

    /**
     * 结店 查询 线下录入的数据 (当天/当月)
     * @param todayBegin
     * @param todayEnd
     * @param monthBegin
     * @param monthEnd
     * @param shopId
     * @return
     */
    UnderLineOrderDto selectDateAndMonthByShopId(Date todayBegin, Date todayEnd, Date monthBegin, Date monthEnd, String shopId);

    /**
     * 结店 查询 线下录入的数据 (当旬)
     * @param xunBegin
     * @param xunEnd
     * @param shopId
     * @return
     */

    UnderLineOrderDto selectXunByShopId(Date xunBegin, Date xunEnd, String shopId);

    UnderLineOrderDto selectMonthByShopId(Date monthBegin, Date monthEnd, String id);

    UnderLineOrderDto selectMonthXunTodayByShopId(Date todayBegin, Date todayEnd, Date xunBegin, Date xunEnd, Date monthBegin, Date monthEnd, String id);

}

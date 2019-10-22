package com.resto.shop.web.dao;

import com.resto.shop.web.dto.UnderLineOrderDto;
import com.resto.shop.web.model.OffLineOrder;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OffLineOrderMapper  extends GenericDao<OffLineOrder,String> {
    int deleteByPrimaryKey(String id);

    int insert(OffLineOrder record);

    int insertSelective(OffLineOrder record);

    OffLineOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OffLineOrder record);

    int updateByPrimaryKey(OffLineOrder record);

    /**
     * 查询当日有效的线下pos订单信息
     * @param source
     * @param shopId
     * @param begin
     * @param end
     * @return
     */
    OffLineOrder selectByTimeSourceAndShopId(@Param("source") Integer source,@Param("shopId") String shopId,@Param("beginDate") Date begin,@Param("endDate") Date end);

    List<OffLineOrder> selectByShopIdAndTime(@Param("shopId") String shopId, @Param("beginDate") Date beginDate, @Param("endDate")Date endDate);

    /**
     * 查询这家店铺当月微信支付 + 支付宝支付之和
     * @param shopId 店铺id
     * @return 这家店铺当月微信支付 + 支付宝支付之和
     */
    BigDecimal selectSumRealMoney(@Param("shopId") String shopId, @Param("beginDate") Date beginDate, @Param("endDate")Date endDate);

    /**
     * 查询订单总额
     * @param shopId
     * @param beginDate
     * @param endDate
     * @return
     */
    BigDecimal selectTotalMoney(@Param("shopId") String shopId, @Param("beginDate") Date beginDate, @Param("endDate")Date endDate);

    OffLineOrder selectSumByTimeSourceAndShopId(@Param("orderType") int offlinePos, @Param("shopId") String id,@Param("beginDate") Date begin,@Param("endDate") Date end);

    /**
     * 结店 时 查询线下 日 和月的线下数据
     * @param todayBegin
     * @param todayEnd
     * @param monthBegin
     * @param monthEnd
     * @param shopId
     * @return
     */
    UnderLineOrderDto selectDateAndMonthByShopId(@Param("todayBegin") Date todayBegin, @Param("todayEnd") Date todayEnd,@Param("monthBegin") Date monthBegin, @Param("monthEnd") Date monthEnd, @Param("shopId") String shopId);

    /**
     * 结店 时 查询 旬的线下数据
     * @param xunBegin
     * @param xunEnd
     * @param shopId
     * @return
     */
    UnderLineOrderDto selectXunByShopId(@Param("xunBegin") Date xunBegin, @Param("xunEnd") Date xunEnd,@Param("shopId") String shopId);

    UnderLineOrderDto selectMonthByShopId(@Param("monthBegin")Date monthBegin,@Param("monthEnd") Date monthEnd, @Param("shopId")String shopId);

    UnderLineOrderDto selectMonthXunTodayByShopId(@Param("todayBegin") Date todayBegin, @Param("todayEnd") Date todayEnd, @Param("xunBegin") Date xunBegin, @Param("xunEnd") Date xunEnd, @Param("monthBegin") Date monthBegin,@Param("monthEnd") Date monthEnd,@Param("shopId") String shopId);
}

package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseDao<Order,String> {

    int insert(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> orderList(@Param("start") Integer start,@Param("datalength") Integer datalength,@Param("shopId") String shopId,@Param("customerId") String customerId,@Param("orderState") String[] orderState);

    List<Order> selectReadyList(String currentShopId);

    Order selectOrderStatesById(@Param("orderId") String orderId);

    List<Order> selectByParentId(@Param("parentOrderId")String parentOrderId, @Param("parentOrderPayType")Integer parentOrderPayType);

    BigDecimal selectPayBefore(String orderId);

    Double selectParentAmountByBossOrder(String orderId);

    Integer selectArticleCountByIdBossOrder(String id);

    Double selectParentAmount(@Param("orderId")String orderId,@Param("shopMode") Integer shopMode);

    Integer selectArticleCountById(@Param("id")String id,@Param("shopMode")Integer shopMode);

    Order getLastOrderByCustomer(@Param("customerId")String customerId,@Param("shopId") String shopId,@Param("time") Integer time);

    Integer checkTableNumber(@Param("shopId") String shopId,@Param("tableNumber") String tableNumber,@Param("customerId") String customerId, @Param("closeContinueTime") Integer closeContinueTime);

    Integer selectArticleCount(String articleId);

    Integer selectArticlePriceCount(String articleId);

    void clearPushOrder(String id, int notOrder);

    List<Order> selectListByParentId(@Param("orderId") String orderId);

    BigDecimal getRefundSumByOrderId(@Param("orderId")String orderId,@Param("type")int type);

    BigDecimal getAliPayment(@Param("orderId")String orderId);

    Order findCustomerNewOrder(@Param("beginDate") Date beginDate,@Param("customerId") String customerId,@Param("shopId") String shopId,@Param("orderState") Integer[] orderState,@Param("orderId") String orderId);

    List<String> selectChildIdsByParentIdByFive(String id);

    List<String> selectChildIdsByParentId(String id);

    String selectNewCustomerPackageId(String currentCustomerId, String currentShopId);

    void setOrderNumber(@Param("orderId")String orderId,@Param("tableNumber") String tableNumber);

    BigDecimal getPayHoufu(String orderId);

    Order customerByOrderForMyPage(@Param("customerId") String customerId, @Param("shopId") String shopId);

    Order getLastOrderByTableNumber(@Param("tableNumber") String tableNumber,@Param("shopId") String shopId);

    List<Order> selectWXOrderItems(Map<String, Object> map);

    Order getLastOrderBySevenMode(String customerId);

}

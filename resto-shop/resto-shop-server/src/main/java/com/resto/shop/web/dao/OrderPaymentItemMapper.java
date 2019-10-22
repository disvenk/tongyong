package com.resto.shop.web.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.resto.shop.web.model.Order;
import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.dto.IncomeReportDto;
import com.resto.shop.web.model.OrderPaymentItem;

public interface OrderPaymentItemMapper  extends GenericDao<OrderPaymentItem,String> {
    int deleteByPrimaryKey(String id);

    int insert(OrderPaymentItem record);

    int insertSelective(OrderPaymentItem record);

    OrderPaymentItem selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderPaymentItem record);

    int updateByPrimaryKeyWithBLOBs(OrderPaymentItem record);

    int updateByPrimaryKey(OrderPaymentItem record);

    List<OrderPaymentItem> selectByOrderId(String orderId);




    List<OrderPaymentItem> selectByOrderIdList(String orderId);

    List<OrderPaymentItem> selectpaymentByPaymentMode(@Param("shopId")String shopId,@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

    /**
     * 根据时间查询  【充值订单 】状态为 1 的信息，用于报表统计时使用
     * @param shopId
     * @param beginDate
     * @param endDate
     * @author lmx
     * @return
     */
    OrderPaymentItem selectChargeOrderByDate(@Param("shopId")String shopId,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

    /**
     * 查询品牌下不同店铺的数据
     * @param brandId
     * @return
     */
    List<IncomeReportDto> selectIncomeList(@Param("brandId")String brandId,@Param("begin")Date begin,@Param("end")Date end);

    /**
     * 查询店铺的营收数据
     * @param shopId
     * @param begin
     * @param end
     * @return
     */
    List<IncomeReportDto> selectIncomeListByShopId(@Param("shopId") String shopId, @Param("begin") Date begin,@Param("end") Date end);

    /**
     * 查询异常订单 订单退款失败：  退款金额大于支付金额
     * @param begin
     * @param end
     * @return
     */
    List<OrderPaymentItem> selectListByResultData(@Param("beginDate") Date begin, @Param("endDate") Date end);

    List<Order> selectOrderMoneyByBrandIdGroupByOrderId(@Param("beginDate")Date begin,@Param("endDate")  Date end);

    /**
     * 品牌收入报表查询
     * 2016-10-29
     * @return
     */
    List<Map<String, Object>> selectShopIncomeList(Map<String, Object> map);

    /**
     * pos端店铺收入报表查询
     * 2016-10-29
     * @param begin
     * @param end
     * @param shopId
     * @return
     */
    List<OrderPaymentItem> selectShopIncomeListByShopId(@Param("beginDate")Date begin,@Param("endDate") Date end, @Param("shopId")String shopId);
    /**
     * 查询异常订单 订单退款失败 没有证书
     * @param begin
     * @param end
     * @return
     */
    List<OrderPaymentItem> selectListByResultDataByNoFile(@Param("beginDate") Date begin, @Param("endDate") Date end);

    List<OrderPaymentItem> selectRefund(@Param("orderId") String orderId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    BigDecimal sumOrderMoneyByOrderId(@Param("orderId")String orderId);

    /**
     *
     * 查询退款证书丢失的订单项
     * 2016-11-1
     * @param orderId
     * @return
     */
    OrderPaymentItem selectByOrderIdAndResultData(String orderId);
    
    List<OrderPaymentItem> selectOrderPayMentItem(Map<String, String> map);
    
    List<OrderPaymentItem> selectPaymentCountByOrderId(@Param("orderId") String orderId);
    
    OrderPaymentItem selectPayMentSumByrefundOrder(@Param("orderId") String orderId);

    OrderPaymentItem selectByShanhuiPayOrder(String orderId);

    void updateByShanhuiPayOrder(@Param("orderId")String orderId, @Param("param")String param);

    OrderPaymentItem insertByBeforePay(OrderPaymentItem record);

    OrderPaymentItem selectWeChatPayResultData(@Param("shopId") String shopId);

    List<OrderPaymentItem> selectRefundPayMent(@Param("orderId") String orderId);

    int deleteByOrderId(String orderId);

    void posSyncDeleteByOrderId(String orderId);

    List<OrderPaymentItem> posSyncListByOrderId(@Param("orderId") String orderId);

    List<OrderPaymentItem> selectPayMentByPayMode(@Param("orderId") String orderId, @Param("payMode") Integer payMode, @Param("type") Integer type);

    BigDecimal selectEnableTicketMoney(@Param("orderId")String orderId);

    void insertItems(List<OrderPaymentItem> orderPaymentItems);

    int insertList(Map<String,List<OrderPaymentItem>> list);

    int updatePayMentByCode(@Param("code")String code,@Param("orderId") String orderId);

    List<OrderPaymentItem> selectDiscountList(@Param("orderId")String orderId);

    void deletePaymentByToPayId(@Param("toPayId") String toPayId);

    OrderPaymentItem selectUseProductCoupons(@Param("orderId") String orderId, @Param("articleId") String articleId);

    OrderPaymentItem selectByToPayIdpaymentModeId(@Param("paymentModeId") Integer paymentModeId, @Param("toPayId") String toPayId);

    List<OrderPaymentItem> selectParentPaymentByOrderId(String orderId);
}

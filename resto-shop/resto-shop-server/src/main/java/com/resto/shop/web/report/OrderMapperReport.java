package com.resto.shop.web.report;


import com.resto.brand.web.dto.*;
import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderPaymentItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderMapperReport{

	/**
	 * 每个店铺的交易笔数
	 * @param brandId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<OrderNumDto> selectOrderNumByTimeAndBrandId(@Param("brandId") String brandId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

	BrandOrderReportDto procDayAllOrderItemBrand(Map<String, Object> selectMap);

	List<Order> selectListByTime(@Param("beginDate")Date begin, @Param("endDate")Date end, @Param("shopId") String shopId, @Param("customerId") String customerId);

	List<ShopIncomeDto> callProcDayAllOrderItem(Map<String, Object> selectMap);

	List<ShopIncomeDto> callProcDayAllOrderPayMent(Map<String, Object> selectMap);

	ShopOrderReportDto procDayAllOrderItemShop(Date beginDate, Date endDate, String shopId);

	/**
	 * 查询店铺下所有的已消费的订单
	 * @param begin
	 * @param end
	 * @param shopId
	 * @return
	 */

	List<Order> selectListByShopId(@Param("beginDate") Date begin, @Param("endDate") Date end,@Param("shopId") String shopId);

	/**
	 * 查询已消费的订单
	 * @param begin
	 * @param end
	 * @param brandId
	 * @return
	 */
	List<Order> selectListBybrandId(@Param("beginDate")Date begin,@Param("endDate") Date end,@Param("brandId") String brandId,@Param("type") Integer type);

	/**
	 * 查询退菜报表list
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<RefundArticleOrder> addRefundArticleDto(@Param("beginDate") String beginDate, @Param("endDate") String endDate,@Param("shopId") String shopId);

	Integer selectBrandArticleNum(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

	List<brandArticleReportDto> selectConfirmMoney(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

	List<Map<String, Object>> selectMealServiceSales(Map<String, Object> selectMap);

	/**
	 * 查询品牌下每个店铺的菜品销售和
	 * @param begin
	 * @param end
	 * @param brandId
	 * @return
	 */
	List<ShopArticleReportDto> selectShopArticleSell(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("brandId") String brandId);

	/**
	 * 查询品牌菜品库下每个店铺的菜品销售和
	 * @param begin
	 * @param end
	 * @param brandId
	 * @return
	 */
	List<ShopArticleReportDto> selectShopArticleSellNew(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("brandId") String brandId);

	/**
	 * 查询微信支付金额
	 * @param beginDate
	 * @param endDate
	 * @param shopIdlist
	 * @return
	 */
	ShopIncomeDto selectWechatMoney(@Param("beginDate")String beginDate,@Param("endDate") String endDate,@Param("shopIdlist")List<String> shopIdlist);


	List<WeChatBill> uploadWeChatBill(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("shopId") String shopId);

    Integer selectBrandArticleNumNew(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

	List<brandArticleReportDto> selectConfirmMoneyNew(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

    List<OrderPaymentItem> selectBusinessReport(Map<String, Object> selectMap);
}

package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.dto.*;
import com.resto.shop.web.dto.EnabelTicketOrderDto;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.model.OrderPaymentItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderMapper  extends GenericDao<Order,String> {
    int deleteByPrimaryKey(String id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);

    Order selectByOrderId(String orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    /**
     * 根据当前 店铺ID 和 用户ID 分页查询 其订单列表
     * @param start
     * @param datalength
     * @param shopId
     * @param customerId
     * @return
     */
    List<Order> orderList(@Param("start") Integer start,@Param("datalength") Integer datalength,@Param("shopId") String shopId,@Param("customerId") String customerId,@Param("orderState") String[] orderState);
    
    /**
     * 根据订单查询 订单状态 和 生产状态
     * @param orderId
     * @return
     */
    Order selectOrderStatesById(@Param("orderId") String orderId);


	/**.
	 * 查询成功支付订单
	 * @param beginTime
	 * @param endTime
	 * @param shopId
	 * @return
	 */
	List<Order> selectOrderSuccess(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime ,@Param("shopId")String shopId);

    /**
     * 查询  用户的新订单,如果 订单ID有值。则查询订单的详情
     * @param beginDate		当天的开始时间
     * @param customerId	当前用户ID
     * @param shopId		当前店铺ID
     * @param orderId		订单ID
     * @return
     */
    Order findCustomerNewOrder(@Param("beginDate") Date beginDate,@Param("customerId") String customerId,@Param("shopId") String shopId,@Param("orderState") Integer[] orderState,@Param("orderId") String orderId);

    /**
     * 查询某个店铺某天，某个状态的订单
     * @param shopId
     * @param date
     * @param proStatus
     * @return
     */

	List<Order> selectShopOrderByDateAndProductionStates(@Param("shopId")String shopId,@Param("date") Date date,@Param("proStatus") int[] proStatus);


	/**
	 *  根据店铺id得到店铺当日的订单情况
	 * @param shopId
	 * @return
     */
	Order getOrderAccount(String shopId);

	Order getOrderAccountHoufu(String shopId);

	Order getOrderAccountBoss(String shopId);
	/**
	 * 查询某天的历史订单
	 * 根据店铺模式查询不同状态的历史订单。（2016-10-11，已取消此功能，改为所有模式的店铺的历史订单都不为取消状态和未支付状态）
	 * @param currentShopId
	 * @param dateBegin
	 * @param dateEnd
	 * @param shopMode
     * @return
	 */
	List<Order> selectHistoryOrderList(@Param("shopId") String currentShopId, @Param("dateBegin") Date dateBegin, @Param("dateEnd") Date dateEnd, @Param("shopMode") Integer shopMode);

	List<Order>listHoufuFinishedOrder(@Param("shopId") String shopId,@Param("shopMode") Integer shopMode);




	List<Order>listHoufuUnFinishedOrder(String shopId);

	/**
	 * 查询某天的异常订单
	 * @param currentShopId
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	List<Order> selectErrorOrderList(@Param("shopId")String currentShopId, @Param("dateBegin")Date dateBegin, @Param("dateEnd")Date dateEnd);


	List<Order> selectErrorOrder(@Param("dateBegin")Date dateBegin, @Param("dateEnd")Date dateEnd);




	/**
	 * 查询未付款的订单（后付模式）
	 * @param currentShopId
	 * @param dateBegin
	 * @param dateEnd
     * @return
     */
	List<Order> getOrderNoPayList(@Param("shopId")String currentShopId, @Param("dateBegin")Date dateBegin, @Param("dateEnd")Date dateEnd);


	void clearPushOrder(String id, int notOrder);

	void setOrderNumber(@Param("orderId")String orderId,@Param("tableNumber") String tableNumber);
	
	/**
	 * 根据取餐码查询已支付的订单
	 * @param vercode
	 * @return
	 */
	List<Order> selectOrderByVercode(@Param("vercode")String vercode,@Param("shopId")String shopId);

	/**
	 * 查询已消费订单的订单数目和订单总额
	 * @param begin
	 * @param end
	 * @param brandId
	 * @return
	 */
	OrderPayDto selectBytimeAndState(@Param("beginDate")Date begin, @Param("endDate")Date end, @Param("brandId")String brandId);


	/**
	 * 根据桌号查询店铺已支付的订单
	 * @param tableNumber
	 * @return
	 */
	List<Order> selectOrderByTableNumber(@Param("tableNumber")String tableNumber,@Param("shopId")String shopId);

	List<Order> listOrderByStatus(@Param("shopId")String currentShopId,@Param("begin") Date begin, 
			@Param("end")Date end, @Param("p_status")int[] productionStatus, 
			@Param("o_state")int[] orderState);

	void updateParentAmount(String orderId, Double money);

	Double selectParentAmount(@Param("orderId")String orderId,@Param("shopMode") Integer shopMode);

	Double selectParentAmountByBossOrder(String orderId);

	BigDecimal selectPayBefore(String orderId);

	void changeAllowContinue(String id, boolean b);

	Integer selectArticleCountById(@Param("id")String id,@Param("shopMode")Integer shopMode);

	Integer selectArticleCountByIdBossOrder(String id);

	List<Order> selectByParentId(@Param("parentOrderId")String parentOrderId, @Param("parentOrderPayType")Integer parentOrderPayType);

	List<String> selectChildIdsByParentId(String id);

	List<String> selectChildIdsByParentIdByFive(String id);

	String selectNewCustomerPackageId(String currentCustomerId, String currentShopId);

	List<Order> selectReadyList(String currentShopId);
	
	
	/**
	 * 根据时间 和指定 店铺ID 查询已完成的订单(orderSatus = 2,10,11,12)
	 * 的菜品销售总和。
	 * @param beginDate
	 * @param endDate
	 * @param brandId
	 * @return
	 */
    Integer selectArticleSumCountByData(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("brandId")String brandId);
	
	
	/**
	 * 根据时间 和 指定 店铺 查询 已完成的订单的 菜品销售详情
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	public List<ArticleSellDto> selectShopArticleSellByDate(@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("shopId")String shopId, @Param("sort")String sort);
	
	
	/**
	 * 根据时间 查询 当前品牌已完成的订单的 菜品销售详情
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<ArticleSellDto> selectBrandArticleSellByDate(@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("sort")String sort);
	
	/**
	 * 根据时间 查询当前品牌已完成订单的某个餐品分类的销售详情
	 * @param beginDate
	 * @param endDate
	 * @param articleFamilyId
	 * @return
	 */
	List<ArticleSellDto> selectBrandArticleSellByDateAndArticleFamilyId(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate,@Param("articleFamilyId")String articleFamilyId,@Param("sort")String sort);


	List<ArticleSellDto> selectShopArticleSellByDateAndArticleFamilyId(@Param("beginDate")Date begin,@Param("endDate")Date end,@Param("shopId")String shopId,@Param("articleFamilyId")String articleFamilyId ,@Param("sort")String sort);
	
	/**
	 * 根据时间查询店铺已完成订单的菜品销售详情
	 * @param shopId
	 * @param begin
	 * @param end
	 * @param sort
	 * @return
	 */
	List<ArticleSellDto> selectShopArticleByDate(@Param("shopId")String shopId,@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("sort") String sort);

	/**
	 * 根据时间  菜品分类id  查询店铺已完成订单的菜品销售详情
	 * @param begin
	 * @param end
	 * @param shopId
	 * @param articleFamilyId
	 * @param sort
	 * @return
	 */
	List<ArticleSellDto> selectShopArticleByDateAndArticleFamilyId(@Param("beginDate")Date begin, @Param("endDate")Date end, @Param("shopId")String shopId,
			@Param("articleFamilyId")String articleFamilyId,@Param("sort") String sort);

	/**
	 * 查询品牌下所有店铺的菜品销售情况
	 * @param beginDate
	 * @param endDate
	 * @param brandId
	 * @return
	 */
	List<ShopArticleReportDto> selectShopArticleDetails(@Param("beginDate")Date beginDate,@Param("endDate") Date endDate, @Param("brandId")String brandId);

	List<ShopArticleReportDto> selectShopArticleCom(@Param("beginDate")Date beginDate,@Param("endDate") Date endDate, @Param("brandId")String brandId);

    /**
     * 查询店铺的菜品销售总量
     * @param brandId
     * @param begin
     * @param end
     * @param sort
     * @return
     */
//	int selectShopArticleNum(@Param("beginDate")Date beginDate,@Param("endDate") Date endDate, @Param("shopId")String shopId);

	List<ArticleSellDto> selectBrandArticleSellByDateAndFamilyId(@Param("brandId")String brandId,@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("sort") String sort);

	List<ArticleSellDto> selectBrandArticleSellByDateAndId(@Param("brandId")String brandId,@Param("beginDate") Date begin, @Param("endDate")Date end,@Param("sort") String sort);

	List<ArticleSellDto> selectBrandFamilyArticleSellByDateAndArticleFamilyId(@Param("brandId")String brandId,@Param("articleFamilyName")String articleFamilyName, @Param("beginDate")Date begin,
			 @Param("endDate")Date end, @Param("sort") String sort);

	List<Order> selectMoneyAndNumByDate(@Param("beginDate")Date begin, @Param("endDate")Date end, @Param("brandId")String brandId);

	List<ArticleSellDto> selectShopArticleSellByDateAndFamilyId(@Param("shopId")String shopId,@Param("beginDate") Date begin,@Param("endDate") Date end, @Param("sort")String sort);

	List<ArticleSellDto> selectShopArticleSellByDateAndId(@Param("shopId")String shopId,@Param("beginDate") Date begin, @Param("endDate")Date end,@Param("sort") String sort);

	Order selectOrderDetails(String orderId);

	int setOrderPrintFail(String orderId);

	List<ArticleSellDto> selectArticleFamilyByBrandAndFamilyName(@Param("brandId")String brandId,@Param("beginDate") Date begin, @Param("endDate")Date end,
			@Param("articleFamilyName")String articleFamilyName);

	List<Order> selectAppraiseByShopId(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("shopId")String shopId);

	/**
	 * 根据菜品id查看菜品库存
	 * @param articleId
	 * @return
     */
	Integer selectArticleCount(String articleId);

	/**
	 * 查看有规格的菜品库存
	 * @param articleId
	 * @return
     */
	Integer selectArticlePriceCount(String articleId);



	/**
	 * 更新该餐品库存 （-1）（无规格）
	 * @param articleId 餐品id
	 * @return
	 */
	Boolean updateArticleStock(@Param("articleId") String articleId,@Param("type") String type,@Param("count") Integer count);

	/**
	 * 更新该餐品库存 （-1）（有规格）
	 * @param articleId 餐品id
	 * @return
	 */
	Boolean updateArticlePriceStock(@Param("articleId") String articleId,@Param("type") String type,@Param("count") Integer count);

	/**
	 * 库存为0时设置沽清	---	tb_article
	 * @param articleId
	 * @return
	 */
	Boolean setEmpty(String articleId);


	/**
	 * 库存为0时设置沽清	---	tb_article_price
	 * @param articleId
	 * @return
	 */
	Boolean setArticlePriceEmpty(String articleId);


	/**
	 * 还原库存时重置售罄状态	---	tb_article
	 * @param articleId
	 * @return
     */
	Boolean setEmptyFail(String articleId);

	/**
	 * 还原库存时重置售罄状态	---	tb_article_price
	 * @param articleId
	 * @return
     */
	Boolean setArticlePriceEmptyFail(String articleId);

	/**
	 * 将单品最低库存设置为 套餐库存
	 * @return
	 */
	Boolean setStockBySuit(@Param("shopId")String shopId);

	BigDecimal getPayment(Map<String, Object> selectMap);

    /**
     *
     * @param shopId
     * @param familyId
     * @return
     */
	Integer getArticleCount(@Param("shopId") String shopId,@Param("familyId") String familyId);

    List<OrderArticleDto>  selectOrderArticle(@Param("brandId") String brandId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    /**
     * 查询品牌菜品的数据 用于中间数据库
     * @param brandId
     * @param begin
     * @param end
     * @return
     */
    List<Map<String,Object>> selectBrandArticleSellList(@Param("brandId") String brandId,@Param("beginDate") Date begin,@Param("endDate") Date end);

    /**
     * 查询订单详情数据 用于中间数据库
     * @param begin
     * @param end
     * @param brandId
     * @return
     */
    List<Order> selectListByTimeAndBrandId(@Param("brandId") String brandId,@Param("beginDate") Date begin, @Param("endDate") Date end);

    /**
     * 查询店铺菜品数据 用于中间数据库
     * @param shopId
     * @param begin
     * @param end
     * @return
     */
    List<Map<String,Object>> selectShopArticleSellList(@Param("shopId") String shopId, @Param("beginDate") Date begin, @Param("endDate") Date end);


    /**
     * 查询品牌菜品的总销量
     * @param begin
     * @param end
     * @param brandId
     * @return
     */
    int selectBrandArticleSum(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("brandId") String brandId);

    /**
     * 查询品牌菜品的总价值
     * @param begin
     * @param end
     * @param brandId
     * @return
     */

    BigDecimal selectBrandArticleSell(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("brandId") String brandId);

    /**
     * 查询品牌下每个店铺的菜品销量和
     * @param begin
     * @param end
     * @param brandId
     * @return
     */
    List<ShopArticleReportDto> selectShopArticleSum(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("brandId") String brandId);

    /**
     * 根据订单状态和生产状态查询指定店铺的订单
     * @param shopId
     * @param orderStates
     * @param productionStates
     * @return
     */
    List<Order> selectByOrderSatesAndProductionStates(@Param("shopId")String shopId,@Param("orderStates")String[] orderStates,@Param("productionStates")String[] productionStates);

	/**
	 * 根据订单状态和生产状态查询指定店铺的订单  (包含外卖)
	 * @param shopId
	 * @param orderStates
	 * @param productionStates
	 * @return
	 */
	List<Order> selectByOrderSatesAndProductionStatesTakeout(@Param("shopId")String shopId,@Param("orderStates")String[] orderStates,@Param("productionStates")String[] productionStates);
    /**
     * 查询所有不正常的订单(并非异常订单)
     * 原因：所有支付的订单(2,10,11,12) 都会去打印，如果打印机有问题或连结不上最终也只会到异常订单里面 不会出现 0,1的情况
     * 所以出现已支付但是生产状态是0,1的都是不正常的订单
     * @param begin
     * @param end
     * @param brandId
     * @return
     */
    List<Order> selectExceptionOrderListBybrandId(@Param("beginDate") Date begin,@Param("endDate") Date end, @Param("brandId") String brandId);

    /**
     * @param begin
     * @param end
     * @param brandId
     * @return
     */
    List<Order> selectHasPayListOrderByBrandId(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("brandId") String brandId);

    List<Order> selectHasPayOrderPayMentItemListBybrandId(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

    /**
     * 手动取消订单
     * @param brandId
     * @param begin
     * @param end
     * @return
     */
    List<Order> selectNeedCacelOrderList(@Param("brandId") String brandId, @Param("begin") Date begin, @Param("end") Date end);
	/**
	 * 获取所有桌号加菜列表
	 * @param shopId
	 * @return
     */
	List<Order> getTableNumberAll(@Param("shopId") String shopId);

	Order getOrderDetail(String orderId);

	List<OrderPaymentItem> selectOrderPaymentItems(String orderId);

	List<OrderItem> selectOrderItems(String orderId);

	List<Order> getOrderByEmployee(@Param("shopId") String shopId,@Param("employeeId") String employeeId);


	Order getLastOrderByCustomer(@Param("customerId")String customerId,@Param("shopId") String shopId,@Param("time") Integer time,@Param("tableNumber") String tableNumber);

	Order getGroupOrderByGroupId(String groupId);

	Order getLastOrderBySevenMode(String customerId);


	Order getLastOrderByTableNumber(@Param("tableNumber") String tableNumber,@Param("shopId") String shopId);

	Order selectOrderByTableNumberNoPay(@Param("tableNumber") String tableNumber,@Param("shopId") String shopId);

	BigDecimal getServicePrice(String shopId);

	void refundServicePrice(@Param("id") String id,@Param("servicePrice") BigDecimal servicePrice,@Param("customerCount") Integer customerCount);

	Integer getMealALLNumber(String shopId);

	BigDecimal getRefundSumByOrderId(@Param("orderId")String orderId,@Param("type")int type);

	BigDecimal getAliPayment(@Param("orderId")String orderId);

	Integer getCustomerPerson(String shopId);
    List<Order> selectOrderListItemByBrandId(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

    List<Order> selectListByParentId(@Param("orderId") String orderId);
	
	public List<Order> selectWXOrderItems(Map<String, Object> map);
	public List<Order> selectWXOrderItems(String serialNumber);
	/**
	 * 会员模块
	 * 用户订单管理
	 */
   List<Order> getCustomerOrderList(@Param("customerId") String customerId,@Param("beginDate") String beginDate, @Param("endDate") String endDate);

	Integer selectByCustomerCount(@Param("customerId") String customerId);

	public List<Order> selectOrderByOrderIds(Map<String, Object> orderIds);
    /**
     * 报表数据查询用于短信推送
     * 短信推送
     * @param begin
     * @param end
     * @param shopId
     * @return
     */
    List<Order> selectListsmsByShopId(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("shopId") String shopId);

    /**
     * 查询该店铺历史的订单数据
     * @param shopId
     * @param dateEnd
     * @return
     */
    List<Order> selectOrderHistoryList(@Param("shopId") String shopId, @Param("endDate") Date dateEnd);

    /**
     * 结店时查询生产状态未改变的订单
     * @param shopId
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    List<Order> selectHasPayNoChangeStatus(@Param("shopId") String shopId, @Param("beginDate") Date dateBegin,@Param("endDate") Date dateEnd);

    /**
     * 小程序定时任务是 查询品牌下生产状态未改变的订单
     * @param beginDate
     * @param endDate
     * @param brandId
     * @return
     */
    List<Order> selectHasPayNoChangeStatusByBrandId(@Param("brandId") String brandId,@Param("beginDate") Date  beginDate, @Param("endDate") Date endDate);

    /**
     * 查询品牌的某个时间的分数
     * @param brandId
     * @param beginDate
     * @param endDate
     * @return
     */
    Double selectAppraiseBybrandId(@Param("brandId") String brandId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * yz
     * @param id
     * @param beginDate
     * @param endDate
     * @return
     */
    BigDecimal selectOrderMoneyByShopIdAndTime(@Param("shopId") String id,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

    Double selectAppraiseSumByShopId(@Param("shopId") String shopId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

	/**
	 * 大boss模式下查询新增订单(不包含外卖订单)
	 */

	List<Order> selectOrderByBoss(String shopId);

	/**
	 * 大boss模式下查询新增订单(包含外卖订单)
	 */

	List<Order> selectOrderByBossTakeout(String shopId);

	/**
	 * 返回用户的最后一比订单时间
	 */
	Order getCustomerLastOrder(String customerId);

	int confirmOrderPos(String orderId);


	Integer checkTableNumber(@Param("shopId") String shopId,@Param("tableNumber") String tableNumber,@Param("customerId") String customerId, @Param("closeContinueTime") Integer closeContinueTime);

	BigDecimal getPayHoufu(String orderId);

	List<Order> getTodayFinishOrder(@Param("shopId") String shopId,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

    List<OrderItem> selectListByShopIdAndTime(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime, @Param("shopId") String id);

    List<OrderItem> selectCustomerListByShopIdAndTime(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime, @Param("shopId") String id);

    /**
     * 2017-03-07新增用户的订单
     * @param id
     * @param todayBegin
     * @param todayEnd
     * @return
     */
    List<Order> selectNewCustomerOrderByShopIdAndTime(@Param("shopId") String id, @Param("beginDate") Date todayBegin,@Param("endDate") Date todayEnd);


    /**
     * 回头用户
     * 2017-03-07
     * @param id
     * @param todayBegin
     * @param todayEnd
     * @return
     */
    List<BackCustomerDto> selectBackCustomerByShopIdAndTime(@Param("shopId")String id, @Param("beginDate")Date todayBegin, @Param("endDate")Date todayEnd);

    /**
     * 2017-03-07
     * @param id
     * @param todayBegin
     * @param todayEnd
     * @return
     */
    List<Order> selectCompleteByShopIdAndTime(@Param("shopId")String id, @Param("beginDate")Date todayBegin,@Param("endDate") Date todayEnd);

    /**
     * 用于查询特定时间内的日结小票
     * @param selectMap
     * @return
     */
    List<Order> getOrderAccountByTime(Map<String, Object> selectMap);

	/**
	 * 添加加菜订单 是用支付宝 现金 银联支付的时候  未确认的时候  需要在pos新增订单上出现
	 * @param shopId
	 * @return
     */
	List<Order> selectByOrderSatesAndNoPay(String shopId);

    /**
     * 查询订单--订单项--订单菜品 用于第三方的接口
     * @param begin
     * @param end
     * @param brandId
     * @return
     */
    List<Order> selectBaseToThirdList(@Param("beginDate") Date begin,@Param("endDate") Date end, @Param("brandId") String brandId);

    List<Order> selectBaseToThirdListByShopId(@Param("beginDate") Date begin,@Param("endDate") Date end,@Param("shopId") String shopId);

    /**
     * kc对接的订单
     * @param brandId
     * @param begin
     * @param end
     * @return
     */
    List<Order> selectBaseToKCList(@Param("brandId") String brandId,@Param("beginDate") Date begin, @Param("endDate") Date end);

    List<Order> selectBaseToKCListByShopId(@Param("shopId") String shopId, @Param("beginDate") Date begin,@Param("endDate") Date end);

    List<Article> getStockBySuit(String shopId);

    List<Order> selectMonthIncomeDto(Map<String, Object> selectMap);

	void fixAllowContinueOrder(@Param("beginDate") Date begin);

	List<Order> getAllowAppraise();

	Order customerByOrderForMyPage(@Param("customerId") String customerId, @Param("shopId") String shopId);

	void colseOrder(String orderId);

	/**
	 * yz 2017/07/28
	 * 新增用户的订单 以品牌为基准
	 * @param brandId
	 * @param begin
	 * @param end
	 * @return
	 */
	List<Order> selectNewCustomerOrderByBrandIdAndTime(@Param("brandId") String brandId, @Param("beginDate") Date begin, @Param("endDate") Date end);

	/**
	 * yz 2017/07/28
	 * 品牌回头用户
	 * @param brandId
	 * @param begin
	 * @param end
	 * @return
	 */
	List<BackCustomerDto> selectBackCustomerByBrandIdAndTime(@Param("brandId") String brandId,@Param("beginDate") Date begin, @Param("endDate") Date end);

	/**
	 * yz 2017/07/28
	 * 品牌回头订单
	 * @param brandId
	 * @param begin
	 * @param end
	 * @return
	 */
	List<Order> selectCompleteByBrandIdAndTime(@Param("brandId") String brandId, @Param("beginDate") Date begin, @Param("endDate") Date end);

	/**
	 * yz 2017/07/28
	 * @param begin
	 * @param end
	 * @param brandId
	 * @return
	 */
	List<Order> selectListsmsByBrandId(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

	/**
	 * yz 2017/07/31 用户id+shopId 查询已经消费订单
	 * @param shopDetailId
	 * @param customerId
	 * @return
	 */
	List<Order> selectOrderListByCustomerIdAndShopId(@Param("shopId") String shopDetailId, @Param("customerId") String customerId);

	List<Map<String, String>> selectCustomerOrderCount(List<String> customerIds);

	/**
	 * 用户最近一把有效订单
	 * @param customerId
	 * @return
     */
	Order selectAfterValidOrderByCustomerId(String customerId);

	/**
	 * Pos 2.0 数据同步方法，根据 orderId 查询订单信息
	 * @param orderId
	 * @return
	 */
	Order posSyncSelectById(@Param("orderId") String orderId);

	/**
	 *
	 * @param shopId
	 * @param customerId
	 * @return
	 */
	Integer selectCompleteOrderCount(@Param("shopId") String shopId, @Param("customerId") String customerId);

	List<String> posSelectNotCancelledOrdersIdByDate(@Param("shopId") String shopId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

	Order selectBySerialNumber(String serialNumber);

	BigDecimal selectSurplusAmountByPayMode(@Param("orderId") String orderId,@Param("payMode") Integer payMode, @Param("isOnLine") Integer isOnLine);

    List<String> serverExceptionOrderList( @Param("shopId") String shopId, @Param("isFirstPay") boolean isFirstPay, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    List<Order> selectNeedWeightArticle(String orderId);

	List<EnabelTicketOrderDto> selectEnableTicketOrder(@Param("customerId")String customerId);

	List<Order> selectLaifuShi();

	/**
	 * 获取桌号最后一笔主订单
	 * @param shopId
	 * @param tableNumber
	 * @return
	 */
	Order selectLastOrderByTableNumber(@Param("shopId") String shopId, @Param("tableNumber") String tableNumber);

	Integer selectGrantCountByDate(@Param("beginDate")String beginDate, @Param("endDate")String endDate);

	List<OrderPaymentItem> selectCashRefundInfo(@Param("orderId") String orderId);
}

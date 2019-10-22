package com.resto.shop.web.service;


import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.*;
import com.resto.brand.web.model.AccountSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.shop.web.dto.OrderNumDto;
import com.resto.shop.web.dto.Summarry;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.model.OrderPaymentItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService extends GenericService<Order, String> {
    
	/**
     * 根据当前 店铺ID 和 用户ID 分页查询 其订单列表
     * @param start
     * @param datalength
     * @param shopId
     * @param customerId
     * @return
     */
	public List<Order> listOrder(Integer start, Integer datalength, String shopId, String customerId, String ORDER_STATE);

	/**.
	 * 查询成功支付订单
	 * @param beginTime
	 * @param endTime
	 * @param shopId
	 * @return
	 */
	List<Order> selectOrderSuccess(Date beginTime,Date endTime ,String shopId);

	/**
	 * 根据订单ID查询订单状态和生产状态
	 * @param orderId
	 * @return
	 */
	public Order selectOrderStatesById(String orderId);

	public JSONResult createOrder(Order order)throws AppException;

	public JSONResult repayOrder(Order order)throws AppException;

	public Order findCustomerNewOrder(String customerId,String shopId,String orderId);

	public boolean cancelOrder(String string);

	public boolean autoRefundOrder(String string);

	Boolean checkRefundLimit(Order order);

	public JSONResult orderWxPaySuccess(OrderPaymentItem item);

	public JSONResult orderWxPaySuccessAspect(JSONResult o);

	public JSONResult pushOrder(String orderId) throws AppException;
	
	public JSONResult callNumber(String orderId);

	public JSONResult printSuccess(String orderId, Boolean openBrandAccount, AccountSetting accountSetting);

	public int printUpdate(String orderId);

	Order getOrderAccount(String shopId);

	/**
	 * 查询当天某些状态的订单
	 * @param shopId
	 * @return
	 */
	public List<Order> selectTodayOrder(String shopId, int[] is);

	public List<Order> selectReadyOrder(String currentShopId);

	public List<Order> selectPushOrder(String currentShopId,Long lastTime);

	public List<Order> selectCallOrder(String currentBrandId,Long lastTime);

	public Map<String, Object> printReceipt(String orderId,Integer selectPrinterId);

	/**
	 * 打印厨房的小票
	 * @param order			订单信息
	 * @param articleList	订单菜品集合
	 * @return
	 */
	public List<Map<String,Object>> printKitchen(Order order, List<OrderItem> articleList, Integer distributionModeId);

    /**
     * 打印换桌的小票
     * @param order
     * @return
     */
	public List<Map<String, Object>> printTurnTable(Order order,String oldtableNumber);


	public JSONResult confirmOrder(Order order);

	public JSONResult confirmWaiMaiOrder(Order order);

	public JSONResult confirmBossOrder(Order order);

	public Order getOrderInfo(String orderId);

	public Order getOrderInfoPos(String orderId);

	public List<Order> selectHistoryOrderList(String currentShopId, Date date, Integer shopMode);

	public List<Order> selectErrorOrderList(String currentShopId, Date date);

	public List<Order> selectErrorOrder( Date date);

	public List<Order> getOrderNoPayList(String currentShopId, Date date);


	public Order cancelOrderPos(String orderId) throws AppException;

	public void changePushOrder(Order order);

	public List<Map<String, Object>> printOrderAll(String orderId);

	public void setTableNumber(String orderId, String tableNumber);
	
	/**
	 * 根据取餐码查询店铺中已支付的订单
	 * @param vercode
	 * @return
	 */
	public List<Order> selectOrderByVercode(String vercode,String shopId);
	
	/**
	 * 根据桌号查询店铺中已支付的订单
	 * @param tableNumber
	 * @return
	 */
	public List<Order> selectOrderByTableNumber(String tableNumber,String shopId);
	
	/**
	 * 修改就餐模式
	 * @param modeId
	 * @param orderId
	 */
	public void updateDistributionMode(Integer modeId, String orderId);

	/**
	 * 清除所有订单状态信息
	 * @param currentShopId
	 */
	public void clearNumber(String currentShopId);

	public List<Order> listOrderByStatus(String currentShopId, Date begin, Date end, int[] productionStatus,
			int[] orderState);

	public void updateAllowContinue(String id, boolean b);

	List<Order> selectByParentId(String parentOrderId,Integer parentOrderPayType);

	public Order findCustomerNewPackage(String currentCustomerId, String currentShopId);
	
	/**
	 * 根据时间 和指定 店铺ID 查询已完成的订单(orderSatus = 2,10,11,12)
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	//SaleReportDto selectArticleSumCountByData(String beginDate,String endDate,String brandId);
	
	/**
	 * 根据时间 和 指定 店铺 查询 已完成的订单的 菜品销售详情
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	public List<ArticleSellDto> selectShopArticleSellByDate(String beginDate,String endDate,String shopId,String sort);
	
	/**
	 * 根据时间 查询 当前品牌已完成的订单的 菜品销售详情(品牌端显示)
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<ArticleSellDto> selectBrandArticleSellByDate(String beginDate,String endDate,String order);

	public List<ArticleSellDto> selectBrandArticleSellByDateAndArticleFamilyId(String beginDate, String endDate,
			String articleFamilyId,String sort);
	/**
	 * 根据时间,指定店铺，指定菜品分类，查询已完成订单的销售详情
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @param articleFamilyId
	 * @return
	 */
	public List<ArticleSellDto> selectShopArticleSellByDateAndArticleFamilyId(String beginDate, String endDate,
			String shopId, String articleFamilyId,String sort);


	/**
	 * 根据时间 和 指定 店铺 查询 已完成的订单的 菜品销售详情(店铺端显示)
	 * @param currentShopId
	 * @param beginDate
	 * @param endDate
	 * @param sort
	 * @return
	 */
	public List<ArticleSellDto> selectShopArticleByDate(String currentShopId, String beginDate, String endDate, String sort);


	/**
	 * 根据时间 和 指定 店铺  指定分类的  查询 已完成的订单的 菜品销售详情(店铺端显示)
	 * @param beginDate
	 * @param endDate
	 * @param articleFamilyId
	 * @param sort
	 * @return
	 */

	public List<ArticleSellDto> selectShopArticleByDateAndArcticleFamilyId(String beginDate, String endDate,String shopId,
			String articleFamilyId, String sort);


	/**
	 * 根据时间查询已消费订单的订单数目和订单总额
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public OrderPayDto selectBytimeAndState(String beginDate, String endDate,String brandId);


	/**
	 * 比较订单的店铺id 和 二维码的店铺id
	 * @param orderId 订单号
	 * @param shopId 二维码的店铺id
	 * @return 相等返回true 返回返回false
     */
	Boolean checkShop(String orderId,String shopId);
	
	/**
	 * 获取品牌菜品的销售数量
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public brandArticleReportDto callBrandArticleNum(String beginDate, String endDate,String brandId,String brandName);

	
	/**
	 * 获取店铺菜品的销售数据
     * 2016-11-3
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<ShopArticleReportDto> callShopArticleDetails(String beginDate, String endDate,String brandId,List<ShopDetail> shopDetails);

	/**
	 * 获取店铺菜品的销售数据
	 * 2018-11-20
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<ShopArticleReportDto> callShopArticleDetailsNew(String beginDate, String endDate,String brandId,List<ShopDetail> shopDetails);
	
	/**
	 * 根据时间 查询 当前品牌已完成的订单的 菜品分类销售详情(品牌端显示)
	 * @param beginDate
	 * @param endDate
	 * @return
	 */

	public List<ArticleSellDto> selectBrandArticleSellByDateAndFamilyId(String currentBrandId, String beginDate,
			String endDate, String sort);
	
	/**
	 * 根据时间 查询 当前品牌已完成的订单的 菜品销售详情(品牌端显示)
	 * @param beginDate
	 * @param endDate
	 * @return
	 */

	public List<ArticleSellDto> selectBrandArticleSellByDateAndId(String brandId ,String beginDate, String endDate, String sort);

	

	/**
	 * 查询订单数目和订单金额
	 * @param beginDate
	 * @param endDate
	 * @param brandName
     * @param shopDetails
	 * @return
	 */

	public Map<String,Object> callMoneyAndNumByDate(String beginDate, String endDate,String brandId, String brandName, List<ShopDetail> shopDetails);





	/**
	 * 根据时间 查询 当前选择店铺已完成的订单的 菜品分类销售详情(品牌端显示)
	 * @param beginDate
	 * @param endDate
	 * @return
	 */


	public List<ArticleSellDto> selectShopArticleSellByDateAndFamilyId(String beginDate, String endDate, String shopId,
			String sort);


	/**
	 * 根据时间 查询 选中店铺已完成的订单的 菜品销售详情(品牌端显示)
	 * @param beginDate
	 * @param endDate
	 * @return
	 */

	public List<ArticleSellDto> selectShopArticleSellByDateAndId(String beginDate, String endDate, String shopId,
			String sort);

//	public List<Order> selectListByTime(String beginDate, String endDate, String shopId);
public List<Order> callListByTime(String beginDate, String endDate, String shopId,String customerId);

	//查询订单的详细信息(客户和菜品以及菜品信息分类 )

    /**
     * 2016-11-06
     * 新增查看微信支付单号
     * @param orderId
     * @return
     */
	public Order selectOrderDetails(String orderId);

	Boolean setOrderPrintFail(String orderId);

	public List<ArticleSellDto> selectArticleFamilyByBrandAndFamilyName(String currentBrandId, String beginDate,
			String endDate, String selectValue);

	//查询品牌所有已消费的订单
	public List<Order> selectListBybrandId(String beginDate, String endDate, String currentBrandId, Integer type);

	//查询店铺所有的已消费的订单
	public  List<Order> selectListByShopId(String beginDate,String endDate,String shopId);


	//查询订单关联评论的内容
	public List<Order> selectAppraiseByShopId(String beginDate, String endDate, String shopId);

	void autoRefundMoney();

	/**
	 * 检查此订单的菜品是否有库存
	 * @param orderId 订单号
	 * @return
     */
	Result checkArticleCount(String orderId);


	/**
	 * 出单时更新库存
	 * @param order
	 * @return
     */
	Boolean updateStock(Order order) throws AppException;


	/**
	 * 出单时还原库存
	 * @param order
	 * @return
	 */
	Boolean addStock(Order order) throws AppException;

	List<Map<String, Object>> printTotal(String shopId,String beginDate,String endDate);


    /**
     * 打印厨房小票
     *
     * @param oid
     * @return
     */
    List<Map<String, Object>>  printKitchenReceipt(String oid);

    /**
     * 获取所有桌号加菜列表
     *
     * @param shopId
     * @return
     */
    List<Order> getTableNumberAll(String shopId);

    List<OrderArticleDto> selectOrderArticle(String currentBrandId,String beginDate,String endDate);

    /**
     * 查询品牌菜品销售  用于中间数据库
     *
     * @param currentBrandId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<String,Object>> selectBrandArticleSellList(String currentBrandId, String beginDate, String endDate);

    /**
     * 查询店铺菜品销售 用于中间数据库
     *
     * @param currentBrandId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Map<String,Object>> selectShopArticleSellList(String currentBrandId, String beginDate, String endDate);

    /**
     * 查询订单详情 用于中间数据库
     *
     * @param beginDate
     * @param endDate
     * @param currentBrandId
     * @return
     */
    List<Order> selectListByTimeAndBrandId(String currentBrandId,String beginDate, String endDate);


    /**
     * 查询所有的已经消费的订单
     * @param currentBrandId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Order> selectAllAlreadyConsumed(String currentBrandId, String beginDate, String endDate);


	/**
	 * 根据订单状态和生产状态查询指定店铺的订单(不包含外卖)
	 * @param shopId
	 * @param orderStates
	 * @param productionStates
	 * @return
	 */
	List<Order> selectByOrderSatesAndProductionStates(String shopId,String[] orderStates,String[] productionStates);

	/**
	 * 根据订单状态和生产状态查询指定店铺的订单(包含外卖)
	 * @param shopId
	 * @param orderStates
	 * @param productionStates
	 * @return
	 */
	List<Order> selectByOrderSatesAndProductionStatesTakeout(String shopId, String[] orderStates,String[] productionStates);
	Order payOrderModeFive(String orderId);

	Order payOrderWXModeFive(String orderId);

	Order payPrice(BigDecimal factMoney, String orderId);

	void useRedPrice(BigDecimal factMoney,String orderId);

    List<Order> selectExceptionOrderListBybrandId(String beginDate, String endDate, String currentBrandId);

    List<Order> selectHasPayListOrderByBrandId(String beginDate, String endDate, String currentBrandId);

    /**
     * 查询所有订单和该订单下的订单项  (用于报表的异常订单项中查询订单项是否和订单的金额一致 也就是看有没有订单项丢失)
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Order> selectHasPayOrderPayMentItemListBybrandId(String beginDate, String endDate,String brandId);

    void updateOrderChild(String orderId);

	public boolean cancelExceptionOrder(String orderId);

    /**
     * 查询所有已提交单位支付的订单
     * @param currentBrandId
     * @param s
     * @param s1
     * @return
     */
    List<Order> selectNeedCacelOrderList(String currentBrandId, String s, String s1);


	/**
	 * 根据订单获取订单信息
	 *
	 * @return
	 */
	Order getOrderDetail(String orderId);

	/**
	 * 获取员工订单
	 *
	 * @return
	 */
	List<Order> getOrderByEmployee(String employeeId, String shopId);

	JSONResult createOrderByEmployee(Order order) throws AppException;

	Order lastOrderByCustomer(String customerId,String shopId,String tableNumber);

	Order lastOrderByCustomerGroupId(String customerId,String shopId,String groupId,String tableNumber);

	Order getLastOrderBySevenMode(String customerId);

    public boolean cancelWXPayOrder(String orderId);


	/**
	 * 查找该桌位最新的未消费的已选择支付宝支付的订单
	 * @param tableNumber
	 * @return
	 */
	Order getLastOrderByTableNumber(String tableNumber,String shopId);

	/**
	 * 查找该桌位最新的未消费的已选择支付宝支付的订单
	 * @param tableNumber
	 * @return
	 */
	Order selectOrderByTableNumberNoPay(String tableNumber,String shopId);

	/**
	 * 返回子订单的菜品项
	 * @param orderId
	 * @return
	 */
	List<Order> getChildItem(String orderId);

	Result updateOrderItem(String orderId,Integer count,String orderItemId,Integer type);

	void refundArticle(Order order);

	boolean checkOrder(Order order);

	JSONResult refundArticleMsg(Order order);

	void updateArticle(Order order);


    /**
     * 查询已完成订单  ---级对应的订单的菜品-
     * @param beginDate
     * @param endDate
     * @param currentBrandId
     * @return
     */
    List<Order> selectOrderListItemByBrandId(String beginDate, String endDate, String currentBrandId);

    /**
     * 查询该订单所有的子订单（包含已取消的）
     * @param orderId
     * @return
     */
    List<Order> selectListByParentId(String orderId);

    List<Order> selectHoufuOrderList(String beginDate, String endDate, String currentBrandId);
	/**
     * 微信端查询:以流水号查询订单详情
     * @param
     * @return
     */
    public List<Order> selectWXOrderItems(Map<String, Object> map);

    /**
     * 会员管理
     * yjuany 一个用户有多个订单
     * @param customerId
     * @return
     */
	public List<Order> getCustomerOrderList(String customerId,String beginDate,String endDate);

	/**
	 * 用户已消费记录总数
	 * @param customerId
	 * @return
     */
	Integer selectByCustomerCount(String customerId);
	
	public List<Order> selectOrderByOrderIds(Map<String, Object> orderIds);

	/**
	 * 返回未支付的订单的支付项
	 */
	Result refundPaymentByUnfinishedOrder(String orderId);

	List<Map<String, Object>> refundOrderPrintReceipt(Order refundOrder);
	
	List<Map<String, Object>> refundOrderPrintKitChen(Order refundOrder);

    List<Order> selectHasPayNoChangeStatusByBrandId(String brandId);

    /**
     * 查询品牌时间端内的分数
     * @param brandId
     * @param beginDate
     * @param endDate
     * @return
     */
    Double selectAppraiseBybrandId(String brandId, Date beginDate, Date endDate);

    /**
     * 查询店铺时间段内的订单总额
     * @param shopId
     * @param beginDate
     * @param endDate
     * @return
     */
    BigDecimal selectOrderMoneyByShopIdAndTime(String shopId, Date beginDate, Date endDate);

    /**
     * 查询店铺时间段内的分数
     * @param shopId
     * @param beginDate
     * @param endDate
     * @return
     */
    Double selectAppraiseByshopId(String shopId, Date beginDate, Date endDate);

    List<Order> selectOrderHistoryList(String id, Date dateEnd);

    List<Order> selectListsmsByShopId(Date begin, Date end, String id);

	JSONResult refundItem(Order order);

	JSONResult afterPay(String orderId,String couponId,BigDecimal price,BigDecimal pay,BigDecimal waitMoney,Integer payMode, String customerId);

	Order getCustomerLastOrder(String customerId);

	JSONResult confirmOrderPos(String orderId);

	BigDecimal selectPayBefore(String orderId);

	List<Order> getTodayFinishOrder(String shopId,String beginTime,String endTime);

	List<String []>  getThirdData(List<Order> orderList,int size,String brandSign);

	String fixedRefund(String brandId,String shopId,int total,int refund,String transaction_id,String mchid,String id);

    List<OrderItem> selectListByShopIdAndTime(String zuoriDay, String id);

    List<OrderItem> selectCustomerListByShopIdAndTime(String zuoriDay, String id);

	void alipayRefund(String orderId,BigDecimal refundTotal);

	List<Order> selectByOrderSatesAndNoPay(String shopId);

    /**
     * 查询第三方接口嫩绿茶需要的订单数据
     * @param beginDate
     * @param endDate
     * @param brandId
     * @return
     */
    List<Order> selectBaseToThirdList(String brandId,String beginDate, String endDate);

    List<Order> selectBaseToThirdListByShopId(String shopId,String beginDate,String endDate);

    /**
     *
     * @param brandId
     * @param beginDate
     * @param endDate kc需要的数据
     * @return
     */
    List<Order> selectBaseToKCList(String brandId, String beginDate, String endDate);

    List<Order> selectBaseToKCListByShopId(String shopId,String beginDate,String endDate);

	void changeOrderMode(String orderId);



    Order posPayOrder(String orderId, Integer payMode, String couponId, BigDecimal payValue, BigDecimal giveChange, BigDecimal remainValue, BigDecimal couponValue);

    /**
     * 查询营业报表月报表
     * @param selectMap
     * @return
     */
    List<Order> selectMonthIncomeDto(Map<String, Object> selectMap);

	Order colseOrder(String orderId);

    List<Map<String, Object>> reminder(String orderItemIds, String orderId);

	List<ShopIncomeDto> callProcDayAllOrderItem(Map<String, Object> selectMap);

	List<ShopIncomeDto> callProcDayAllOrderPayMent(Map<String, Object> selectMap);

    //修复加菜时间过后 任然允许加菜的bug
    void fixErrorOrder();

	//修复加菜时间过后 任然允许加菜的bug
	void fixErrorGroup();

	Order customerByOrderForMyPage(String customerId, String shopId);

	List<RefundArticleOrder> addRefundArticleDto(String beginDate, String endDate, String shopId);

	List<Map<String, Object>> selectMealServiceSales(Map<String, Object> selectMap);

	List<Map<String, Object>> badAppraisePrintOrder(String orderId);

	/**
	 * yz
	 * 2017-07-27
	 * 测试获取店铺 (用户消费笔数 -- 折扣比率 -- ==相关的数据
	 * 本次获取店铺数据为 鲁肉范 -- 田林路店
	 * )
	 * @param beginDate
	 * @param endDate
	 * @param currentShopId
	 * @return
	 */
	Summarry selctSummaryShopData(String beginDate, String endDate, String currentShopId);

	/**
	 *  * yz
	 * 2017-07-28
	 * 测试获取品牌 (用户消费笔数 -- 折扣比率 -- ==相关的数据
	 * 本次获取品牌数据为 嫩绿茶
	 * )
	 * @param beginDate
	 * @param endDate
	 * @param currentBrandId
	 * @return

	 */
	Summarry selctSummaryBrandData(String beginDate, String endDate, String currentBrandId);

	List<Map<String, String>> selectCustomerOrderCount(List<String> customerIds);

	JSONResult afterPayShareBenefits(String orderId);

	List<Order> selectHasPayNoChangeStatus(String shopId, Date dateBegin, Date dateEnd);

	/**
	 * yz 2017-08-15 范围内新增用户的订单
	 * @param shopId
	 * @param todayBegin
	 * @param todayEnd
	 * @return
	 */
	List<Order> selectNewCustomerOrderByShopIdAndTime(String shopId, Date todayBegin, Date todayEnd);

	/**
	 * yz 2017-08-15 范围内查询 回头用户的消费次数
	 * @param shopId
	 * @param todayBegin
	 * @param todayEnd
	 * @return
	 */
	List<BackCustomerDto> selectBackCustomerByShopIdAndTime(String shopId, Date todayBegin, Date todayEnd);

	/**
	 * yz 2017-08-15 查询店铺已完成订单
	 * @param shopId
	 * @param todayBegin
	 * @param todayEnd
	 * @return
	 */
	List<Order> selectCompleteByShopIdAndTime(String shopId, Date todayBegin, Date todayEnd);

    /**
     * 	【Pos 2.0】
     * 	本地 Pos 订单上传接口
     * 	lmx			2017年6月20日 19:56:21
     * @param orderList
     */
    void uploadLocalPosOrderList(List<Map<String, Object>> orderList);

	/**
	 * 用户最近一把有效订单
	 * @param customerId
	 * @return
	 */
	Order selectAfterValidOrderByCustomerId(String customerId);

	Order posDiscount(String orderId, BigDecimal discount, List<OrderItem> orderItems, BigDecimal eraseMoney, BigDecimal noDiscountMoney, Integer type, BigDecimal orderPosDiscountMoney);

	/**
	 * 查询每个店铺的交易笔数
	 * @param currentBrandId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<OrderNumDto> selectOrderNumByTimeAndBrandId(String currentBrandId, String beginDate, String endDate);


	List<Map<String,Object>> callBossAppOrdrReport(String brandId, List<ShopDetail> shopDetailList, String beginDate, String endDate);

	/**
	 * Pos 2.0 数据同步方法，根据 orderId 查询订单信息
	 * @param orderId
	 * @return
	 */
	Order posSyncSelectById(String orderId);

	/**
	 * 根据 店铺ID 和 用户ID 查询此用户在该店消费订单数量
	 * pos2.0 使用
	 * @param shopId
	 * @param customerId
	 * @return
	 */
	Integer selectCompleteOrderCount(String shopId, String customerId);

	/**
	 * 根据 订单创建时间 获取店铺未取消的订单
	 * @param shopId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<String> posSelectNotCancelledOrdersIdByDate(String shopId, String beginDate, String endDate);

	Order selectBySerialNumber(String serialNumber);

	void sendPosNewOrder(String shopId, Order order);


	/**
	 * 查询微信支付金额
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ShopIncomeDto selectWechatMoney(String beginDate,String endDate,List<String> shopIdlist);

	/**
	 * 下载微信对账单
	 * @param beginDate
	 * @param endDate
	 * @param shopId
	 * @return
	 */
	List<WeChatBill> uploadWeChatBill(String beginDate,String endDate, String shopId);

	public List<Order> selectLaifuShi();


	public Object getEnableTicketOrder(String customerId);

	public Order selectOrderInfo(String orderId);

    brandArticleReportDto callBrandArticleNumNew(String beginDate, String endDate, String brandId, String brandName);

	/**
	 * 获取日期内的赠菜数量
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Integer selectGrantCountByDate(String beginDate, String endDate);

    /**
     * 查询营业报表的支付项数据
     * @param selectMap
     * @return
     */
    List<OrderPaymentItem> selectBusinessReport(Map<String, Object> selectMap);
}

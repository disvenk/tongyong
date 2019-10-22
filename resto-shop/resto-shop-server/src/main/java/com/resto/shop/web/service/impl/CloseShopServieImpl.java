package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.api.appraise.service.NewAppraiseNewService;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.brand.core.util.*;
import com.resto.brand.web.dto.ArticleTopDto;
import com.resto.brand.web.dto.BackCustomerDto;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.*;
import com.resto.shop.web.datasource.DataSourceContextHolder;
import com.resto.shop.web.dto.UnderLineOrderDto;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.DateTimeUtils;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.resto.brand.core.util.OrderCountUtils.formatDouble;
import static com.resto.brand.core.util.OrderCountUtils.getOrderMoney;
@SuppressWarnings("ALL")
/**
 * @author Resto
 * @date 17/10/19 上午11:45
 */
@Component
@Service
@Log4j
public class CloseShopServieImpl implements CloseShopService{


	@Resource
	private OrderService orderService;

	@Resource
	private CustomerService customerService;

	@Resource
	private WechatConfigService wechatConfigService;

	@Resource
	private BrandService brandService;

	@Resource
	private ShopDetailService shopDetailService;

	@Resource
	private OrderItemService orderItemService;

	@Resource
	private WetherService wetherService;

	@Resource
	private OffLineOrderService offLineOrderService;

	@Resource
	private NewAppraiseService newAppraiseService;

	@Resource
	private NewAppraiseNewService newAppraiseNewService;

	@Resource
	private ChargeOrderService chargeOrderService;

	@Resource
	private ArticleTopService articleTopService;

	@Resource
	private DayDataMessageService dayDataMessageService;

	@Resource
	private DayAppraiseMessageService dayAppraiseMessageService;

	@Resource
	private SmsLogService smsLogService;

	@Resource
	private GoodTopService goodTopService;

	@Resource
	private BadTopService badTopService;

	@Resource
	private BrandSettingService brandSettingService;

	@Autowired
	private WeChatService weChatService;

	@Autowired
	private OperationLogService operationLogService;

	@Override
	public Boolean cleanShopOrder(ShopDetail shopDetail, OffLineOrder offLineOrder, Brand brand,String operator,String dailyLogId) {
		Long beginTime = System.currentTimeMillis();
		//查询结店时选择的日期
		Date cleanDate = offLineOrder.getCreateTime();
		//获取天气数据
		Wether wether = getWether(shopDetail,cleanDate);
		//第一版短信数据通过短信或和微信推送发送发送
		sendMessageByFirst(brand,shopDetail,cleanDate,offLineOrder, dailyLogId);
		//记录发送短信操作日志
		operationLogService.insertMessageLog(operator,new Date(),shopDetail,brand);
		//第二版短信内容由于模板原因无法发送短信 将数据存在数据库
		insertDateData(shopDetail,offLineOrder,wether,brand,cleanDate);
		return true;

	}

	@Override
	public Boolean insertOffLineOrder(String brandId, String shopId, OffLineOrder offLineOrder) {
		Boolean flag = false;
		Date cleanDate = offLineOrder.getCreateTime();
		OffLineOrder offLineOrder1 = offLineOrderService.selectByTimeSourceAndShopId(OfflineOrderSource.OFFLINE_POS, shopId, DateUtil.getDateBegin(cleanDate), DateUtil.getDateEnd(cleanDate));
		if (null != offLineOrder1) {
			offLineOrder1.setState(Common.NO);
			offLineOrderService.update(offLineOrder1);
		}
		offLineOrder.setCreateTime(cleanDate);
		offLineOrder.setCreateDate(cleanDate);
		offLineOrder.setId(ApplicationUtils.randomUUID());
		offLineOrder.setState(Common.YES);
		offLineOrder.setResource(OfflineOrderSource.OFFLINE_POS);
		offLineOrder.setBrandId(brandId);
		offLineOrder.setShopDetailId(shopId);
		int temp = offLineOrderService.insert(offLineOrder);
		if(temp > 0){
			flag = true;
		}
		return flag;
	}

	private void sendMessageByFirst(Brand brand,ShopDetail shopDetail,Date cleanDate,OffLineOrder offLineOrder ,String dailyLogId) {
		WechatConfig wechatConfig = wechatConfigService.selectByBrandId(brand.getId());
		BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
		//短信第一版用来发日结短信
		Map<String, String> dayMapByFirstEdtion = querryDateDataByFirstEdtion(shopDetail,cleanDate,brandSetting.getAppraiseEdition(),dailyLogId);
		//3发短信推送/微信推送
		pushMessageByFirstEdtion(dayMapByFirstEdtion, shopDetail, wechatConfig,brand);
		//3判断是否需要发送旬短信
		int temp = DateUtil.getEarlyMidLateEnd(cleanDate);
		switch (temp){
			case DayMessageType.DAY_TYPE:
				//第一版旬结短信
				Map<String, String> xunMapByFirstEdtion = querryXunDataByFirstEditon(shopDetail,cleanDate,brandSetting.getAppraiseEdition());
				pushMessageByFirstEdtion(xunMapByFirstEdtion, shopDetail, wechatConfig, brand);
				break;
			case DayMessageType.XUN_TYPE:
				Map<String, String> xunMapByFirstEdtion2 = querryXunDataByFirstEditon(shopDetail,cleanDate,brandSetting.getAppraiseEdition());
				pushMessageByFirstEdtion(xunMapByFirstEdtion2, shopDetail, wechatConfig, brand);
				break;
			case DayMessageType.MONTH_TYPE:
				Map<String, String> xunMapByFirstEdtion3 = querryXunDataByFirstEditon(shopDetail,cleanDate,brandSetting.getAppraiseEdition());
				pushMessageByFirstEdtion(xunMapByFirstEdtion3, shopDetail, wechatConfig, brand);
				Map<String, String> monthMapByFirstEdtion = querryMonthDataByFirstEditon(shopDetail, offLineOrder,cleanDate,brandSetting.getAppraiseEdition());
				pushMessageByFirstEdtion(monthMapByFirstEdtion, shopDetail, wechatConfig, brand);
				break;
			default:
				break;
		}
	}

    /**
     * 获取到天气
     * @param shopDetail
     * @param cleanDate
     * @return
     */
	private Wether getWether(ShopDetail shopDetail,Date cleanDate) {
		Wether wether = wetherService.selectDateAndShopId(shopDetail.getId(), DateUtil.formatDate(cleanDate,"yyyy-MM-dd"));
		if(wether == null){
			//说明没有调用定时任务 ---
			//手动去查
			JSONObject jsonObject = AliWetherUtil.getWetherGps(shopDetail.getLatitude(),shopDetail.getLongitude());
			if(jsonObject == null){
				wether = new Wether();
				wether.setDayWeather("---");
				wether.setDayTemperature(-1);
				wether.setWeekady(-1);
			}else {
				wether = new Wether();
				wether.setDayWeather(jsonObject.getString("day_weather"));
				wether.setDayTemperature(jsonObject.getInteger("day_air_temperature"));
				wether.setWeekady(jsonObject.getInteger("weekday"));
			}
		}
		return  wether;

	}


	/**
	 * 第一版短信 日结数据封装
	 * @param shopDetail
	 * @param offLineOrder
	 * @return
	 */
	private Map<String,String> querryDateDataByFirstEdtion(ShopDetail shopDetail,Date cleanDate,Integer appraiseEdition,String dailyLogId) {

		BrandSetting brandSetting = brandSettingService.selectByBrandId(shopDetail.getBrandId());

		//数据库查询时候需要的时间begin  注意要与下面时间的比较的日期有区分-----------------

		//----1.定义时间(指定日期的开始时间)---
		Date todayBegin = DateUtil.getDateBegin(cleanDate);
		Date todayEnd = DateUtil.getDateEnd(cleanDate);

		//指定日期本月的开始时间 本月结束时间
		Date monthBegin = DateUtil.beginOfMonth(cleanDate);
		Date monthEnd = todayEnd;
		//定义本旬的开始时间和结束时间
		Date xunBegin = getXunBegin(cleanDate,monthBegin);
		Date xunEnd = todayEnd;
		UnderLineOrderDto dto = offLineOrderService.selectDateAndMonthByShopId(todayBegin,todayEnd,monthBegin,monthEnd,shopDetail.getId());

		List<Order> newCustomerOrders = orderService.selectNewCustomerOrderByShopIdAndTime(shopDetail.getId(), todayBegin, todayEnd);

		//新用户
		int newCustomerOrderNum = (int)newCustomerOrders.stream().count();
		 BigDecimal newCustomerOrderTotal = sumList(orderListToBigDecimalList(newCustomerOrders));

		List<Order> newShareList = orderListFilterNewShareCustomer(newCustomerOrders);
		List<Order> newNormalList = subOrderList(newCustomerOrders,newShareList);

		//新分享用户
	  	int	newShareCustomerOrderNum = (int)newShareList.stream().count();
		BigDecimal newShareCustomerOrderTotal = sumList(orderListToBigDecimalList(newShareList));

		//新自然用户
		int newNormalCustomerOrderNum = (int)newNormalList.stream().count();
		BigDecimal newNormalCustomerOrderTotal = sumList(orderListToBigDecimalList(newNormalList));


		//查询回头用户的
		List<BackCustomerDto> backCustomerDtos = orderService.selectBackCustomerByShopIdAndTime(shopDetail.getId(), todayBegin, todayEnd);

		//回头用户
		Set<String> backCustomerId = orderListToBack(backCustomerDtos);
		//二次回头用户
		Set<String> backTwoCustomerId = orderListToBackTwo(backCustomerDtos);
		//多次回头用户
		Set<String> backTwoMoreCustomerId = orderListToBackTwoMore(backCustomerDtos);

		List<Order> todayOrders = orderService.selectCompleteByShopIdAndTime(shopDetail.getId(), todayBegin, todayEnd);

		List<Order> backList = orderListFilter(todayOrders,backCustomerId);
		List<Order> backTwoList = orderListFilter(todayOrders,backTwoCustomerId);
		List<Order> backTwoMoreList = orderListFilter(todayOrders,backTwoMoreCustomerId);

		//查询当日已消费的订单
		//回头用户的订单总数
		int backCustomerOrderNum = (int)backList.stream().count();
		//二次回头用户的订单总数
		int backTwoCustomerOrderNum = (int)backTwoList.stream().count();
		//多次回头用户的订单总数
		int backTwoMoreCustomerOderNum = (int)backTwoMoreList.stream().count();
		//回头用户的订单总额
		BigDecimal backCustomerOrderTotal =sumList(orderListToBigDecimalList(backList));
		//二次回头用户的订单总额
		BigDecimal backTwoCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoList));
		//多次回头用户的订单总额
		BigDecimal backTwoMoreCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoMoreList));

		//本月订单
		List<Order> monthOrders = orderService.selectCompleteByShopIdAndTime(shopDetail.getId(),monthBegin, monthEnd);

		//2定义resto订单
		//本日resto订单总数 新增+回头
		int todayRestoCount = backCustomerOrderNum + newCustomerOrderNum;
		//本日resto订单总额
		BigDecimal todayRestoTotal = sumOrderMoney(todayOrders);

		//本月resto订单总额
		BigDecimal monthRestoTotal = sumOrderMoney(monthOrders);

		List<OrderPaymentItem> paymentItems = orderListToPaymentItemList(todayOrders);
        //得到订单折扣
        BigDecimal orderPosDiscount = sumOrderPosDiscount(todayOrders);
        //免单
        BigDecimal exemptionMoney = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.EXEMPTION_PAY));
		//红包
		BigDecimal redPackTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.ACCOUNT_PAY));
		//优惠券
		BigDecimal couponTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.COUPON_PAY));
		//充值赠送
		BigDecimal chargeReturn = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.REWARD_PAY));
        //等位红包
        BigDecimal waitNumberIncome = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.WAIT_MONEY));
        //积分
        BigDecimal integralPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.INTEGRAL_PAY));
        //代金券
        BigDecimal cashCouponPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.CASH_COUPIN_PAY));
		//定义折扣合计
		BigDecimal discountTotal = orderPosDiscount.add(exemptionMoney).add(redPackTotal).add(couponTotal).add(chargeReturn)
                                    .add(waitNumberIncome).add(integralPayment).add(cashCouponPayment);

		//本日用户消费比率 R+线下+外卖
		//到店总笔数 线上+线下
		double dmax = dto.getTodayEnterCount() + todayRestoCount;
		Boolean temp2 = dmax!=0;

		String discountRatio = todayRestoTotal.add(discountTotal).compareTo(BigDecimal.ZERO) > 0 ? discountTotal.divide(todayRestoTotal.add(discountTotal), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString():"";
		//本日用户消费比率
		String todayCustomerRatio = temp2 ? formatDouble((todayRestoCount / dmax) * 100):"";
		//回头用户消费比率
		String todayBackCustomerRatio = temp2 ? formatDouble((backCustomerOrderNum / dmax) * 100):"";
		//新增用户比率
		String todayNewCustomerRatio = temp2 ? formatDouble((newCustomerOrderNum / dmax) * 100):"";

		int fiveStar=0;
		int fourStar=0;
		int oneToThreeStar=0;
		String todaySatisfaction=null;
		String theTenDaySatisfaction=null;
		String monthSatisfaction=null;
		//单独查询评价和分数
		if(appraiseEdition==1){
			List<AppraiseNew> todayAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(todayBegin), DateTimeUtils.dateToString(todayEnd));

			List<AppraiseNew> xunAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(xunBegin), DateTimeUtils.dateToString(xunEnd));

			List<AppraiseNew> monthAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(monthBegin), DateTimeUtils.dateToString(monthEnd));

			//五星
			fiveStar = appraiseNewListFilterFiveStar(todayAppraises);
			//四星
			fourStar = appraiseNewListFilterFourStar(todayAppraises);
			//3星-1星
			oneToThreeStar = appraiseNewListFilterOneToThreeStar(todayAppraises);

			//当日所有评价的总分数
			double dayAppraiseSum = appraiseNewListPrice(todayAppraises);
			//上旬所有评价的总分数
			double xunAppraiseSum = appraiseNewListPrice(xunAppraises);
			//本月所有评价的总分数
			double monthAppraiseSum = appraiseNewListPrice(monthAppraises);


			//当日评价的总单数
			int dayAppraiseNum = (int)todayAppraises.stream().count();
			//本旬评价的总单数
			int xunAppraiseNum = (int)xunAppraises.stream().count();
			//本月评价的单数
			int monthAppraiseNum = (int)monthAppraises.stream().count();
			//3定义满意度
			//本日满意度
			todaySatisfaction = dayAppraiseNum != 0? formatDouble(dayAppraiseSum / dayAppraiseNum) :"";
			//本旬满意度
			theTenDaySatisfaction = xunAppraiseNum != 0? formatDouble(xunAppraiseSum / xunAppraiseNum) :"";
			//本月满意度
			monthSatisfaction = monthAppraiseNum != 0? formatDouble(monthAppraiseSum / monthAppraiseNum) :"";

		}else{
			List<Appraise> todayAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(todayBegin), DateTimeUtils.dateToString(todayEnd));

			List<Appraise> xunAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(xunBegin),DateTimeUtils.dateToString(xunEnd));

			List<Appraise> monthAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(monthBegin), DateTimeUtils.dateToString(monthEnd));

			//五星
			fiveStar = appraiseListFilterFiveStar(todayAppraises);
			//四星
			fourStar = appraiseListFilterFourStar(todayAppraises);
			//3星-1星
			oneToThreeStar = appraiseListFilterOneToThreeStar(todayAppraises);

			//当日所有评价的总分数
			double dayAppraiseSum = appraiseListPrice(todayAppraises);
			//上旬所有评价的总分数
			double xunAppraiseSum = appraiseListPrice(xunAppraises);
			//本月所有评价的总分数
			double monthAppraiseSum = appraiseListPrice(monthAppraises);
			//当日评价的总单数
			int dayAppraiseNum = (int)todayAppraises.stream().count();
			//本旬评价的总单数
			int xunAppraiseNum = (int)xunAppraises.stream().count();
			//本月评价的单数
			int monthAppraiseNum = (int)monthAppraises.stream().count();
			//3定义满意度
			//本日满意度
			todaySatisfaction = dayAppraiseNum != 0? formatDouble(dayAppraiseSum / dayAppraiseNum) :"";
			//本旬满意度
			theTenDaySatisfaction = xunAppraiseNum != 0? formatDouble(xunAppraiseSum / xunAppraiseNum) :"";
			//本月满意度
			monthSatisfaction = monthAppraiseNum != 0? formatDouble(monthAppraiseSum / monthAppraiseNum) :"";
		}

		//门店付出零用金
		BigDecimal emersionCashPay = new BigDecimal(0);
		//门店付出零找金
		BigDecimal emersionIncomePay = new BigDecimal(0);

		//获取当天店铺复核金额最后一次保存的记录
		List<PaymentReview> paymentReviewList = operationLogService.selectFloatingMoneyLastDay(cleanDate,dailyLogId);
		if(paymentReviewList != null && paymentReviewList.size() != 0 ){
			for(PaymentReview pr : paymentReviewList){
				if(pr.getPaymentModeId() == PayMode.EMERSION_CASH_PAY){
					emersionCashPay = pr.getAuditMoney();
				}else if(pr.getPaymentModeId() == PayMode.EMERSION_INCOME_PAY){
					emersionIncomePay = pr.getAuditMoney();
				}
			}
		}

		//发送本日信息 本月信息 上旬信息
		//本日信息
		StringBuilder todayContent = new StringBuilder();

        todayContent.append("{")
                .append("shopName:").append("'").append(shopDetail.getName()).append("'").append(",")
                .append("datetime:").append("'").append(DateUtil.formatDate(cleanDate, "yyyy-MM-dd HH:mm:ss")).append("'").append(",")
                //用户消费笔数  R+订单总数
                .append("customerPayCount:").append("'").append(todayRestoCount).append("'").append(",")
                //用户消费金额: (r+订单总额)
                .append("customerPayAmount:").append("'").append(todayRestoTotal).append("'").append(",")
                //新客消费比率
                .append("newCustomerPercent:").append("'").append(todayNewCustomerRatio).append("%").append("'").append(",")
                //回头消费比率 R+多次消费用户数/R+消费用户数）
                .append("userBackPercent:").append("'").append(todayBackCustomerRatio).append("%").append("'").append(",")
                //新用户消费
                .append("newCustomerPay:").append("'").append(newCustomerOrderNum).append("笔/").append(newCustomerOrderTotal).append("'").append(",")
                //回头用户消费
                .append("customerBackPay:").append("'").append(backCustomerOrderNum).append("笔/").append(backCustomerOrderTotal).append("'").append(",")
                //折扣合计:648.05（使用红包总额+使用优惠券总额+使用充值赠送总额）
                .append("discountTotal:").append("'").append(discountTotal).append("'").append(",")
                //五星评论
                .append("goodCount:").append("'").append(fiveStar).append("'").append(",")
                //本日改进意见
                .append("badCount:").append("'").append(fourStar).append("'").append(",")
                //本日差评投诉
                .append("terribleCount:").append("'").append(oneToThreeStar).append("'").append(",")
                //本日满意度
                .append("satisfied1:").append("'").append(todaySatisfaction).append("'").append(",")
                //今日外卖金额
                .append("outFoodTotal:").append("'").append(dto.getTodayOrderBooks()).append("'").append(",")
                //总营业额
                .append("totalOrderMoney:").append("'").append(dto.getTodayEnterTotal().add(todayRestoTotal).add(dto.getTodayOrderBooks())).append("'").append(",")
                //本月总额
                .append("monthTotalMoney:").append("'").append(dto.getMonthOrderBooks().add(dto.getMonthEnterTotal()).add(monthRestoTotal)).append("'").append(",");

		if(brandSetting.getOpenAudit() == 1){
			//开启复核金额
			todayContent
					//浮出零用金
					.append("emersionCashPay:").append("'").append(emersionCashPay).append("'").append(",")
					//浮出零找金
					.append("emersionIncomePay:").append("'").append(emersionIncomePay).append("'");
		}else{
			//未开启复核金额
			todayContent
					//折扣比率2.7%（折扣合计/(堂吃消费总额＋折扣合计)
					.append("discountPercent:").append("'").append(discountRatio).append("%").append("'");
		}
		todayContent.append("}");
		//封装微信推送文本
		StringBuilder sb = new StringBuilder();
		sb.append("店铺名称:").append(shopDetail.getName()).append("\n")
        .append("时间:").append(DateUtil.formatDate(cleanDate, "yyyy-MM-dd HH:mm:ss")).append("\n")
        .append("到店总笔数:").append(dto.getTodayEnterCount() + todayRestoCount).append("\n")
        .append("到店消费总额:").append(dto.getTodayEnterTotal().add(todayRestoTotal)).append("\n")
        .append("---------------------").append("\n")
        .append("用户消费比数:").append(todayRestoCount).append("\n")
        .append("用户消费金额").append(todayRestoTotal).append("\n")
        .append("---------------------").append("\n")
        .append("用户消费比率:").append(todayCustomerRatio).append("%").append("\n")
        .append("回头消费比率:").append(todayBackCustomerRatio).append("%").append("\n")
        .append("新增用户比率:").append(todayNewCustomerRatio).append("%").append("\n")
        .append("---------------------").append("\n")
        .append("新用户消费:").append(newCustomerOrderNum).append("笔/").append(newCustomerOrderTotal).append("\n")
        .append("其中自然用户:").append(newNormalCustomerOrderNum).append("笔/").append(newNormalCustomerOrderTotal).append("\n")
        .append("其中分享用户:").append(newShareCustomerOrderNum).append("笔/").append(newShareCustomerOrderTotal).append("\n")
        .append("回头用户消费:").append(backCustomerOrderNum).append("笔/").append(backCustomerOrderTotal).append("\n")
        .append("二次回头用户:").append(backTwoCustomerOrderNum).append("笔/").append(backTwoCustomerOrderTotal).append("\n")
        .append("多次回头用户:").append(backTwoMoreCustomerOderNum).append("笔/").append(backTwoMoreCustomerOrderTotal).append("\n")
        .append("---------------------").append("\n")
        .append("折扣合计:").append(discountTotal).append("\n")
        .append("POS折扣:").append(orderPosDiscount).append("\n")
        .append("免单:").append(exemptionMoney).append("\n")
        .append("红包:").append(redPackTotal).append("\n")
        .append("优惠券:").append(couponTotal).append("\n")
        .append("充值赠送:").append(chargeReturn).append("\n")
        .append("等位红包:").append(waitNumberIncome).append("\n")
        .append("积分支付:").append(integralPayment).append("\n")
        .append("代金券:").append(cashCouponPayment).append("\n")
        .append("折扣比率").append(discountRatio).append("\n");
		if(brandSetting.getOpenAudit() == 1){
			//开启复核金额
			sb.append("本日浮出零用金:").append(emersionCashPay).append("\n")
            .append("本日浮出零找金:").append(emersionIncomePay).append("\n");
		}
		sb.append("---------------------").append("\n")
        .append("本日五星评论:").append(fiveStar).append("\n")
        .append("本日更改意见:").append(fourStar).append("\n")
        .append("本日差评投诉:").append(oneToThreeStar).append("\n")
        .append("本日满意度:").append(todaySatisfaction).append("\n")
        .append("本旬满意度:").append(theTenDaySatisfaction).append("\n")
        .append("本月满意度:").append(monthSatisfaction).append("\n")
        .append("---------------------").append("\n")
        .append("今日外卖金额:").append(dto.getTodayOrderBooks()).append("\n")
        .append("今日总营业额:").append(dto.getTodayEnterTotal().add(todayRestoTotal).add(dto.getTodayOrderBooks())).append("\n")
        .append("本月总额:").append(dto.getMonthOrderBooks().add(dto.getMonthEnterTotal()).add(monthRestoTotal)).append("\n");

		Map<String, String> map = new HashMap<>(128);
		map.put("sms", todayContent.toString());
		map.put("wechat", sb.toString());
		return map;
	}




	/**
	 * 第一版日结短信 xun 结数据的封装
	 * @param shopDetail
	 * @return
	 */
	private Map<String,String> querryXunDataByFirstEditon(ShopDetail shopDetail,Date cleanDate,Integer appraiseEdition) {
		//----1.定义时间---
		Date xunBegin = DateUtil.getAfterDayDate(cleanDate, -10);
		Date xunEnd = cleanDate;

		UnderLineOrderDto dto = offLineOrderService.selectXunByShopId(xunBegin,xunEnd,shopDetail.getId());

		//查询本旬新增用户的订单
		List<Order> newCustomerOrders = orderService.selectNewCustomerOrderByShopIdAndTime(shopDetail.getId(), xunBegin, xunEnd);

		//新增用户的订单总数
		int newCustomerOrderNum = (int)newCustomerOrders.stream().count();
		//新增用户的订单总额
		BigDecimal newCustomerOrderTotal = sumList(orderListToBigDecimalList(newCustomerOrders));

		List<Order> newShareList = orderListFilterNewShareCustomer(newCustomerOrders);
		List<Order> newNormalList = subOrderList(newCustomerOrders,newShareList);

		//新增分享用户的的订单总数
		int newShareCustomerOrderNum = (int)newShareList.stream().count();
		//新增分享用户的订单总额
		BigDecimal newShareCustomerOrderTotal = sumList(orderListToBigDecimalList(newShareList));
		//新增自然用户的订单总数
		int newNormalCustomerOrderNum = (int)newNormalList.stream().count();
		//新增自然用户的订单总额
		BigDecimal newNormalCustomerOrderTotal = sumList(orderListToBigDecimalList(newNormalList));

		//查询回头用户的
		List<BackCustomerDto> backCustomerDtos = orderService.selectBackCustomerByShopIdAndTime(shopDetail.getId(), xunBegin, xunEnd);
		//回头用户
		Set<String> backCustomerId = orderListToBack(backCustomerDtos);
		//二次回头用户
		Set<String> backTwoCustomerId = orderListToBackTwo(backCustomerDtos);
		//多次回头用户
		Set<String> backTwoMoreCustomerId = orderListToBackTwoMore(backCustomerDtos);

		List<Order> xunOrders = orderService.selectCompleteByShopIdAndTime(shopDetail.getId(), xunBegin, xunEnd);

		List<Order> backList = orderListFilter(xunOrders,backCustomerId);
		List<Order> backTwoList = orderListFilter(xunOrders,backTwoCustomerId);
		List<Order> backTwoMoreList = orderListFilter(xunOrders,backTwoMoreCustomerId);


		//回头用户的订单总数
		int backCustomerOrderNum = (int)backList.stream().count();
		//二次回头用户的订单总数
		int backTwoCustomerOrderNum = (int)backTwoList.stream().count();
		//多次回头用户的订单总数
		int backTwoMoreCustomerOderNum = (int)backTwoMoreList.stream().count();
		//回头用户的订单总额
		BigDecimal backCustomerOrderTotal = sumList(orderListToBigDecimalList(backList));
		//二次回头用户的订单总额
		BigDecimal backTwoCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoList));
		//多次回头用户的订单总额
		BigDecimal backTwoMoreCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoMoreList));

		int xunRestoCount = newCustomerOrderNum + backCustomerOrderNum;

		//本旬resto订单总额
		BigDecimal xunRestoTotal = sumOrderMoney(xunOrders);

		List<OrderPaymentItem> paymentItems = orderListToPaymentItemList(xunOrders);
        //得到订单折扣
        BigDecimal orderPosDiscount = sumOrderPosDiscount(xunOrders);
        //免单
        BigDecimal exemptionMoney = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.EXEMPTION_PAY));
        //红包
        BigDecimal redPackTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.ACCOUNT_PAY));
        //优惠券
        BigDecimal couponTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.COUPON_PAY));
        //充值赠送
        BigDecimal chargeReturn = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.REWARD_PAY));
        //等位红包
        BigDecimal waitNumberIncome = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.WAIT_MONEY));
        //积分
        BigDecimal integralPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.INTEGRAL_PAY));
        //代金券
        BigDecimal cashCouponPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.CASH_COUPIN_PAY));
        //定义折扣合计
        BigDecimal discountTotal = orderPosDiscount.add(exemptionMoney).add(redPackTotal).add(couponTotal).add(chargeReturn)
                .add(waitNumberIncome).add(integralPayment).add(cashCouponPayment);

		//折扣比率

		double dmax = (dto == null ? 0 : dto.getXunEnterCount()) + xunRestoCount;
		Boolean temp2 = dmax!=0;

		String discountRatio = xunRestoTotal.add(discountTotal).compareTo(BigDecimal.ZERO) > 0 ? discountTotal.divide(xunRestoTotal.add(discountTotal), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString():"";
		//本旬用户消费比率
		String xunCustomerRatio = temp2 ? formatDouble((xunRestoCount / dmax) * 100):"";
		//回头用户消费比率
		String xunBackCustomerRatio = temp2 ? formatDouble((newCustomerOrderNum / dmax) * 100):"";
		//新增用户比率
		String xunNewCustomerRatio = temp2 ? formatDouble((backCustomerOrderNum / dmax) * 100):"";

		int fiveStar=0;
		int fourStar=0;
		int oneToThreeStar=0;
		String theTenDaySatisfaction=null;
		if(appraiseEdition==1){
			//单独查询评价和分数
			List<AppraiseNew> xunAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(xunBegin), DateTimeUtils.dateToString(xunEnd));
			//五星
			fiveStar = appraiseNewListFilterFiveStar(xunAppraises);
			//四星
			fourStar = appraiseNewListFilterFourStar(xunAppraises);
			//3星-1星
			oneToThreeStar = appraiseNewListFilterOneToThreeStar(xunAppraises);
			//本旬所有评价的总分数
			double xunAppraiseSum = appraiseNewListPrice(xunAppraises);
			//本旬评价的总单数
			int xunAppraiseNum = (int)xunAppraises.stream().count();
			//3定义满意度
			//本旬满意度
			theTenDaySatisfaction = xunAppraiseNum != 0? formatDouble(xunAppraiseSum / xunAppraiseNum) :"";
		}else{
			//单独查询评价和分数
			List<Appraise> xunAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(xunBegin), DateTimeUtils.dateToString(xunEnd));
			//五星
			fiveStar = appraiseListFilterFiveStar(xunAppraises);
			//四星
			fourStar = appraiseListFilterFourStar(xunAppraises);
			//3星-1星
			oneToThreeStar = appraiseListFilterOneToThreeStar(xunAppraises);
			//本旬所有评价的总分数
			double xunAppraiseSum = appraiseListPrice(xunAppraises);
			//本旬评价的总单数
			int xunAppraiseNum = (int)xunAppraises.stream().count();
			//3定义满意度
			//本旬满意度
			theTenDaySatisfaction = xunAppraiseNum != 0? formatDouble(xunAppraiseSum / xunAppraiseNum) :"";
		}

		//查询充值
		List<ChargeOrder> chargeOrderList = chargeOrderService.selectByDateAndShopId(DateUtil.formatDate(xunBegin, "yyyy-MM-dd"), DateUtil.formatDate(xunEnd, "yyyy-MM-dd"), shopDetail.getId());
		BigDecimal xunChargeMoney = chargeOrderListToBigDeciaml(chargeOrderList);


		//查询菜品top10
		//1.查询好评的总数(旬内)
		int goodNum = articleTopService.selectSumGoodByTime(xunBegin, xunEnd, shopDetail.getId());
		//查询差评总数
		int badNum = articleTopService.selectSumBadByTime(xunBegin, xunEnd, shopDetail.getId());

		//查询好评top10
		List<ArticleTopDto> goodList = articleTopService.selectListByTimeAndGoodType(xunBegin, xunEnd, shopDetail.getId());

		//查询差评top10
		List<ArticleTopDto> badList = articleTopService.selectListByTimeAndBadType(xunBegin, xunEnd, shopDetail.getId());

		//封装微信推送文本
		StringBuilder sb = new StringBuilder();
		sb
				.append("店铺名称:").append(shopDetail.getName()).append("\n")
				.append("时间:").append(DateUtil.formatDate(cleanDate,"yyyy-MM-dd HH:mm:ss")).append("\n")
				.append("本旬总结").append("\n")
				.append("到店总笔数:").append((dto == null ? 0 : dto.getXunEnterCount()) + xunRestoCount).append("\n")
				.append("到店消费总额:").append((dto == null ? new BigDecimal(0) : dto.getXunEnterTotal()).add(xunRestoTotal)).append("\n")
				.append("---------------------").append("\n")
				.append("Resto+用户消费比数:").append(xunRestoCount).append("\n")
				.append("Resto+用户消费金额").append(xunRestoTotal).append("\n")
				.append("---------------------").append("\n")
				.append("Resto+用户消费比率:").append(xunCustomerRatio).append("%").append("\n")
				.append("Resto+回头消费比率:").append(xunBackCustomerRatio).append("%").append("\n")
				.append("Resto+新增用户比率:").append(xunNewCustomerRatio).append("%").append("\n")
				.append("---------------------").append("\n")
				.append("Resto+新用户消费:").append(newCustomerOrderNum).append("笔/").append(newCustomerOrderTotal).append("\n")
				.append("Resto+其中自然用户:").append(newNormalCustomerOrderNum).append("笔/").append(newNormalCustomerOrderTotal).append("\n")
				.append("Resto+其中分享用户:").append(newShareCustomerOrderNum).append("笔/").append(newShareCustomerOrderTotal).append("\n")
				.append("Resto+回头用户消费:").append(backCustomerOrderNum).append("笔/").append(backCustomerOrderTotal).append("\n")
				.append("Resto+二次回头用户:").append(backTwoCustomerOrderNum).append("笔/").append(backTwoCustomerOrderTotal).append("\n")
				.append("Resto+多次回头用户:").append(backTwoMoreCustomerOderNum).append("笔/").append(backTwoMoreCustomerOrderTotal).append("\n")
				.append("---------------------").append("\n")
				.append("折扣合计:").append(discountTotal).append("\n")
                .append("POS折扣:").append(orderPosDiscount).append("\n")
                .append("免单:").append(exemptionMoney).append("\n")
                .append("红包:").append(redPackTotal).append("\n")
                .append("优惠券:").append(couponTotal).append("\n")
                .append("充值赠送:").append(chargeReturn).append("\n")
                .append("等位红包:").append(waitNumberIncome).append("\n")
                .append("积分支付:").append(integralPayment).append("\n")
                .append("代金券:").append(cashCouponPayment).append("\n")
				.append("折扣比率").append(discountRatio).append("\n")
				.append("---------------------").append("\n")
				.append("本旬五星评论:").append(fiveStar).append("\n")
				.append("本旬更改意见:").append(fourStar).append("\n")
				.append("本旬差评投诉:").append(oneToThreeStar).append("\n")
				.append("本旬满意度:").append(theTenDaySatisfaction).append("\n")
				.append("---------------------").append("\n")
				.append("本旬外卖金额:").append((dto == null ? 0 : dto.getXunOrderBooks())).append("\n")
				.append("本旬实收:").append((dto == null ? new BigDecimal(0) : dto.getXunEnterTotal()).add(xunRestoTotal).add((dto == null ? new BigDecimal(0) : dto.getXunOrderBooks()))).append("\n")
				.append("本旬充值:").append(xunChargeMoney).append("\n")
				.append("---------------------").append("\n")
				.append("本旬红榜top10：").append("\n");

		//封装好评top10
		//无好评
		if (goodNum == 0) {
			sb.append("------无-----");
		} else {
			if (!goodList.isEmpty()) {
				for (int i = 0; i < goodList.size(); i++) {
					//1、27% 剁椒鱼头
					sb.append(i + 1).append(".").append(NumberUtil.getFormat(goodList.get(i).getNum(), goodNum)).append("%").append(" ").append(goodList.get(i).getName()).append("\n");
				}
			}
		}
		sb.append("本旬黑榜top10：").append("\n");
		//封装差评top10
		//无差评
		if (badNum == 0) {
			sb.append("------无-----");
		} else {
			if (!badList.isEmpty()) {
				for (int i = 0; i < badList.size(); i++) {
					//1、27% 剁椒鱼头
					sb.append(i + 1).append(".").append(NumberUtil.getFormat(badList.get(i).getNum(), badNum)).append("%").append(" ").append(badList.get(i).getName()).append("\n");
				}
			}
		}
		Map<String, String> map = new HashMap<>(128);
		map.put("wechat", sb.toString());
		return map;
	}


	/**
	 * 第一版短信 月结数据封装
	 * @param shopDetail
	 * @param offLineOrder
	 * @return
	 */
	private Map<String,String> querryMonthDataByFirstEditon(ShopDetail shopDetail, OffLineOrder offLineOrder,Date cleanDate,Integer appraiseEdition) {
		//----1.定义时间---
		Date monthBegin =DateUtil.beginOfMonth(cleanDate);
		Date monthEnd = DateUtil.getDateEnd(cleanDate);

		UnderLineOrderDto dto = offLineOrderService.selectMonthByShopId(monthBegin,monthEnd,shopDetail.getId());


		//查询本月新增用户的订单
		List<Order> newCustomerOrders = orderService.selectNewCustomerOrderByShopIdAndTime(shopDetail.getId(), monthBegin, monthEnd);

		//新增用户的订单总数
		int newCustomerOrderNum = (int)newCustomerOrders.stream().count();
		//新增用户的订单总额
		BigDecimal newCustomerOrderTotal = sumList(orderListToBigDecimalList(newCustomerOrders));

		List<Order> newShareList = orderListFilterNewShareCustomer(newCustomerOrders);
		List<Order> newNormalList = subOrderList(newCustomerOrders,newShareList);

		//新增分享用户
		int newShareCustomerOrderNum = (int)newShareList.stream().count();
		//新增分享用户的订单总额
		BigDecimal newShareCustomerOrderTotal = sumList(orderListToBigDecimalList(newShareList));
		//新增自然用户的订单总数
		int newNormalCustomerOrderNum = (int)newNormalList.stream().count();
		//新增自然用户的订单总额
		BigDecimal newNormalCustomerOrderTotal = sumList(orderListToBigDecimalList(newNormalList));


		//查询回头用户的
		List<BackCustomerDto> backCustomerDtos = orderService.selectBackCustomerByShopIdAndTime(shopDetail.getId(), monthBegin, monthEnd);
		//回头用户
		Set<String> backCustomerId = orderListToBack(backCustomerDtos);
		//二次回头用户
		Set<String> backTwoCustomerId = orderListToBackTwo(backCustomerDtos);
		//多次回头用户
		Set<String> backTwoMoreCustomerId = orderListToBackTwoMore(backCustomerDtos);

		List<Order> monthOrders = orderService.selectCompleteByShopIdAndTime(shopDetail.getId(), monthBegin, monthEnd);

		List<Order> backList = orderListFilter(monthOrders,backCustomerId);
		List<Order> backTwoList = orderListFilter(monthOrders,backTwoCustomerId);
		List<Order> backTwoMoreList = orderListFilter(monthOrders,backTwoMoreCustomerId);

		//回头用户的订单总数
		int backCustomerOrderNum = (int)backList.stream().count();
		//二次回头用户的订单总数
		int backTwoCustomerOrderNum = (int)backTwoList.stream().count();
		//多次回头用户的订单总数
		int backTwoMoreCustomerOderNum = (int)backTwoMoreList.stream().count();
		//回头用户的订单总额
		BigDecimal backCustomerOrderTotal =sumList(orderListToBigDecimalList(backList));
		//二次回头用户的订单总额
		BigDecimal backTwoCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoList));
		//多次回头用户的订单总额
		BigDecimal backTwoMoreCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoMoreList));

		//2定义resto订单
		//本月resto订单总数
		int monthRestoCount = newCustomerOrderNum + backCustomerOrderNum;

		//本月resto订单总额
		BigDecimal monthRestoTotal = sumOrderMoney(monthOrders);

		List<OrderPaymentItem> paymentItems = orderListToPaymentItemList(monthOrders);
        //得到订单折扣
        BigDecimal orderPosDiscount = sumOrderPosDiscount(monthOrders);
        //免单
        BigDecimal exemptionMoney = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.EXEMPTION_PAY));
        //红包
        BigDecimal redPackTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.ACCOUNT_PAY));
        //优惠券
        BigDecimal couponTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.COUPON_PAY));
        //充值赠送
        BigDecimal chargeReturn = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.REWARD_PAY));
        //等位红包
        BigDecimal waitNumberIncome = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.WAIT_MONEY));
        //积分
        BigDecimal integralPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.INTEGRAL_PAY));
        //代金券
        BigDecimal cashCouponPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.CASH_COUPIN_PAY));
        //定义折扣合计
        BigDecimal discountTotal = orderPosDiscount.add(exemptionMoney).add(redPackTotal).add(couponTotal).add(chargeReturn)
                .add(waitNumberIncome).add(integralPayment).add(cashCouponPayment);
		//折扣比率
		double dmax = dto.getMonthEnterCount() + monthRestoCount;
		Boolean temp2 = dmax!=0;
		String discountRatio = monthRestoTotal.add(discountTotal).compareTo(BigDecimal.ZERO) > 0 ? discountTotal.divide(monthRestoTotal.add(discountTotal), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString():"";
		//本月用户消费比率
		String monthCustomerRatio = temp2 ? formatDouble((monthRestoCount / dmax) * 100):"";
		//回头用户消费比率
		String monthBackCustomerRatio = temp2 ? formatDouble(( backCustomerOrderNum/ dmax) * 100):"";
		//新增用户比率
		String monthNewCustomerRatio = temp2 ? formatDouble((newCustomerOrderNum / dmax) * 100):"";
		int fiveStar=0;
		int fourStar=0;
		int oneToThreeStar=0;
		String monthSatisfaction=null;
		if(appraiseEdition==1){
			//单独查询评价和分数
			List<AppraiseNew> monthAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(monthBegin), DateTimeUtils.dateToString(monthEnd));

			//五星
			fiveStar = appraiseNewListFilterFiveStar(monthAppraises);
			//四星
			fourStar = appraiseNewListFilterFourStar(monthAppraises);
			//3星-1星
			oneToThreeStar = appraiseNewListFilterOneToThreeStar(monthAppraises);

			//本月所有评价的总分数
			double monthAppraiseSum = appraiseNewListPrice(monthAppraises);

			//本月评价的单数
			int monthAppraiseNum = (int)monthAppraises.stream().count();

			//本月满意度
			monthSatisfaction = monthAppraiseNum != 0?  formatDouble(monthAppraiseSum / monthAppraiseNum) :"";
		}else{
			//单独查询评价和分数
			List<Appraise> monthAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(monthBegin), DateTimeUtils.dateToString(monthEnd));

			//五星
			fiveStar = appraiseListFilterFiveStar(monthAppraises);
			//四星
			fourStar = appraiseListFilterFourStar(monthAppraises);
			//3星-1星
			oneToThreeStar = appraiseListFilterOneToThreeStar(monthAppraises);

			//本月所有评价的总分数
			double monthAppraiseSum = appraiseListPrice(monthAppraises);

			//本月评价的单数
			int monthAppraiseNum = (int)monthAppraises.stream().count();

			//本月满意度
			monthSatisfaction = monthAppraiseNum != 0?  formatDouble(monthAppraiseSum / monthAppraiseNum) :"";
		}

		BigDecimal monthChargeMoney =chargeOrderService.selectTotalBalanceByTimeAndShopId(monthBegin, monthEnd, shopDetail.getId());


		//查询菜品top10
		//1.查询好评的总数(月内)
		int goodNum = 0;
		goodNum = articleTopService.selectSumGoodByTime(monthBegin, monthEnd, shopDetail.getId());
		//查询差评总数
		int badNum = 0;
		badNum = articleTopService.selectSumBadByTime(monthBegin, monthEnd, shopDetail.getId());

		//查询好评top10
		List<ArticleTopDto> goodList = articleTopService.selectListByTimeAndGoodType(monthBegin, monthEnd, shopDetail.getId());

		//查询差评top10
		List<ArticleTopDto> badList = articleTopService.selectListByTimeAndBadType(monthBegin, monthEnd, shopDetail.getId());

		//封装微信推送文本
		StringBuilder sb = new StringBuilder();
		sb
				.append("店铺名称:").append(shopDetail.getName()).append("\n")
				.append("时间:").append(DateUtil.formatDate(cleanDate, "yyyy-MM-dd HH:mm:ss")).append("\n")
				.append("本月总结").append("\n")
				.append("到店总笔数:").append(dto.getMonthEnterCount() + monthRestoCount).append("\n")
				.append("到店消费总额:").append(dto.getMonthEnterTotal().add(monthRestoTotal)).append("\n")
				.append("---------------------").append("\n")
				.append("Resto+用户消费比数:").append(monthRestoCount).append("\n")
				.append("Resto+用户消费金额").append(monthRestoTotal).append("\n")
				.append("---------------------").append("\n")
				.append("Resto+用户消费比率:").append(monthCustomerRatio).append("%").append("\n")
				.append("Resto+回头消费比率:").append(monthBackCustomerRatio).append("%").append("\n")
				.append("Resto+新增用户比率:").append(monthNewCustomerRatio).append("%").append("\n")
				.append("---------------------").append("\n")
				.append("Resto+新用户消费:").append(newCustomerOrderNum).append("笔/").append(newCustomerOrderTotal).append("\n")
				.append("Resto+其中自然用户:").append(newNormalCustomerOrderNum).append("笔/").append(newNormalCustomerOrderTotal).append("\n")
				.append("Resto+其中分享用户:").append(newShareCustomerOrderNum).append("笔/").append(newShareCustomerOrderTotal).append("\n")
				.append("Resto+回头用户消费:").append(backCustomerOrderNum).append("笔/").append(backCustomerOrderTotal).append("\n")
				.append("Resto+二次回头用户:").append(backTwoCustomerOrderNum).append("笔/").append(backTwoCustomerOrderTotal).append("\n")
				.append("Resto+多次回头用户:").append(backTwoMoreCustomerOderNum).append("笔/").append(backTwoMoreCustomerOrderTotal).append("\n")
				.append("---------------------").append("\n")
				.append("折扣合计:").append(discountTotal).append("\n")
                .append("POS折扣:").append(orderPosDiscount).append("\n")
                .append("免单:").append(exemptionMoney).append("\n")
                .append("红包:").append(redPackTotal).append("\n")
                .append("优惠券:").append(couponTotal).append("\n")
                .append("充值赠送:").append(chargeReturn).append("\n")
                .append("等位红包:").append(waitNumberIncome).append("\n")
                .append("积分支付:").append(integralPayment).append("\n")
                .append("代金券:").append(cashCouponPayment).append("\n")
				.append("折扣比率").append(discountRatio).append("\n")
				.append("---------------------").append("\n")
				.append("本月五星评论:").append(fiveStar).append("\n")
				.append("本月更改意见:").append(fourStar).append("\n")
				.append("本月差评投诉:").append(oneToThreeStar).append("\n")
				.append("本月满意度:").append(monthSatisfaction).append("\n")
				.append("---------------------").append("\n")
				.append("本月外卖金额:").append(dto.getMonthOrderBooks()).append("\n")
				.append("本月实收:").append(dto.getMonthEnterTotal().add(monthRestoTotal).add(dto.getMonthOrderBooks())).append("\n")
				.append("本月充值:").append(monthChargeMoney).append("\n")
				.append("---------------------").append("\n")
				.append("本月红榜top10：").append("\n");

		//封装好评top10
		if (goodNum == 0) {
			sb.append("------无-----");
		} else {
			if (!goodList.isEmpty()) {
				for (int i = 0; i < goodList.size(); i++) {
					//1、27% 剁椒鱼头
					sb.append(i + 1).append(".").append(NumberUtil.getFormat(goodList.get(i).getNum(), goodNum)).append("%").append(" ").append(goodList.get(i).getName()).append("\n");
				}
			}
		}

		sb.append("本月黑榜top10：").append("\n");
		//封装差评top10
		if (badNum == 0) {
			sb.append("------无-----");
		} else {
			if (!badList.isEmpty()) {
				for (int i = 0; i < badList.size(); i++) {
					//1、27% 剁椒鱼头
					sb.append(i + 1).append(".").append(NumberUtil.getFormat(badList.get(i).getNum(), badNum)).append("%").append(" ").append(badList.get(i).getName()).append("\n");
				}
			}
		}
		Map<String, String> map = new HashMap<>(128);
		map.put("wechat", sb.toString());
		return map;
	}


    /**
     * 计算数据
     * @param shopDetail
     * @param offLineOrder
     * @param wether
     * @param brand
     * @param cleanDate
     */
	private void insertDateData(ShopDetail shopDetail, OffLineOrder offLineOrder, Wether wether,Brand brand,Date cleanDate) {

		//----1.定义时间---
		Date todayBegin = DateUtil.getDateBegin(cleanDate);
		Date todayEnd = DateUtil.getDateEnd(cleanDate);

		//本月的开始时间 本月结束时间
		Date monthBegin = DateUtil.beginOfMonth(cleanDate);
		Date monthEnd = todayEnd;

		//旬开始时间 旬结束时间

		Date xunBegin = getXunBegin(cleanDate,monthBegin);
		Date xunEnd = todayEnd;

		int temp = DateUtil.getEarlyMidLateEnd(cleanDate);

		//三.定义线下订单

		UnderLineOrderDto dto = offLineOrderService.selectMonthXunTodayByShopId(todayBegin,todayEnd,xunBegin,xunEnd,monthBegin,monthEnd,shopDetail.getId());

		//查询当日新增用户的订单
		List<Order> todayNewCustomerOrders = orderService.selectNewCustomerOrderByShopIdAndTime(shopDetail.getId(), todayBegin, todayEnd);
		//新增用户的订单总数
		int todayNewCustomerOrderNum = (int)todayNewCustomerOrders.stream().count();
		//新增用户的订单总额
		BigDecimal todayNewCustomerOrderTotal = sumList(orderListToBigDecimalList(todayNewCustomerOrders));

		List<Order> todayNewShareList = orderListFilterNewShareCustomer(todayNewCustomerOrders);
		List<Order> todayNewNormalList = subOrderList(todayNewCustomerOrders,todayNewShareList);

		//新增分享用户的的订单总数
		int todayNewShareCustomerOrderNum = (int)todayNewShareList.stream().count();
		//新增分享用户的订单总额
		BigDecimal  todayNewShareCustomerOrderTotal = sumList(orderListToBigDecimalList(todayNewShareList));

		//新增自然用户的订单总数
		int  todayNewNormalCustomerOrderNum = (int)todayNewNormalList.stream().count();
		//新增自然用户的订单总额
		BigDecimal  todayNewNormalCustomerOrderTotal = sumList(orderListToBigDecimalList(todayNewNormalList));


		//查询回头用户的
		List<BackCustomerDto> todayBackCustomerDtos = orderService.selectBackCustomerByShopIdAndTime(shopDetail.getId(), todayBegin, todayEnd);
		//回头用户
		Set<String> todayBackCustomerId = orderListToBack(todayBackCustomerDtos);
		//二次回头用户
		Set<String> todayBackTwoCustomerId = orderListToBackTwo(todayBackCustomerDtos);
		//多次回头用户
		Set<String> todayBackTwoMoreCustomerId = orderListToBackTwoMore(todayBackCustomerDtos);

		List<Order> todayOrders = orderService.selectCompleteByShopIdAndTime(shopDetail.getId(), todayBegin, todayEnd);

		List<Order> backList = orderListFilter(todayOrders,todayBackCustomerId);
		List<Order> backTwoList = orderListFilter(todayOrders,todayBackTwoCustomerId);
		List<Order> backTwoMoreList = orderListFilter(todayOrders,todayBackTwoMoreCustomerId);

		//查询当日已消费的订单
		//回头用户的订单总数
		int todayBackCustomerOrderNum = (int)backList.stream().count();
		//二次回头用户的订单总数
		int  todayBackTwoCustomerOrderNum = (int)backTwoList.stream().count();
		//多次回头用户的订单总数
		int  todayBackTwoMoreCustomerOderNum = (int)backTwoMoreList.stream().count();
		//回头用户的订单总额
		BigDecimal  todayBackCustomerOrderTotal = sumList(orderListToBigDecimalList(backList));
		//二次回头用户的订单总额
		BigDecimal todayBackTwoCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoList));
		//多次回头用户的订单总额
		BigDecimal todayBackTwoMoreCustomerOrderTotal = sumList(orderListToBigDecimalList(backTwoMoreList));

		//本月订单
		List<Order> monthOrders = orderService.selectCompleteByShopIdAndTime(shopDetail.getId(),monthBegin, monthEnd);

		//2定义resto订单
		//本日resto订单总数
		int todayRestoCount = todayNewCustomerOrderNum+todayBackCustomerOrderNum;

		//本日resto订单总额
		BigDecimal todayRestoTotal = sumOrderMoney(todayOrders);

		//本月resto订单总额
		BigDecimal monthRestoTotal = sumOrderMoney(monthOrders);

		List<OrderPaymentItem> paymentItems = orderListToPaymentItemList(todayOrders);
        //得到订单折扣
        BigDecimal orderPosDiscount = sumOrderPosDiscount(todayOrders);
        //免单
        BigDecimal exemptionMoney = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.EXEMPTION_PAY));
        //红包
        BigDecimal redPackTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.ACCOUNT_PAY));
        //优惠券
        BigDecimal couponTotal = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.COUPON_PAY));
        //充值赠送
        BigDecimal chargeReturn = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.REWARD_PAY));
        //等位红包
        BigDecimal waitNumberIncome = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.WAIT_MONEY));
        //积分
        BigDecimal integralPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.INTEGRAL_PAY));
        //代金券
        BigDecimal cashCouponPayment = sumList(paymentItemListToBigDecimalList(paymentItems, PayMode.CASH_COUPIN_PAY));
        //定义折扣合计
        BigDecimal discountTotal = orderPosDiscount.add(exemptionMoney).add(redPackTotal).add(couponTotal).add(chargeReturn)
                .add(waitNumberIncome).add(integralPayment).add(cashCouponPayment);
		//折扣比率
		Boolean t1 = todayRestoTotal.add(discountTotal).compareTo(BigDecimal.ZERO) > 0;

		//本日用户消费比率 R+线下+外卖
		//到店总笔数 线上+线下
		double dmax = dto.getTodayEnterCount() + todayRestoCount;
		Boolean t2 = dmax!=0;

		String discountRatio = todayRestoTotal.add(discountTotal).compareTo(BigDecimal.ZERO) > 0 ? discountTotal.divide(todayRestoTotal.add(discountTotal), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString():"";
		//本日用户消费比率
		String todayCustomerRatio = t2 ? formatDouble((todayRestoCount / dmax) * 100):"";
		//回头用户消费比率
		String todayBackCustomerRatio = t2 ? formatDouble((todayBackCustomerOrderNum/ dmax) * 100):"";
		//新增用户比率
		String todayNewCustomerRatio = t2 ? formatDouble((todayNewCustomerOrderNum / dmax) * 100):"";

		BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());

		int todayFiveStar=0;
		int todayFourStar=0;
		int todayOneToThreeStar=0;
		String todaySatisfaction=null;
		String xunSatisfaction=null;
		String monthSatisfaction=null;
		if(brandSetting.getAppraiseEdition()==1){
			//查本日
			List<AppraiseNew> todayAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(todayBegin), DateTimeUtils.dateToString(todayEnd));

			//查本旬
			List<AppraiseNew> xunAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(xunBegin), DateTimeUtils.dateToString(xunEnd));

			//查本月
			List<AppraiseNew> monthAppraises = newAppraiseNewService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(),DateTimeUtils.dateToString(monthBegin), DateTimeUtils.dateToString(monthEnd));

			//本日五星
			todayFiveStar = appraiseNewListFilterFiveStar(todayAppraises);
			//本日四星
			todayFourStar = appraiseNewListFilterFourStar(todayAppraises);
			//本日3星-1星
			todayOneToThreeStar = appraiseNewListFilterOneToThreeStar(todayAppraises);

			//本旬五星
			int xunFiveStar = appraiseNewListFilterFiveStar(xunAppraises);
			//本旬四星
			int xunFourStar = appraiseNewListFilterFourStar(xunAppraises);
			//本旬3星-1星
			int xunOneToThreeStar = appraiseNewListFilterOneToThreeStar(xunAppraises);

			//本月五星
			int monthFiveStar = appraiseNewListFilterFiveStar(monthAppraises);
			//本月四星
			int monthFourStar = appraiseNewListFilterFourStar(monthAppraises);
			//本月3星-1星
			int monthOneToThreeStar = appraiseNewListFilterOneToThreeStar(monthAppraises);


			//当日所有评价的总分数
			double dayAppraiseSum = appraiseNewListPrice(todayAppraises);
			//上旬所有评价的总分数
			double xunAppraiseSum = appraiseNewListPrice(xunAppraises);
			//本月所有评价的总分数
			double monthAppraiseSum = appraiseNewListPrice(monthAppraises);


			//当日评价的总单数
			int dayAppraiseNum = (int)todayAppraises.stream().count();
			//本旬评价的总单数
			int xunAppraiseNum = (int)xunAppraises.stream().count();
			//本月评价的单数
			int monthAppraiseNum = (int)monthAppraises.stream().count();

			//3定义满意度
			//本日满意度
			todaySatisfaction = dayAppraiseNum != 0? formatDouble(dayAppraiseSum / dayAppraiseNum) :"";
			//本旬满意度
			xunSatisfaction = xunAppraiseNum != 0? formatDouble(xunAppraiseSum / xunAppraiseNum) :"";
			//本月满意度
			monthSatisfaction = monthAppraiseNum != 0? formatDouble(monthAppraiseSum / monthAppraiseNum) :"";
		}else{
			//查本日
			List<Appraise> todayAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(todayBegin), DateTimeUtils.dateToString(todayEnd));

			//查本旬
			List<Appraise> xunAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(), DateTimeUtils.dateToString(xunBegin), DateTimeUtils.dateToString(xunEnd));

			//查本月
			List<Appraise> monthAppraises = newAppraiseService.selectByTimeAndShopId(shopDetail.getBrandId(),shopDetail.getId(),DateTimeUtils.dateToString(monthBegin), DateTimeUtils.dateToString(monthEnd));

			//本日五星
			todayFiveStar = appraiseListFilterFiveStar(todayAppraises);
			//本日四星
			todayFourStar = appraiseListFilterFourStar(todayAppraises);
			//本日3星-1星
			todayOneToThreeStar = appraiseListFilterOneToThreeStar(todayAppraises);

			//本旬五星
			int xunFiveStar = appraiseListFilterFiveStar(xunAppraises);
			//本旬四星
			int xunFourStar = appraiseListFilterFourStar(xunAppraises);
			//本旬3星-1星
			int xunOneToThreeStar = appraiseListFilterOneToThreeStar(xunAppraises);

			//本月五星
			int monthFiveStar = appraiseListFilterFiveStar(monthAppraises);
			//本月四星
			int monthFourStar = appraiseListFilterFourStar(monthAppraises);
			//本月3星-1星
			int monthOneToThreeStar = appraiseListFilterOneToThreeStar(monthAppraises);


			//当日所有评价的总分数
			double dayAppraiseSum = appraiseListPrice(todayAppraises);
			//上旬所有评价的总分数
			double xunAppraiseSum = appraiseListPrice(xunAppraises);
			//本月所有评价的总分数
			double monthAppraiseSum = appraiseListPrice(monthAppraises);


			//当日评价的总单数
			int dayAppraiseNum = (int)todayAppraises.stream().count();
			//本旬评价的总单数
			int xunAppraiseNum = (int)xunAppraises.stream().count();
			//本月评价的单数
			int monthAppraiseNum = (int)monthAppraises.stream().count();

			//3定义满意度
			//本日满意度
			todaySatisfaction = dayAppraiseNum != 0? formatDouble(dayAppraiseSum / dayAppraiseNum) :"";
			//本旬满意度
			xunSatisfaction = xunAppraiseNum != 0? formatDouble(xunAppraiseSum / xunAppraiseNum) :"";
			//本月满意度
			monthSatisfaction = monthAppraiseNum != 0? formatDouble(monthAppraiseSum / monthAppraiseNum) :"";
		}

		//查询菜品今日top10
		//1.查询好评的总数(本日)
		int  todayGoodNum = 0;
		todayGoodNum = articleTopService.selectSumGoodByTime(todayBegin, todayEnd, shopDetail.getId());
		//查询差评总数
		int todayBadNum = 0;
		todayBadNum = articleTopService.selectSumBadByTime(todayBegin, todayEnd, shopDetail.getId());

		//查询好评top10
		List<ArticleTopDto> todayGoodList = articleTopService.selectListByTimeAndGoodType(todayBegin, todayEnd, shopDetail.getId());

		//查询差评top10
		List<ArticleTopDto> todayBadList = articleTopService.selectListByTimeAndBadType(todayBegin, todayEnd, shopDetail.getId());

		//yz 2017-07-25 dayDataMessage 被merge了
		//存储结店数据

		DayDataMessage ds = new DayDataMessage();
		ds.setId(ApplicationUtils.randomUUID());
		ds.setShopId(shopDetail.getId());
		//日结
		ds.setType(DayMessageType.DAY_TYPE);
		ds.setShopName(shopDetail.getName());
		ds.setWeekDay(wether.getWeekady());
		ds.setDate(new Date());
		ds.setWether(wether.getDayWeather());
		ds.setTemperature(wether.getDayTemperature());
		//到店总笔数
		ds.setOrderNumber(dto.getTodayEnterCount() + todayRestoCount);
		//到店消费总额
		ds.setOrderSum(dto.getTodayEnterTotal().add(todayRestoTotal));
		ds.setCustomerOrderNumber(todayRestoCount);
		ds.setCustomerOrderSum(todayRestoTotal);
		ds.setCustomerOrderRatio(todayCustomerRatio+"%");
		ds.setNewCustomerOrderRatio(todayNewCustomerRatio+"%");
		ds.setBackCustomerOrderRatio(todayBackCustomerRatio+"%");
		//新用户订单数
		ds.setNewCuostomerOrderNum(todayNewCustomerOrderNum);
		ds.setNewCustomerOrderSum(todayNewCustomerOrderTotal);
		ds.setNewNormalCustomerOrderNum(todayNewNormalCustomerOrderNum);
		ds.setNewNormalCustomerOrderSum(todayNewNormalCustomerOrderTotal);
		ds.setNewShareCustomerOrderNum(todayNewShareCustomerOrderNum);
		ds.setNewShareCustomerOrderSum(todayNewShareCustomerOrderTotal);
		ds.setBackCustomerOrderNum(todayBackCustomerOrderNum);
		ds.setBackCustomerOrderSum(todayBackCustomerOrderTotal);
		ds.setBackTwoCustomerOrderNum(todayBackTwoCustomerOrderNum);
		ds.setBackTwoCustomerOrderSum(todayBackTwoCustomerOrderTotal);
		ds.setBackTwoMoreCustomerOrderNum(todayBackTwoMoreCustomerOderNum);
		ds.setBackTwoMoreCustomerOrderSum(todayBackTwoMoreCustomerOrderTotal);
		ds.setDiscountTotal(discountTotal);
		ds.setRedPack(redPackTotal);
		ds.setCoupon(couponTotal);
		ds.setChargeReward(chargeReturn);
		ds.setDiscountRatio(discountRatio);
		ds.setTakeawayTotal(dto.getTodayOrderBooks());
		//本日营业总额
		ds.setBussinessTotal(dto.getTodayEnterTotal().add(todayRestoTotal).add(dto.getTodayOrderBooks()));
		//本月营业总额
		ds.setMonthTotal(dto.getMonthOrderBooks().add(dto.getMonthEnterTotal()).add(monthRestoTotal));
		//yz 2017-10-18 设置--
		DataSourceContextHolder.setDataSourceName(brand.getId());
		dayDataMessageService.insert(ds);

		//删除今日top10数据
		goodTopService.deleteByTodayAndShopId(brand.getId(),shopDetail.getId(),MessageType.DAY_MESSAGE,new Date());

		//存查询出来的今日数据

		//存今日goodTop10
		if(todayGoodList!=null&&!todayGoodList.isEmpty()){
			for(int i=0;i<todayGoodList.size();i++){
				GoodTop goodTop = new GoodTop();
				goodTop.setName(todayGoodList.get(i).getName());
				goodTop.setPrecent(NumberUtil.getFormat(todayGoodList.get(i).getNum(),todayGoodNum));
				goodTop.setSort(i+1);
				goodTop.setShopId(shopDetail.getId());
				goodTop.setBrandId(brand.getId());
				goodTop.setBrandName(brand.getBrandName());
				goodTop.setShopName(shopDetail.getName());
				goodTop.setDate(new Date());
				goodTop.setType(MessageType.DAY_MESSAGE);
				goodTopService.insert(goodTop);
			}

		}

		//存今日BadTop10
		if(todayBadList!=null&&!todayBadList.isEmpty()){
			for(int i=0;i<todayBadList.size();i++){
				BadTop badTop = new BadTop();
				badTop.setName(todayBadList.get(i).getName());
				badTop.setPrecent(NumberUtil.getFormat(todayBadList.get(i).getNum(),todayBadNum));
				badTop.setSort(i+1);
				badTop.setShopId(shopDetail.getId());
				badTop.setBrandId(brand.getId());
				badTop.setBrandName(brand.getBrandName());
				badTop.setShopName(shopDetail.getName());
				badTop.setDate(new Date());
				badTop.setType(MessageType.DAY_MESSAGE);
				badTopService.insert(badTop);
			}

		}

		//查询菜品本旬top10
		//1.查询好评的总数(本日)
		int  xunGoodNum = 0;
		xunGoodNum = articleTopService.selectSumGoodByTime(xunBegin, xunEnd, shopDetail.getId());
		//查询差评总数
		int xunBadNum = 0;
		xunBadNum = articleTopService.selectSumBadByTime(xunBegin, xunEnd, shopDetail.getId());
		//查询好评top10
		List<ArticleTopDto> xunGoodList = articleTopService.selectListByTimeAndGoodType(xunBegin, xunEnd, shopDetail.getId());

		//查询差评top10
		List<ArticleTopDto> xunBadList = articleTopService.selectListByTimeAndBadType(xunBegin, xunEnd, shopDetail.getId());

		//存本旬goodTop10
		if(xunGoodList!=null&&!xunGoodList.isEmpty()){
			for(int i=0;i<xunGoodList.size();i++){
				GoodTop goodTop = new GoodTop();
				goodTop.setName(xunGoodList.get(i).getName());
				goodTop.setPrecent(NumberUtil.getFormat(xunGoodList.get(i).getNum(),xunGoodNum));
				goodTop.setSort(i+1);
				goodTop.setShopId(shopDetail.getId());
				goodTop.setBrandId(brand.getId());
				goodTop.setBrandName(brand.getBrandName());
				goodTop.setShopName(shopDetail.getName());
				goodTop.setDate(new Date());
				goodTop.setType(MessageType.XUN_MESSAGE);
				goodTopService.insert(goodTop);
			}

		}

		//存本旬BadTop10
		if(xunBadList!=null&&!xunBadList.isEmpty()){
			for(int i=0;i<xunBadList.size();i++){
				BadTop badTop = new BadTop();
				badTop.setName(xunBadList.get(i).getName());
				badTop.setPrecent(NumberUtil.getFormat(xunBadList.get(i).getNum(),xunBadNum));
				badTop.setSort(i+1);
				badTop.setShopId(shopDetail.getId());
				badTop.setBrandId(brand.getId());
				badTop.setBrandName(brand.getBrandName());
				badTop.setShopName(shopDetail.getName());
				badTop.setDate(new Date());
				badTop.setType(MessageType.XUN_MESSAGE);
				badTopService.insert(badTop);
			}

		}


		//查询菜品本月top10
		//1.查询好评的总数(本月)
		int  monthGoodNum = 0;
		monthGoodNum = articleTopService.selectSumGoodByTime(monthBegin, monthEnd, shopDetail.getId());
		//查询差评总数
		int monthBadNum = 0;
		monthBadNum = articleTopService.selectSumBadByTime(monthBegin, monthEnd, shopDetail.getId());

		//查询好评top10
		List<ArticleTopDto> monthGoodList = articleTopService.selectListByTimeAndGoodType(monthBegin, monthEnd, shopDetail.getId());

		//查询差评top10
		List<ArticleTopDto> monthBadList = articleTopService.selectListByTimeAndBadType(monthBegin, monthEnd, shopDetail.getId());

		//存本月goodTop10
		if(monthGoodList!=null&&!monthGoodList.isEmpty()){
			for(int i=0;i<monthGoodList.size();i++){
				GoodTop goodTop = new GoodTop();
				goodTop.setName(monthGoodList.get(i).getName());
				goodTop.setPrecent(NumberUtil.getFormat(monthGoodList.get(i).getNum(),monthGoodNum));
				goodTop.setSort(i+1);
				goodTop.setShopId(shopDetail.getId());
				goodTop.setBrandId(brand.getId());
				goodTop.setBrandName(brand.getBrandName());
				goodTop.setShopName(shopDetail.getName());
				goodTop.setDate(new Date());
				goodTop.setType(MessageType.MONTH_MESSAGE);
				goodTopService.insert(goodTop);

			}

		}

		//存本月BadTop10
		if(monthBadList!=null&&!monthBadList.isEmpty()){
			for(int i=0;i<monthBadList.size();i++){
				BadTop badTop = new BadTop();
				badTop.setName(monthBadList.get(i).getName());
				badTop.setPrecent(NumberUtil.getFormat(monthBadList.get(i).getNum(),monthBadNum));
				badTop.setSort(i+1);
				badTop.setShopId(shopDetail.getId());
				badTop.setBrandId(brand.getId());
				badTop.setBrandName(brand.getBrandName());
				badTop.setShopName(shopDetail.getName());
				badTop.setDate(new Date());
				badTop.setType(MessageType.MONTH_MESSAGE);
				badTopService.insert(badTop);
			}

		}

		//存评论数据
		DayAppraiseMessageWithBLOBs dm = new DayAppraiseMessageWithBLOBs();
		dm.setId(ApplicationUtils.randomUUID());
		dm.setShopId(shopDetail.getId());
		dm.setShopName(shopDetail.getName());
		dm.setDate(new Date());
		dm.setState(true);
		dm.setWether(wether.getDayWeather());
		dm.setWeekDay(wether.getWeekady());
		dm.setTemperature(wether.getDayTemperature());
		dm.setType(DayMessageType.DAY_TYPE);
		dm.setFiveStar(todayFiveStar);
		dm.setFourStar(todayFourStar);
		dm.setOneThreeStar(todayOneToThreeStar);
		dm.setDaySatisfaction(todaySatisfaction);
		dm.setXunSatisfaction(xunSatisfaction);
		dm.setMonthSatisfaction(monthSatisfaction);
		if (todayGoodNum == 0) {
			dm.setRedList("----无好评----");
		} else {
			if (!todayGoodList.isEmpty()) {
				com.alibaba.fastjson.JSONObject redJson = new com.alibaba.fastjson.JSONObject();
				for (int i = 0; i < todayGoodList.size(); i++) {
					//1、27% 剁椒鱼头
					// sbScore.append("top"+(i + 1)).append("：").append(NumberUtil.getFormat(todayGoodList.get(i).getNum(), todayGoodNum)).append("%").append(" ").append(todayGoodList.get(i).getName())
					StringBuilder sb = new StringBuilder();
					sb.append(NumberUtil.getFormat(todayGoodList.get(i).getNum(), todayGoodNum)).append("%").append(" ").append(todayGoodList.get(i).getName());
					redJson.put("top"+(i+1),sb.toString());
				}
				dm.setRedList(com.alibaba.fastjson.JSONObject.toJSONString(redJson));
			}
		}
		if (todayBadNum == 0) {
			dm.setBadList("------无差评-----");
		} else {
			if (!todayBadList.isEmpty()) {
				com.alibaba.fastjson.JSONObject bakcJson = new com.alibaba.fastjson.JSONObject();
				for (int i = 0; i < todayBadList.size(); i++) {
					//1、27% 剁椒鱼头
					StringBuilder sb = new StringBuilder();
					sb.append(NumberUtil.getFormat(todayBadList.get(i).getNum(), todayBadNum)).append("%").append(" ").append(todayBadList.get(i).getName());
					bakcJson.put("top"+(i+1),sb.toString());
				}
				dm.setBadList(com.alibaba.fastjson.JSONObject.toJSONString(bakcJson));
			}
		}
		DataSourceContextHolder.setDataSourceName(brand.getId());
		dayAppraiseMessageService.insert(dm);
	}


    /**
     * 发送消息
     * @param querryMap
     * @param shopDetail
     * @param wechatConfig
     * @param brand
     */
	private void pushMessageByFirstEdtion(Map<String, String> querryMap, ShopDetail shopDetail, WechatConfig wechatConfig, Brand brand) {
		if (Common.YES.equals(shopDetail.getIsOpenSms())&& null != shopDetail.getNoticeTelephone()) {
			//截取电话号码
			String telephones = shopDetail.getNoticeTelephone().replaceAll("，", ",");

			BrandSetting brandSetting = brandSettingService.selectByBrandId(brand.getId());

			String[] tels = telephones.split(",");

			for (String telephone : tels) {
				//String brandId, String shopId,int smsType, String sign, String code_temp,String phone,JSONObject jsonObject
				if(brandSetting.getOpenAudit() == 1){
					//开启复核日结短信
					JSONObject smsResult = smsLogService.sendMessage(brand.getId(),shopDetail.getId(),SmsLogType.REVIEWAMOUNTDAYMESSGAGE,SMSUtils.SIGN,SMSUtils.REVIEW_AMOUNT_DAY_SMS_SEND,telephone,JSONObject.parseObject(querryMap.get("sms")));
					//JSONObject smsResult = SMSUtils.sendMessage(s, JSONObject.parseObject(querryMap.get("sms")), "餐加", "SMS_46725122", null);//推送本日信息
					log.info("短信返回内容："+smsResult);
				}else{
					//未开启复核日结短信
					JSONObject smsResult = smsLogService.sendMessage(brand.getId(),shopDetail.getId(),SmsLogType.DAYMESSGAGE,SMSUtils.SIGN,SMSUtils.SMS_NEW_DAY_SEND,telephone,JSONObject.parseObject(querryMap.get("sms")));
					//JSONObject smsResult = SMSUtils.sendMessage(s, JSONObject.parseObject(querryMap.get("sms")), "餐加", "SMS_46725122", null);//推送本日信息
					log.info("短信返回内容："+smsResult);
				}
				Customer c = customerService.selectByTelePhone(telephone);
				/**
				 发送客服消息
				 */
				if (null != c) {
					weChatService.sendDayCustomerMsgASync(querryMap.get("wechat"), c.getWechatId(), wechatConfig.getAppid(), wechatConfig.getAppsecret(), telephone, brand.getBrandName(), shopDetail.getName());
				}
			}

		}
	}


    /**
     * 计算订单列表中的订单金额（加上pos端折扣、抹零、按金额优惠、会员折扣）
     * @param orderList
     * @return
     */
	private BigDecimal sumOrderMoney(List<Order> orderList) {
	    BigDecimal orderMoney = orderList.stream().filter(order -> StringUtils.isBlank(order.getParentOrderId()))
                .map(order -> order.getAmountWithChildren().compareTo(BigDecimal.ZERO) > 0 ? order.getAmountWithChildren() : order.getOrderMoney())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
	    BigDecimal orderPosDiscount = sumOrderPosDiscount(orderList);
        BigDecimal total = orderMoney.add(orderPosDiscount);
        return total;
    }

    /**
     * 计算订单列表中的折扣金额
     * @param orderList
     * @return
     */
    private BigDecimal sumOrderPosDiscount(List<Order> orderList) {
        BigDecimal orderPosDiscountMoney = orderList.stream().map(Order::getOrderPosDiscountMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal memberDiscountMoney = orderList.stream().map(Order::getMemberDiscountMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal realEraseMoney = orderList.stream().map(Order::getRealEraseMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal grantMoney = orderList.stream().map(Order::getGrantMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = orderPosDiscountMoney.add(memberDiscountMoney).add(realEraseMoney).add(grantMoney);
        return total;
    }

	private BigDecimal sumList(List<BigDecimal> list){
		return  list.stream().reduce(BigDecimal.ZERO,(s1,s2) -> s1.add(s2));
	}


	private List<BigDecimal>  orderListToBigDecimalList(List<Order> orderList){
		List<BigDecimal> list = new ArrayList<>();
		orderList.stream().forEach(o -> list.add(getOrderMoney(o.getParentOrderId(), o.getOrderMoney(), o.getAmountWithChildren())));
		return  list;
	}


	private List<OrderPaymentItem> orderListToPaymentItemList(List<Order> orderList){
		List<OrderPaymentItem> paymentItems = new ArrayList<>();
		orderList.stream().filter(o -> o.getOrderPaymentItems()!=null).forEach(o ->paymentItems.addAll(o.getOrderPaymentItems()));
		return paymentItems;
	}

	private List<BigDecimal>  paymentItemListToBigDecimalList(List<OrderPaymentItem> paymentItemList,Integer type){
		List<BigDecimal> list = new ArrayList<>();
		paymentItemList.stream().filter(oi -> type.equals(oi.getPaymentModeId()) ).forEach(oi -> list.add(oi.getPayValue()));
		return  list;
	}

	private List<Order> orderListFilterNewShareCustomer(List<Order> list){
		List<Order> orderList = new ArrayList<>();
		list.stream().filter(o -> o.getCustomer() != null && !StringUtils.isEmpty(o.getCustomer().getShareCustomer())).forEach(o -> orderList.add(o));
		return  orderList;
	}

	private  List<Order> subOrderList(List<Order> parentList,List<Order> childrenList){
		parentList.removeAll(childrenList);
		return parentList;
	}

	private Set <String> orderListToBack(List<BackCustomerDto> dtoList){
		Set<String> list = new HashSet<>();
		dtoList.stream().forEach(o -> list.add(o.getCustomerId()));
		return list;
	}

	private Set<String> orderListToBackTwo(List<BackCustomerDto> dtoList){
		Set<String> list = new HashSet<>();
		 dtoList.stream().filter(o ->o.getNum() ==1).forEach(o -> list.add(o.getCustomerId()));
		 return list;
	}

	private Set<String> orderListToBackTwoMore(List<BackCustomerDto> dtoList){
		Set<String> list = new HashSet<>();
		dtoList.stream().filter(o ->o.getNum() > 1).forEach(o -> list.add(o.getCustomerId()));
		return list;
	}

	private List<Order> orderListFilter(List<Order> list,Set<String> strings){
		List<Order> orderList = new ArrayList<>();
		list.stream().filter(o -> strings.contains(o.getCustomerId())).forEach(o -> orderList.add(o));
		return  orderList;
	}

	/**
	 * 获取五星好评
	 * @param appraiseList
	 * @return
	 */
	private int appraiseListFilterFiveStar(List<Appraise> appraiseList){
		return (int)appraiseList.stream().filter(a ->a.getLevel() == 5).count();
	}

	private int appraiseListFilterFourStar(List<Appraise> appraiseList){
		return (int)appraiseList.stream().filter(a ->a.getLevel() == 4).count();
	}

	private int appraiseListFilterOneToThreeStar(List<Appraise> appraiseList){
		return (int)appraiseList.stream().filter(a ->a.getLevel() == 1||a.getLevel()==2 || a.getLevel()==3).count();
	}

	/**
	 * 新版评论五星好评
	 * @param appraiseList
	 * @return
	 */
	private int appraiseNewListFilterFiveStar(List<AppraiseNew> appraiseNewList){
		return (int)appraiseNewList.stream().filter(a ->a.getLevel() == 100).count();
	}

	private int appraiseNewListFilterFourStar(List<AppraiseNew> appraiseNewList){
		return (int)appraiseNewList.stream().filter(a ->a.getLevel() == 80).count();
	}

	private int appraiseNewListFilterOneToThreeStar(List<AppraiseNew> appraiseNewList){
		return (int)appraiseNewList.stream().filter(a ->a.getLevel() == 20||a.getLevel()==40 || a.getLevel()==60).count();
	}

	/**
	 * 获取总评分
	 * @param appraiseList
	 * @return
	 */
	private int appraiseListPrice(List<Appraise> appraiseList){
		List<Integer> list = new ArrayList<>();
		appraiseList.stream().forEach(a ->list.add(a.getLevel()*20));
		return list.stream().reduce(0,(s1,s2) -> s1+s2);
	}

	/**
	 * 获取新版评论总评分
	 * @param appraiseList
	 * @return
	 */
	private int appraiseNewListPrice(List<AppraiseNew> appraiseList){
		List<Integer> list = new ArrayList<>();
		appraiseList.stream().forEach(a ->list.add(a.getLevel()));
		return list.stream().reduce(0,(s1,s2) -> s1+s2);
	}

	private List<BigDecimal> chargeOrderListToBigDecimalList(List<ChargeOrder> chargeOrders){
		List<BigDecimal> list = chargeOrders.stream().map(chargeOrder -> chargeOrder.getChargeMoney()).collect(Collectors.toList());
		return list;
	}

	private BigDecimal chargeOrderListToBigDeciaml(List<ChargeOrder> chargeOrders){
		List<BigDecimal> list = chargeOrderListToBigDecimalList(chargeOrders);
		return sumList(list);

	}


	private Date getXunBegin(Date cleanDate,Date monthBegin) {

		//判断当前传入的日期是在哪旬中
		int temp = DateUtil.getEarlyMidLate(cleanDate);
		Date xunBegin = new Date();
		switch (temp){
			case DayMessageType.DAY_TYPE:
				xunBegin = monthBegin;
				break;
			case DayMessageType.XUN_TYPE:
				xunBegin = DateUtil.getAfterDayDate(monthBegin,10);
				break;
			case DayMessageType.MONTH_TYPE:
				xunBegin = DateUtil.getAfterDayDate(monthBegin,20);
				break;
			default:
				break;
		}
		return xunBegin;
	}

	public static void main(String[] args) {

		Set<String> set = new HashSet<>();
		set.stream().count();

	}
}

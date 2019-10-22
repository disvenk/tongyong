package com.resto.brand.web.dto;

import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.ExcelAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class OrderDetailDto implements Serializable {

	private String shopDetailId;//店铺id
	@ExcelAnnotation(exportName = "订单Id")
	private String orderId;

	@Getter
	@Setter
	@ExcelAnnotation(exportName = "订单流水号")
	private String serialNumber;

	@ExcelAnnotation(exportName = "店铺名字")
	private String shopName;//店铺名字

	private String tableNumber;//桌号
	
	private Date beginTime ;//下单时间

	@ExcelAnnotation(exportName = "下单时间")
    private String createTime;

	@ExcelAnnotation(exportName = "手机号")
	private String telephone;//订单数

	@ExcelAnnotation(exportName = "订单金额(元)")
	private BigDecimal orderMoney;//订单金额

	@Getter
	@Setter
	private Boolean offline;
	@ExcelAnnotation(exportName = "微信支付(元)")
	private BigDecimal weChatPay;//微信支付

	@ExcelAnnotation(exportName = "红包支付(元)")
	private BigDecimal accountPay;//红包支付

	@ExcelAnnotation(exportName = "优惠券支付(元)")
	private BigDecimal couponPay;//优惠券支付

	@ExcelAnnotation(exportName = "充值金额支付(元)	")
	private BigDecimal chargePay;//充值金额支付

	@ExcelAnnotation(exportName = "现金实收(元)")
	private BigDecimal moneyPay;

	@ExcelAnnotation(exportName = "银联支付(元)")
	private BigDecimal backCartPay;//银行卡

	@ExcelAnnotation(exportName = "充值赠送金额支付(元)")
	private BigDecimal rewardPay;//充值赠送支付

	@ExcelAnnotation(exportName = "等位红包支付(元)")
    private BigDecimal waitRedPay;//等位红包支付
	
	private String incomePrize;//营销撬动率

    private Boolean isChildOrder;//是否是子订单
	private BigDecimal otherPayment;
    //用户

	@ExcelAnnotation(exportName = "支付宝支付(元)")
	private BigDecimal aliPayment;

	@ExcelAnnotation(exportName = "退菜返还红包(元)")
	private BigDecimal articleBackPay; //退菜红包

    private List<Map<String, Object>> shopOrderList;

	@ExcelAnnotation(exportName = "会员支付(元")
    private BigDecimal integralPay;

	@ExcelAnnotation(exportName = "新美大支付(元)")
    private BigDecimal shanhuiPay;

    private BigDecimal giveChangePayment;

	private Integer distributionModeId;//订单类型
	@ExcelAnnotation(exportName = "现金退款(元)")
	private BigDecimal refundCrashPayment;

	@ExcelAnnotation(exportName = "实体卡折扣")
	private BigDecimal cardDiscountPay;

	@ExcelAnnotation(exportName = "实体卡充值支付")
	private BigDecimal cardRechargePay;

	@ExcelAnnotation(exportName = "实体卡赠送支付")
	private BigDecimal cardRechargeFreePay;

	@ExcelAnnotation(exportName = "实体卡退款金额")
	private BigDecimal refundCardPay;

	@Setter
	@Getter
	@ExcelAnnotation(exportName = "POS端折扣金额(元)")
	private BigDecimal orderPosDiscountMoney;

	@Setter
	@Getter
	@ExcelAnnotation(exportName = "会员折扣金额(元)")
	private BigDecimal memberDiscountMoney;

	@Setter
	@Getter
	@ExcelAnnotation(exportName = "抹零金额(元)")
	private BigDecimal realEraseMoney;

	@Setter
	@Getter
	@ExcelAnnotation(exportName = "抹零金额(元)")
	private BigDecimal exemptionMoney;

	@Setter
	@Getter
	@ExcelAnnotation(exportName = "团购支付(元)")
	private BigDecimal groupPurchasePayment;

	@Setter
	@Getter
	@ExcelAnnotation(exportName = "代金券支付(元)")
	private BigDecimal cashCouponPayment;

	@Setter
	@Getter
	private BigDecimal grantMoney;

	public BigDecimal getCardDiscountPay() {
		return cardDiscountPay;
	}

	public void setCardDiscountPay(BigDecimal cardDiscountPay) {
		this.cardDiscountPay = cardDiscountPay;
	}

	public BigDecimal getCardRechargePay() {
		return cardRechargePay;
	}

	public void setCardRechargePay(BigDecimal cardRechargePay) {
		this.cardRechargePay = cardRechargePay;
	}

	public BigDecimal getCardRechargeFreePay() {
		return cardRechargeFreePay;
	}

	public void setCardRechargeFreePay(BigDecimal cardRechargeFreePay) {
		this.cardRechargeFreePay = cardRechargeFreePay;
	}

	public BigDecimal getRefundCardPay() {
		return refundCardPay;
	}

	public void setRefundCardPay(BigDecimal refundCardPay) {
		this.refundCardPay = refundCardPay;
	}

	public BigDecimal getRefundCrashPayment() {
		return refundCrashPayment;
	}

	public void setRefundCrashPayment(BigDecimal refundCrashPayment) {
		this.refundCrashPayment = refundCrashPayment;
	}

	public BigDecimal getGiveChangePayment() {
        return giveChangePayment;
    }

    public void setGiveChangePayment(BigDecimal giveChangePayment) {
        this.giveChangePayment = giveChangePayment;
    }

    public BigDecimal getShanhuiPay() {
        return shanhuiPay;
    }

    public void setShanhuiPay(BigDecimal shanhuiPay) {
        this.shanhuiPay = shanhuiPay;
    }

    public BigDecimal getIntegralPay() {
        return integralPay;
    }

    public void setIntegralPay(BigDecimal integralPay) {
        this.integralPay = integralPay;
    }

    public List<Map<String, Object>> getShopOrderList() {
        return shopOrderList;
    }

    public void setShopOrderList(List<Map<String, Object>> shopOrderList) {
        this.shopOrderList = shopOrderList;
    }

    final public BigDecimal getArticleBackPay() {
		return articleBackPay;
	}

	final public void setArticleBackPay(BigDecimal articleBackPay) {
		this.articleBackPay = articleBackPay;
	}

	public BigDecimal getAliPayment() {
		return aliPayment;
	}

	public void setAliPayment(BigDecimal aliPayment) {
		this.aliPayment = aliPayment;
	}

	public BigDecimal getOtherPayment() {
		return otherPayment;
	}

	public void setOtherPayment(BigDecimal otherPayment) {
		this.otherPayment = otherPayment;
	}

	public String getMarket() {
		return market;
	}

	public BigDecimal getMoneyPay() {
		return moneyPay;
	}

	public void setMoneyPay(BigDecimal moneyPay) {
		this.moneyPay = moneyPay;
	}

	public BigDecimal getBackCartPay() {
		return backCartPay;
	}

	public void setBackCartPay(BigDecimal backCartPay) {
		this.backCartPay = backCartPay;
	}

	//yjuany 加了个构造
    public OrderDetailDto(String orderId, String shopName, Date beginTime, String telephone, BigDecimal orderMoney,
			BigDecimal weChatPay, BigDecimal accountPay, BigDecimal couponPay, BigDecimal chargePay,
			BigDecimal rewardPay, BigDecimal waitRedPay, String incomePrize, String begin, String market, String level,
			String orderState) {
		super();
		this.orderId = orderId;
		this.shopName = shopName;
		this.beginTime = beginTime;
		this.telephone = telephone;
		this.orderMoney = orderMoney;
		this.weChatPay = weChatPay;
		this.accountPay = accountPay;
		this.couponPay = couponPay;
		this.chargePay = chargePay;
		this.rewardPay = rewardPay;
		this.waitRedPay = waitRedPay;
		this.incomePrize = incomePrize;
		this.begin = begin;
		this.market = market;
		this.level = level;
		this.orderState = orderState;
	}

	private  Integer orderMode;//店铺模式

    public Integer getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(Integer orderMode) {
        this.orderMode = orderMode;
    }

    public Boolean getChildOrder() {
        return isChildOrder;
    }

    public void setChildOrder(Boolean childOrder) {
        isChildOrder = childOrder;
    }

    @SuppressWarnings("unused")
	private String begin;
	
	
	public String getBegin() {
		
		return DateUtil.formatDate(this.beginTime, "yyyy-MM-dd HH:mm:ss");
	}

    public BigDecimal getWaitRedPay() {
        return waitRedPay;
    }

    public void setWaitRedPay(BigDecimal waitRedPay) {
        this.waitRedPay = waitRedPay;
    }

    public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getIncomePrize() {
		//
		return incomePrize;
	}

	public void setIncomePrize(String incomePrize) {
		this.incomePrize = incomePrize;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@SuppressWarnings("unused")
	private String market;//营销撬动
	
	private String level;//评价

	@ExcelAnnotation(exportName = "订单状态")
	private String orderState;//订单状态
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public OrderDetailDto() {
		super();
	}
   
	
	public OrderDetailDto(String orderId,String shopDetailId, String shopName, Date beginTime, String telephone, BigDecimal orderMoney,
			BigDecimal weChatPay, BigDecimal accountPay, BigDecimal couponPay, BigDecimal chargePay,
			BigDecimal rewardPay,BigDecimal waitRedPay, String market,String level,String orderState,Boolean isChildOrder) {
		super();
		this.orderId = orderId;
		this.shopDetailId = shopDetailId;
		this.shopName = shopName;
		this.beginTime = beginTime;
		this.telephone = telephone;
		this.orderMoney = orderMoney;
		this.weChatPay = weChatPay;
		this.accountPay = accountPay;
		this.couponPay = couponPay;
		this.chargePay = chargePay;
		this.rewardPay = rewardPay;
        this.waitRedPay=waitRedPay;
		this.market = market;
		this.level = level;
		this.orderState = orderState;
		this.isChildOrder=isChildOrder;
	}

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getShopDetailId() {
		return shopDetailId;
	}

	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}

	public String getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getWeChatPay() {
		return weChatPay;
	}

	public void setWeChatPay(BigDecimal weChatPay) {
		this.weChatPay = weChatPay;
	}

	public BigDecimal getAccountPay() {
		return accountPay;
	}

	public void setAccountPay(BigDecimal accountPay) {
		this.accountPay = accountPay;
	}

	public BigDecimal getCouponPay() {
		return couponPay;
	}

	public void setCouponPay(BigDecimal couponPay) {
		this.couponPay = couponPay;
	}

	public BigDecimal getChargePay() {
		return chargePay;
	}

	public void setChargePay(BigDecimal chargePay) {
		this.chargePay = chargePay;
	}

	public BigDecimal getRewardPay() {
		return rewardPay;
	}

	public void setRewardPay(BigDecimal rewardPay) {
		this.rewardPay = rewardPay;
	}

	public String getMarket(BigDecimal orderMoney,BigDecimal wechatPay,BigDecimal accountPay,BigDecimal couponPay,
			BigDecimal chargePay,BigDecimal rewardPay) {
		
		BigDecimal d1 = wechatPay.add(accountPay).add(couponPay).add(chargePay).add(rewardPay);
		BigDecimal d2 = orderMoney.subtract(d1);
		
		return d2.divide(d1,2,BigDecimal.ROUND_HALF_UP)+"";
		
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Integer getDistributionModeId() {
		return distributionModeId;
	}

	public void setDistributionModeId(Integer distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	public OrderDetailDto(String shopDetailId, String orderId, String shopName, Date beginTime, String telephone, BigDecimal orderMoney, BigDecimal weChatPay, BigDecimal accountPay,
						   BigDecimal couponPay, BigDecimal chargePay, BigDecimal moneyPay, BigDecimal backCartPay, BigDecimal rewardPay,
						   BigDecimal waitRedPay, String incomePrize, Boolean isChildOrder, BigDecimal otherPayment, BigDecimal aliPayment,
						   BigDecimal articleBackPay, Integer orderMode, String begin, String market, String level, String orderState,
						   BigDecimal integralPay, BigDecimal shanhuiPay, BigDecimal giveChangePayment) {
		this.shopDetailId = shopDetailId;
		this.orderId = orderId;
		this.shopName = shopName;
		this.beginTime = beginTime;
		this.telephone = telephone;
		this.orderMoney = orderMoney;
		this.weChatPay = weChatPay;
		this.accountPay = accountPay;
		this.couponPay = couponPay;
		this.chargePay = chargePay;
		this.moneyPay = moneyPay;
		this.backCartPay = backCartPay;
		this.rewardPay = rewardPay;
		this.waitRedPay = waitRedPay;
		this.incomePrize = incomePrize;
		this.isChildOrder = isChildOrder;
		this.otherPayment = otherPayment;
		this.aliPayment = aliPayment;
		this.articleBackPay = articleBackPay;
		this.orderMode = orderMode;
		this.begin = begin;
		this.market = market;
		this.level = level;
		this.orderState = orderState;
		this.integralPay = integralPay;
		this.shanhuiPay = shanhuiPay;
		this.giveChangePayment = giveChangePayment;
	}

	public OrderDetailDto(String shopDetailId, String orderId, String shopName, Date beginTime, String telephone, BigDecimal orderMoney, BigDecimal weChatPay, BigDecimal accountPay,
						  BigDecimal couponPay, BigDecimal chargePay, BigDecimal moneyPay, BigDecimal backCartPay, BigDecimal rewardPay,
						  BigDecimal waitRedPay, String incomePrize, Boolean isChildOrder, BigDecimal otherPayment, BigDecimal aliPayment,
						  BigDecimal articleBackPay, Integer orderMode, String begin, String market, String level, String orderState,
						  BigDecimal integralPay, BigDecimal shanhuiPay, BigDecimal giveChangePayment, BigDecimal refundCrashPayment,BigDecimal cardRechargePay, BigDecimal cardRechargeFreePay,
						  BigDecimal refundCardPay, BigDecimal cardDiscountPay, BigDecimal grantMoney) {
		this.shopDetailId = shopDetailId;
		this.orderId = orderId;
		this.shopName = shopName;
		this.beginTime = beginTime;
		this.telephone = telephone;
		this.orderMoney = orderMoney;
		this.weChatPay = weChatPay;
		this.accountPay = accountPay;
		this.couponPay = couponPay;
		this.chargePay = chargePay;
		this.moneyPay = moneyPay;
		this.backCartPay = backCartPay;
		this.rewardPay = rewardPay;
		this.waitRedPay = waitRedPay;
		this.incomePrize = incomePrize;
		this.isChildOrder = isChildOrder;
		this.otherPayment = otherPayment;
		this.aliPayment = aliPayment;
		this.articleBackPay = articleBackPay;
		this.orderMode = orderMode;
		this.begin = begin;
		this.market = market;
		this.level = level;
		this.orderState = orderState;
		this.integralPay = integralPay;
		this.shanhuiPay = shanhuiPay;
		this.giveChangePayment = giveChangePayment;
		this.refundCrashPayment = refundCrashPayment;
		this.groupPurchasePayment = BigDecimal.ZERO;
		this.cashCouponPayment = BigDecimal.ZERO;
		this.cardRechargePay = cardRechargePay;
		this.cardRechargeFreePay = cardRechargeFreePay;
		this.refundCardPay = refundCardPay;
		this.cardDiscountPay = cardDiscountPay;
		this.grantMoney = grantMoney;
	}
}





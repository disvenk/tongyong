package com.resto.api.brand.dto;


import com.resto.core.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OrderDetailDto implements Serializable {

	private static final long serialVersionUID = -3545925480050574818L;

	private String shopDetailId;//店铺id
	
	private String orderId;
	
	private String shopName;//店铺名字

	private String tableNumber;//桌号
	
	private Date beginTime ;//下单时间

    private String createTime;
	
	private String telephone;//订单数
	
	private BigDecimal orderMoney;//订单金额
	
	private BigDecimal weChatPay;//微信支付
	
	private BigDecimal accountPay;//红包支付
	
	private BigDecimal couponPay;//优惠券支付

	private BigDecimal chargePay;//充值金额支付

	private BigDecimal moneyPay;

	private BigDecimal backCartPay;//银行卡

	private BigDecimal rewardPay;//充值赠送支付

    private BigDecimal waitRedPay;//等位红包支付
	
	private String incomePrize;//营销撬动率

    private Boolean isChildOrder;//是否是子订单

	private BigDecimal otherPayment;
    //用户
	private BigDecimal aliPayment;

	private BigDecimal articleBackPay; //退菜红包

    private List<Map<String, Object>> shopOrderList;

    private BigDecimal integralPay;

    private BigDecimal shanhuiPay;

    private BigDecimal giveChangePayment;

	private Integer distributionModeId;//订单类型

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

	private String begin;

	private String market;//营销撬动
	
	private String level;//评价
	
	private String orderState;//订单状态

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

	public String getMarket(BigDecimal orderMoney,BigDecimal wechatPay,BigDecimal accountPay,BigDecimal couponPay,
			BigDecimal chargePay,BigDecimal rewardPay) {
		
		BigDecimal d1 = wechatPay.add(accountPay).add(couponPay).add(chargePay).add(rewardPay);
		BigDecimal d2 = orderMoney.subtract(d1);
		
		return d2.divide(d1,2,BigDecimal.ROUND_HALF_UP)+"";
		
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
}





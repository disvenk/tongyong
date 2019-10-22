package com.resto.shop.web.constant;

public class ProductionStatus {
	/**
	 * 已付款未下单
	 */
	public static final int NOT_ORDER = 0;

	/**
	 * 已支付，并且已下单
	 */
	public final static int HAS_ORDER=1;
	
	/**
	 * 已打印
	 */
	public final static int PRINTED=2; 
	
	/**
	 * 已叫号
	 */
	public final static int HAS_CALL=3;
	
	/**
	 * 已取餐(R+外卖、计费系统要用)
	 */
	public final static int GET_IT=4;

	/**
	 * 已下单未打印
	 */
	public final static int NOT_PRINT = 5;

	/**
	 * 订单菜品全是退完
	 */
	public final static int REFUND_ARTICLE = 6;


	public static String getStatusName(Integer productionStatus){
		switch (productionStatus) {
		case ProductionStatus.NOT_ORDER:
			return "已付款,未下单";
		case ProductionStatus.HAS_ORDER:
			return "已支付,并且已下单";
		case ProductionStatus.PRINTED:
			return "已打印";
		case ProductionStatus.HAS_CALL:
			return "已叫号";
		case ProductionStatus.GET_IT:
			return "已取餐";
		case ProductionStatus.NOT_PRINT:
			return "已下单,未打印";
		case ProductionStatus.REFUND_ARTICLE:
			return "退菜取消";
		default:
			return "未知状态";
		}
	}
}

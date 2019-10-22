package com.resto.shop.web.constant;

public class OrderState {
	/**
	 * 未提交 （购物车状态）///
	 */
	public static final int NOT_SUBMIT = 0;
	/**
	 * 已提交 未付款 ///
	 */
	public static final int SUBMIT = 1;			
	/**
	 * 已付款 ///
	 */
	public static final int PAYMENT= 2;			
	/**
	 * 已过期 ///
	 */
	public static final int EXPIRED = 8;		
	/**
	 * 已取消///
	 */
	public static final int CANCEL= 9;			//订单已取消
	
	/**
	 * 已确认
	 */
	public static final int CONFIRM=10;
	
	/**
	 * 已评价
	 */
	public static final int HASAPPRAISE=11;
	
	/**
	 * 已分享 
	 */
	public static final int SHARED =12;
	
	public static String getStateName(int state) {
		switch (state) {
		case NOT_SUBMIT:
			return "购物车";
		case SUBMIT:
			return "已提交,未付款";
		case PAYMENT:
			return "已支付";
		case EXPIRED:
			return "已过期";
		case CANCEL:
			return "已取消";
		case CONFIRM:
			return "已确认";
		case HASAPPRAISE:
			return "已评价";
		case SHARED:
			return "已分享";
		default:
			return"未知状态";
		}
	}
}

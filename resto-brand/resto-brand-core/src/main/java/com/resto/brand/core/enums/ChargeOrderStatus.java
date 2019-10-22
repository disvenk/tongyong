package com.resto.brand.core.enums;

/**
 * 短信充值订单状态
 * @author Administrator
 *
 */
public class ChargeOrderStatus {
	/**
	 * 未支付
	 */
	public static final int NO_PAY = 0 ;
	/**
	 * 已支付,并且已成功执行充值操作
	 */
	public static final int HAS_PAY = 1 ;
	/**
	 * 已支付,未成功充值
	 */
	public static final int NO_CHARGE = 2;
	/**
	 * 银行转账专属 状态，审核中
	 */
	public static final int AUDIT = 3;
}

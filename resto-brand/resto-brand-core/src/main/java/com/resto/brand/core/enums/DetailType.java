package com.resto.brand.core.enums;

/**
详情V
 *
 */
public class DetailType {

	public static final int NEW_CUSTOMER_REGISTER = 10; //新用户注册

	public static final int ORDER_SELL = 20; //消费订单抽成

	public static final int ORDER_REAL_SELL = 21 ;//消费订单实付抽成


	public static final int BACK_CUSTOMER_ORDER_SELL = 22; //回头用户消费订单抽成

	public static final int BACK_CUSTOMER_ORDER_REAL_SELL = 23;//回头用户消费实付订单

	public static final int RESTO_OUT_FOOD_ORDER_SELL = 24;//R+外卖订单抽成

	public static final  int RESTO_OUT_FOOD_ORDER_REAL_SELL =25; //R+外卖实付抽成

	public static final  int THIRD_OUT_FOOD_ORDER_SELL = 26;  //第三方外卖订单抽成
	public static final  int THIRD_OUT_FOOD_ORDER_REAL_SELL = 27; //第三方外卖订单实付抽成


	public static final  int SMS_CODE = 30;//短信发送 验证码

	public  static final int SMS_DAY_MESSAGE = 31;//结店短信

	public static final int BRAND_ACCOUNT_CHARGE = 40; //账户充值

	public static String DetailTypeName(int a){

		String temp = "未知";

		switch (a){
			case NEW_CUSTOMER_REGISTER:
				temp = "新用户注册";
				break;
			case ORDER_SELL:
				temp = "消费订单抽成";
				break;
			case ORDER_REAL_SELL:
				temp = "消费订单实付抽成";
				break;
			case BACK_CUSTOMER_ORDER_SELL:
				temp = "回头用户消费订单抽成";
				break;
			case BACK_CUSTOMER_ORDER_REAL_SELL:
				temp = "回头用户消费实付订单抽成";
				break;

			case RESTO_OUT_FOOD_ORDER_SELL:
				temp = "R+外卖订单抽成";
				break;
			case RESTO_OUT_FOOD_ORDER_REAL_SELL:
				temp = "R+外卖实付抽成";
				break;
			case THIRD_OUT_FOOD_ORDER_SELL:
				temp = "第三方外卖订单抽成";
				break;
			case THIRD_OUT_FOOD_ORDER_REAL_SELL:
				temp = "第三方外卖订单实付抽成";
				break;
			case SMS_CODE:
				temp = "短信验证码";
				break;
			case SMS_DAY_MESSAGE:
				temp = "结店短信";
				break;
			case BRAND_ACCOUNT_CHARGE:
				temp = "账户充值";
				break;
			default:
				break;
		}
		return  temp;
	}

}

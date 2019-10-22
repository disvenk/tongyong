package com.resto.brand.core.enums;

/**
 * 账户流水行为V
 * 用 10 , 20 ,30
 * 是为了以后如果需要区分 自然用户注册,分享用户注册 可以用 11 12,
 * 短信 是日结 , 注册 21,22 ==
 *
 */
public class BehaviorType {

	public static final int REGISTER = 10; //注册

	public static final int SELL = 20; //消费

	public static final  int SMS = 30;//短信

	public static  final int CHARGE =40 ;//充值

	public static String getBehaviorName(int a){

		String temp = "未知";

		switch (a){
			case REGISTER:
				temp = "注册";
				break;
			case SELL:
				temp = "消费";
				break;
			case SMS:
				temp = "短信";
				break;
			case CHARGE:
				temp = "充值";
				break;
			default:
				break;
		}
		return  temp;
	}

}

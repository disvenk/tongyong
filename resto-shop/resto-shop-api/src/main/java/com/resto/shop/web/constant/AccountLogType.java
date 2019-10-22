package com.resto.shop.web.constant;

public class AccountLogType {
	public final static int PAY=2;  //支出
	public final static int INCOME=1; //收入
	public final static int FROZEN=3; //冻结
	public final static int RELEASE=4; //释放  -->只在解冻余额时才会产生  只会由3-->4
}

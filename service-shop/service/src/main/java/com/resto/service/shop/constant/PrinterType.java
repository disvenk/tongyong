package com.resto.service.shop.constant;

public class PrinterType {
	
	
	public static final int KITCHEN = 1;    //厨房
	public static final int PACKAGE = 3;    //打包
	public static final int RECEPTION = 2; //前台


	public static final int TOTAL = 0;
	public static String getPrintType(int state) {
		switch (state) {
		case KITCHEN:
			return "厨房";
		case RECEPTION:
			return "前台";
		case PACKAGE:
			return "打包";
		default:
			return"未知类型";
		}
	}
}

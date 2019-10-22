package com.resto.shop.web.constant;

public class DistributionType {
	public final static int RESTAURANT_MODE_ID = 1;  //堂吃
	public final static int DELIVERY_MODE_ID = 2;    //外卖
	public static final int TAKE_IT_SELF = 3;		//外带
	public static final int REFUND_ORDER = 4;		//退菜
    public static final int MODIFY_ORDER = 5;		//pos端增加订单菜品数量
    public static final int REMINDER_ORDER = 6;
	public static final int BAD_APPRAISE_ORDER = 7;
	
	
	public static String getModeText(int mid){
		switch (mid) {
		case RESTAURANT_MODE_ID:
			return "堂吃";
		case DELIVERY_MODE_ID:
			return "外卖";
		case TAKE_IT_SELF:
			return "外带";
		case REFUND_ORDER:
			return "退菜";
        case MODIFY_ORDER:
            return "加菜";
        case REMINDER_ORDER:
            return "催菜";
		case BAD_APPRAISE_ORDER:
			return "差评";
		default:
			return "未知";
		}
	}
}

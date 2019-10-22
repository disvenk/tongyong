package com.resto.service.appraise.constant;

public class PayMode {

	public static final int WEIXIN_PAY = 1;  //微信支付
	public static final int ACCOUNT_PAY =2;  //红包支付
	public static final int COUPON_PAY=3;    //优惠券支付
	public static final int MONEY_PAY = 4;	//其他方式支付
	public static final int BANK_CART_PAY=5; //银行卡支付
	public static final int CHARGE_PAY = 6; //充值金额支付
	public static final int REWARD_PAY = 7; //充值赠送的金额支付
    public static final int WAIT_MONEY = 8; //等位红包
    public static final int HUNGER_MONEY = 9; //饿了吗
    public static final int ALI_PAY = 10; //支付宝
    public static final int ARTICLE_BACK_PAY = 11; //菜品退款支付
    public static final int CRASH_PAY= 12; //现金支付
    public static final int APPRAISE_RED_PAY = 13; //评论红包支付
    public static final int SHARE_RED_PAY = 14; //分享返利红包支付
    public static final int REFUND_ARTICLE_RED_PAY = 15; //退菜红包支付
    public static final int SHANHUI_PAY = 16; //闪惠支付  大众点评
    public static final int INTEGRAL_PAY = 17; //会员积分支付
    public static final int GIVE_CHANGE = 18; //找零
    public static final int REFUND_CRASH = 19; //现金退款
    public static final int THIRD_MONEY_RED_PAY = 20; //第三方储值余额支付
    public static final int REBATE_MONEY_RED_PAY = 21; //消费返利余额支付

    public static String getPayModeName(int state){
	    switch (state) {
            case WEIXIN_PAY:
                return "微信支付";
            case ACCOUNT_PAY:
                return "红包支付";
            case COUPON_PAY:
                return "优惠券支付";
            case MONEY_PAY:
                return "其他方式支付";
            case BANK_CART_PAY:
                return "银行卡支付";
            case CHARGE_PAY:
            	return "充值账户支付";
            case REWARD_PAY:
            	return "充值赠送支付";
            case WAIT_MONEY:
                return "等位红包支付";
            case HUNGER_MONEY:
                return "饿了吗支付";
            case ALI_PAY:
                return "支付宝支付";
            case ARTICLE_BACK_PAY:
                return "退菜返还余额";
            case CRASH_PAY:
                return "现金支付";
            case APPRAISE_RED_PAY:
                return "评论红包支付";
            case SHARE_RED_PAY:
                return "分享返利红包支付";
            case REFUND_ARTICLE_RED_PAY:
                return "退菜红包支付";
            case SHANHUI_PAY:
                return "闪惠支付";
            case INTEGRAL_PAY:
                return "会员支付";
            case GIVE_CHANGE:
                return "找零";
            case REFUND_CRASH:
                return "现金退款";
            case THIRD_MONEY_RED_PAY:
                return "第三方储值余额支付";
            case REBATE_MONEY_RED_PAY:
                return "消费返利余额支付";
            default:
                return "";
            }
	}

}

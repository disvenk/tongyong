package com.resto.shop.web.constant;

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
    public static final int SHANHUI_PAY = 16; //新美大支付  大众点评
    public static final int INTEGRAL_PAY = 17; //会员积分支付
    public static final int GIVE_CHANGE = 18; //找零
    public static final int REFUND_CRASH = 19; //现金退款
    public static final int THIRD_MONEY_RED_PAY = 20; //第三方储值余额支付
    public static final int REBATE_MONEY_RED_PAY = 21; //消费返利余额支付
    public static final int CARD_DISCOUNT_PAY = 22; //储值卡折扣支付                       //美食广场分之使用
    public static final int CARD_CHARGE_PAY = 23; //卡充值金额支付                         //美食广场分之使用
    public static final int CARD_REWARD_PAY = 24; //卡充值赠送金额支付 zhe                  //美食广场分之使用
    public static final int CARD_REFUND_PAY = 25; //退菜退款到充值卡                       //美食广场分之使用
    public static final int EXEMPTION_PAY= 26; //免单支付
    public static final int GROUP_PAY= 27; //团购支付
    public static final int CASH_COUPIN_PAY= 28; //代金券支付

    //以下仅仅只用于味千对接
    public static final int EMERSION_CASH_PAY= 29; //门店浮出零用金
    public static final int EMERSION_INCOME_PAY= 30; //门店浮出零找金
    public static final int TORAL_AMOUNT= 31; //销售总额
    public static final int EMERSION_DISCOUNT= 32; //折扣总额
    public static final int TORAL_NET_INCOME= 33; //净销售总额
    public static final int CASH_AMOUNT= 34; //现金合计
    public static final int BANK_CARD_TOTAL= 35; //银行卡合计
    public static final int RESTS_COUPON_TOTAL= 36; //其他卡券合计
    public static final int TORTAL_MONEY= 37; //合计
    public static final int TOTAL_REFUND= 38; //退款金额




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
                return "新美大支付";
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
            case CARD_DISCOUNT_PAY:
                return "储值卡折扣支付";
            case CARD_CHARGE_PAY:
                return "卡充值余额支付";
            case CARD_REWARD_PAY:
                return "卡充值赠送余额支付";
            case CARD_REFUND_PAY:
                return "退菜退款到充值卡";
            case EXEMPTION_PAY:
                return "免单支付";
            case GROUP_PAY:
                return "团购支付";
            case CASH_COUPIN_PAY:
                return "代金券支付";
            //以下仅仅只用于味千对接
            case EMERSION_CASH_PAY:
                return "门店浮出零用金";
            case EMERSION_INCOME_PAY:
                return "门店浮出零找金";
            case TORAL_AMOUNT:
                return "销售总额";
            case EMERSION_DISCOUNT:
                return "折扣总额";
            case TORAL_NET_INCOME:
                return "净销售总额";
            case CASH_AMOUNT:
                return "现金合计";
            case BANK_CARD_TOTAL:
                return "银行卡合计";
            case RESTS_COUPON_TOTAL:
                return "其他卡券合计";
            case TORTAL_MONEY:
                return "合计";
            case TOTAL_REFUND:
                return "退款金额";
            default:
                return "";
            }
	}
    public static String getRefundPayModeName(int state){
        switch (state) {
            case WEIXIN_PAY:
                return "请线下微信退款给顾客：";
            case ALI_PAY:
                return "请线下支付宝退款给顾客：";
            case ARTICLE_BACK_PAY:
                return "退菜红包返还：";
            case REFUND_CRASH:
                return "线下退还现金：";
            case COUPON_PAY:
                return "退还优惠券：";
            default:
                return "";
        }
    }

}

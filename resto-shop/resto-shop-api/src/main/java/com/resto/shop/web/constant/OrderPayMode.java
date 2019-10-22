package com.resto.shop.web.constant;

/**
 * Created by KONATA on 2016/11/22.
 */
public class OrderPayMode {

    public static final int YUE_PAY = 0;       //余额支付

    public static final int WX_PAY = 1;        //微信支付

    public static final int ALI_PAY = 2;       //支付宝支付

    public static final int YL_PAY = 3;       //银联支付

    public static final int XJ_PAY = 4;       //现金支付

    public static final int SHH_PAY = 5;       //新美大支付

    public static final int JF_PAY = 6;       //会员支付

    public static final int CARD_PAY = 7;     //美食广场分之使用

    public static final int MD_PAY = 8;       //免单支付

    public static final int GROUP_PAY = 9;      //团购支付

    public static final int CASH_COUPIN_PAY = 10;     //代金券支付

    public static final int ALL_TYPE_PAY = 99;      //混合支付


    public static String getPayModeName(int state){
        switch (state) {
            case WX_PAY:
                return "微信支付";
            case ALI_PAY:
                return "支付宝支付";
            case YL_PAY:
                return "银联支付";
            case XJ_PAY:
                return "现金支付";
            case SHH_PAY:
                return "新美大支付";
            case JF_PAY:
                return "会员支付";
            case CARD_PAY:
                return "储值卡支付";
            case MD_PAY:
                return "免单支付";
            case GROUP_PAY:
                return "团购支付 ß";
            case CASH_COUPIN_PAY:
                return "代金券支付";
            case ALL_TYPE_PAY:
                return "混合支付";
            default:
                return "余额支付";
        }
    }

    public static Integer getPayModeByPaymentMode(int payMode){
        switch (payMode) {
            case PayMode.WEIXIN_PAY:
                return WX_PAY;
            case PayMode.ALI_PAY:
                return ALI_PAY;
            case PayMode.ARTICLE_BACK_PAY:
                return YUE_PAY;
            case PayMode.REFUND_CRASH:
                return XJ_PAY;
            default:
                return 0;
        }
    }
}

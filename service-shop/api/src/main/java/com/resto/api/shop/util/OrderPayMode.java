package com.resto.api.shop.util;

/**
 * Created by KONATA on 2016/11/22.
 */
public class OrderPayMode {

    public static final int YUE_PAY = 0;

    public static final int WX_PAY = 1;

    public static final int ALI_PAY = 2;

    public static final int YL_PAY = 3;

    public static final int XJ_PAY = 4;

    public static final int SHH_PAY = 5;

    public static final int JF_PAY = 6;

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
                return "闪惠支付";
            case JF_PAY:
                return "会员支付";
            default:
                return "余额支付";
        }
    }
}

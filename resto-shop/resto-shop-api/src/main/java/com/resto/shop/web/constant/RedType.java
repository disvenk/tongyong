package com.resto.shop.web.constant;

import java.util.HashMap;
import java.util.Map;

public class RedType {

    //评论红包
    public static final int APPRAISE_RED = 0;

    //分享返利红包
    public static final int SHARE_RED = 1;

    //退菜红包
    public static final int REFUND_ARTICLE_RED = 2;

    //第三方账户余额
    public static final int THIRD_MONEY = 3;

    //消费返利
    public static final int REBATE_MONEY = 4;

    public static final Map<Integer, String> GETREDTYPE = new HashMap<Integer, String>(){
        {
            put(APPRAISE_RED,"评论红包");
            put(SHARE_RED,"分享返利红包");
            put(REFUND_ARTICLE_RED,"退菜红包");
            put(THIRD_MONEY,"第三方储值余额");
            put(REBATE_MONEY,"消费返利余额");
        }
    };
}

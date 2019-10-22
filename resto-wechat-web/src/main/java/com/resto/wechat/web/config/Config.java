package com.resto.wechat.web.config;

public class Config {
    public static final String IMAGE_SERVER = "http://op.saladplus.restoplus.cn/";
    //博力支付方式
    public static final String PAY_TYPE_FORTH = "pay";

    //返回给前端的表示请求成功的返回值
    public static final int SUCCESS_CODE = 200;

    //返回给前端的表示请求失败的返回值
    public static final int ERROR_CODE = 100;

    public static final int OPEN = 1;




    //非套餐
    public static final String ARTICLE = "1";

    //套餐
    public static final String TOTAL_ARTICLE = "2";

    //套餐子品
    public static final String CHILD_ARTICLE = "3";

    //无规格推荐配餐主品
    public static final String SINGLE_ARTICLE = "4";

    //推荐配餐
    public static final String RECOMMEND_ARTICLE = "5";

    //新规格单品
    public static final String NEW_UNIT_ARTICLE = "6";

    //新规格属性
    public static final String NEW_UNIT_DETAIL = "7";

    //重量包属性单品
    public static final String WEIGHT_PACKAGE_ARTICLE = "8";

    //重量包属性明细
    public static final String WEIGHT_PACKAGE_ARTICLE_DETAIL = "9";


}

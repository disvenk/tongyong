package com.resto.shop.web.config;

public class SessionKey {
	public static final String USER_INFO="USER_INFO";

	public static final String USER_INFO_RESOUCE="USER_INFO_RESOUCE";     //用户的来源   1 bo(brand_user)   2 shop(tb_brand_user)
	public static final String CURRENT_BRAND_ID = "current_brand_id";
	public static final String CURRENT_SHOP_ID = "current_shop_id";
	public static final String CURRENT_SHOP_NAME = "current_shop_name";
	public static final String CURRENT_BRAND_NAME = "current_brand_name";
    public static final String CURRENT_SHOP_NAMES = "current_shop_names";
    public static  final  String WETHERINFO = "wether_info";//当日天气相关信息

	public static final String OPEN_BRAND_ACCOUNT = "open_brand_account";//是否开启品牌账户 1.开启了品牌账户但是余额小于最低设置 设置为false 默认是true


}

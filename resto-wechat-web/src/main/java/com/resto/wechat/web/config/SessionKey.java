package com.resto.wechat.web.config;

public class SessionKey {


	public static final String USER_INFO="USER_INFO";
	public static final String CURRENT_BRAND_ID = "current_brand_id";
	public static final String CURRENT_WECHAT_CONFIG = "current_wechat_config";
	public static final String CURRENT_BRAND = "current_brand";
	public static final String CURRENT_CUSTOMER= "current_customer";
	public static final String SHOP_LIST = "shop_list";
	public static final String SHOP_MAP = "shop_map";
	public static final String CURRENT_SHOP = "current_shop";

	/**
	 * yz 2017/07/29 计费系统存 品牌账户相关信息
	 * 不存品牌账户信息 是因为品牌账户的数据会一直变动
	 * 而 品牌账户设置一般不会随意更改
	 */
	public static final String OPENBRANDACCOUNT = "open_brandAccount";//判断是否开启了品牌账户
	public static final String CURRENT_ACCOUNT_SETTING = "account_setting"; //存账户设置相关信息

}

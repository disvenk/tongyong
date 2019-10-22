package com.resto.shop.web.constant;

public class OrderItemType {
	public final static int ARTICLE=1; //餐品类型
	public final static int UNITPRICE=2;  //价格类型
	public static final int SETMEALS = 3; //套餐类型
	public static final int MEALS_CHILDREN = 4; //套餐子项
	public static final int UNIT_NEW = 5; //新规格
	public static final int RECOMMEND = 6; //推荐餐包
	public static final int WEIGHT_PACKAGE_ARTICLE = 8; //重量包属性单品


	public static String getPayModeName(int state){
		switch (state) {
			case ARTICLE:
				return "单品";
			case UNITPRICE:
				return "多规格单品";
			case SETMEALS:
				return "套餐";
			case MEALS_CHILDREN:
				return "套餐子品";
			case UNIT_NEW:
				return "新规格单品";
			case RECOMMEND:
				return "推荐餐品";
			case WEIGHT_PACKAGE_ARTICLE:
				return "重量包属性餐品";
			default:
				return "未知";
		}
	}

}

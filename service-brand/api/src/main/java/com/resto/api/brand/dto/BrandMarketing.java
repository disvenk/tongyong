package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 品牌营销报表
 * @author Administrator
 *
 */
@Data
public class BrandMarketing implements Serializable {

	private String brandName;
	
	private String redMoneyAll;
	
	private String plRedMoney; 

	private String czRedMoney; 
	
	private String fxRedMoney; 
	
	private String dwRedMoney; 
	
	private String tcRedMoney; 
	
	private String couponAllMoney; 
	
	private String zcCouponMoney; 
	
	private String yqCouponMoney;

    private String birthCouponMoney;

}

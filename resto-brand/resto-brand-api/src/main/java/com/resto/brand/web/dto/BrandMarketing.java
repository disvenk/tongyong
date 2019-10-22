package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 * 品牌营销报表
 * @author Administrator
 *
 */
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

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getPlRedMoney() {
		return plRedMoney;
	}

	public void setPlRedMoney(String plRedMoney) {
		this.plRedMoney = plRedMoney;
	}

	public String getCzRedMoney() {
		return czRedMoney;
	}

	public void setCzRedMoney(String czRedMoney) {
		this.czRedMoney = czRedMoney;
	}

	public String getFxRedMoney() {
		return fxRedMoney;
	}

	public void setFxRedMoney(String fxRedMoney) {
		this.fxRedMoney = fxRedMoney;
	}

	public String getDwRedMoney() {
		return dwRedMoney;
	}

	public void setDwRedMoney(String dwRedMoney) {
		this.dwRedMoney = dwRedMoney;
	}

	public String getTcRedMoney() {
		return tcRedMoney;
	}

	public void setTcRedMoney(String tcRedMoney) {
		this.tcRedMoney = tcRedMoney;
	}

	public String getCouponAllMoney() {
		return couponAllMoney;
	}

	public void setCouponAllMoney(String couponAllMoney) {
		this.couponAllMoney = couponAllMoney;
	}

	public String getZcCouponMoney() {
		return zcCouponMoney;
	}

	public void setZcCouponMoney(String zcCouponMoney) {
		this.zcCouponMoney = zcCouponMoney;
	}

	public String getYqCouponMoney() {
		return yqCouponMoney;
	}

	public void setYqCouponMoney(String yqCouponMoney) {
		this.yqCouponMoney = yqCouponMoney;
	}

    public String getBirthCouponMoney() {
        return birthCouponMoney;
    }

    public void setBirthCouponMoney(String birthCouponMoney) {
        this.birthCouponMoney = birthCouponMoney;
    }

    public BrandMarketing() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getRedMoneyAll() {
		return redMoneyAll;
	}

	public void setRedMoneyAll(String redMoneyAll) {
		this.redMoneyAll = redMoneyAll;
	}

	public BrandMarketing(String brandName, String redMoneyAll, String plRedMoney, String czRedMoney, String fxRedMoney,
			String dwRedMoney, String tcRedMoney, String couponAllMoney, String zcCouponMoney, String yqCouponMoney,String birthCouponMoney) {
		super();
		this.brandName = brandName;
		this.redMoneyAll = redMoneyAll;
		this.plRedMoney = plRedMoney;
		this.czRedMoney = czRedMoney;
		this.fxRedMoney = fxRedMoney;
		this.dwRedMoney = dwRedMoney;
		this.tcRedMoney = tcRedMoney;
		this.couponAllMoney = couponAllMoney;
		this.zcCouponMoney = zcCouponMoney;
		this.yqCouponMoney = yqCouponMoney;
        this.birthCouponMoney = birthCouponMoney;
	}

}

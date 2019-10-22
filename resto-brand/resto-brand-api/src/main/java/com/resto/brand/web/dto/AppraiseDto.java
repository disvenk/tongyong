package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收入统计  （用于报表）
 * @author lmx
 *
 */
public class AppraiseDto implements Serializable {
	
	private String shopId;
	
	private String brandName;//品牌
	
	private String shopName;//店铺
	
	private int appraiseNum;//评价单数
	
	private String appraiseRatio;//评价率
	
	private BigDecimal redMoney;//红包总额
	
	private BigDecimal totalMoney;//订单总额
	
	private String redRatio;//红包撬动率
	
	private int onestar;//一星
	private int twostar;//二星
	private int threestar;//三星
	private int fourstar;//四星
	private int fivestar;//五星
	
	private String name; //报表下载用,让品牌和店铺名字显示在同一列上

    private Map<String, Object> brandAppraise;
    private List<Map<String, Object>> shopAppraises;


    public Map<String, Object> getBrandAppraise() {
        return brandAppraise;
    }

    public void setBrandAppraise(Map<String, Object> brandAppraise) {
        this.brandAppraise = brandAppraise;
    }

    public List<Map<String, Object>> getShopAppraises() {
        return shopAppraises;
    }

    public void setShopAppraises(List<Map<String, Object>> shopAppraises) {
        this.shopAppraises = shopAppraises;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getAppraiseRatio() {
		return appraiseRatio;
	}
	public void setAppraiseRatio(String appraiseRatio) {
		this.appraiseRatio = appraiseRatio;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getAppraiseNum() {
		return appraiseNum;
	}
	public void setAppraiseNum(int appraiseNum) {
		this.appraiseNum = appraiseNum;
	}
	public BigDecimal getRedMoney() {
		return redMoney;
	}
	public void setRedMoney(BigDecimal redMoney) {
		this.redMoney = redMoney;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getRedRatio() {
		return redRatio;
	}
	public void setRedRatio(String redRatio) {
		this.redRatio = redRatio;
	}
	public int getOnestar() {
		return onestar;
	}
	public void setOnestar(int onestar) {
		this.onestar = onestar;
	}
	public int getTwostar() {
		return twostar;
	}
	public void setTwostar(int twostar) {
		this.twostar = twostar;
	}
	public int getThreestar() {
		return threestar;
	}
	public void setThreestar(int threestar) {
		this.threestar = threestar;
	}
	public int getFourstar() {
		return fourstar;
	}
	public void setFourstar(int fourstar) {
		this.fourstar = fourstar;
	}
	public int getFivestar() {
		return fivestar;
	}
	public void setFivestar(int fivestar) {
		this.fivestar = fivestar;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public AppraiseDto(String shopId,String shopName, int appraiseNum, String appraiseRatio, BigDecimal redMoney,
			BigDecimal totalMoney, String redRatio, int onestar, int twostar, int threestar, int fourstar,
			int fivestar) {
		super();
		this.shopId=shopId;
		this.shopName = shopName;
		this.appraiseNum = appraiseNum;
		this.appraiseRatio = appraiseRatio;
		this.redMoney = redMoney;
		this.totalMoney = totalMoney;
		this.redRatio = redRatio;
		this.onestar = onestar;
		this.twostar = twostar;
		this.threestar = threestar;
		this.fourstar = fourstar;
		this.fivestar = fivestar;
	}
	public AppraiseDto() {
		super();
	}
	
	
	
	
	
}

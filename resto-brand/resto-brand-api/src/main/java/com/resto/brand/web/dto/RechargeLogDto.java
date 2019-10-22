package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 主要用于统计充值报表，下载报表
 * @author yangwei
 * RechargeLogDto
 * 2017年1月5日
 */

public class RechargeLogDto implements Serializable {
	
    //用于品牌充值记录
	
	private String brandName;
	private BigDecimal rechargeCount; //充值总数
	private BigDecimal rechargeNum;//充值总额
	private BigDecimal rechargeGaNum;//充值赠送总额
	private BigDecimal rechargeWeChat;//微信端充值
	private BigDecimal rechargePos;//POS端充值
	private BigDecimal rechargeCsNum;//充值消费总额
	private BigDecimal rechargeGaCsNum;//充值赠送消费总额
	private BigDecimal rechargeSpNum;//充值剩余总额
	private BigDecimal rechargeGaSpNum;//充值赠送剩余总额
    private String date;

	//用于查询店铺充值记录
	private String shopId;
	private String shopName;//店铺名
	private BigDecimal shopCount;//店铺充值总数
	private BigDecimal shopNum;//店铺充值总额
	private BigDecimal shopGaNum;//店铺充值赠送总额
	private BigDecimal shopWeChat;//店铺微信端充值
	private BigDecimal shopPos;//店铺POS端充值
	private BigDecimal shopCsNum;//店铺充值消费
	private BigDecimal shopGaCsNum;//店铺充值赠送消费
    private Map<String, Object> brandChargeLogs;
    private List<Map<String, Object>> shopChargeLogs;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Object> getBrandChargeLogs() {
        return brandChargeLogs;
    }

    public void setBrandChargeLogs(Map<String, Object> brandChargeLogs) {
        this.brandChargeLogs = brandChargeLogs;
    }

    public List<Map<String, Object>> getShopChargeLogs() {
        return shopChargeLogs;
    }

    public void setShopChargeLogs(List<Map<String, Object>> shopChargeLogs) {
        this.shopChargeLogs = shopChargeLogs;
    }

    public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public BigDecimal getShopCount() {
		return shopCount;
	}
	public void setShopCount(BigDecimal shopCount) {
		this.shopCount = shopCount;
	}
	public BigDecimal getShopNum() {
		return shopNum;
	}
	public void setShopNum(BigDecimal shopNum) {
		this.shopNum = shopNum;
	}
	public BigDecimal getShopGaNum() {
		return shopGaNum;
	}
	public void setShopGaNum(BigDecimal shopGaNum) {
		this.shopGaNum = shopGaNum;
	}
	public BigDecimal getShopWeChat() {
		return shopWeChat;
	}
	public void setShopWeChat(BigDecimal shopWeChat) {
		this.shopWeChat = shopWeChat;
	}
	public BigDecimal getShopPos() {
		return shopPos;
	}
	public void setShopPos(BigDecimal shopPos) {
		this.shopPos = shopPos;
	}
	public BigDecimal getShopCsNum() {
		return shopCsNum;
	}
	public void setShopCsNum(BigDecimal shopCsNum) {
		this.shopCsNum = shopCsNum;
	}
	public BigDecimal getShopGaCsNum() {
		return shopGaCsNum;
	}
	public void setShopGaCsNum(BigDecimal shopGaCsNum) {
		this.shopGaCsNum = shopGaCsNum;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public BigDecimal getRechargeCount() {
		return rechargeCount;
	}
	public void setRechargeCount(BigDecimal rechargeCount) {
		this.rechargeCount = rechargeCount;
	}
	public BigDecimal getRechargeNum() {
		return rechargeNum;
	}
	public void setRechargeNum(BigDecimal rechargeNum) {
		this.rechargeNum = rechargeNum;
	}
	public BigDecimal getRechargeGaNum() {
		return rechargeGaNum;
	}
	public void setRechargeGaNum(BigDecimal rechargeGaNum) {
		this.rechargeGaNum = rechargeGaNum;
	}
	public BigDecimal getRechargeWeChat() {
		return rechargeWeChat;
	}
	public void setRechargeWeChat(BigDecimal rechargeWeChat) {
		this.rechargeWeChat = rechargeWeChat;
	}
	public BigDecimal getRechargePos() {
		return rechargePos;
	}
	public void setRechargePos(BigDecimal rechargePos) {
		this.rechargePos = rechargePos;
	}
	public BigDecimal getRechargeCsNum() {
		return rechargeCsNum;
	}
	public void setRechargeCsNum(BigDecimal rechargeCsNum) {
		this.rechargeCsNum = rechargeCsNum;
	}
	public BigDecimal getRechargeGaCsNum() {
		return rechargeGaCsNum;
	}
	public void setRechargeGaCsNum(BigDecimal rechargeGaCsNum) {
		this.rechargeGaCsNum = rechargeGaCsNum;
	}
	public BigDecimal getRechargeSpNum() {
		return rechargeSpNum;
	}
	public void setRechargeSpNum(BigDecimal rechargeSpNum) {
		this.rechargeSpNum = rechargeSpNum;
	}
	public BigDecimal getRechargeGaSpNum() {
		return rechargeGaSpNum;
	}
	public void setRechargeGaSpNum(BigDecimal rechargeGaSpNum) {
		this.rechargeGaSpNum = rechargeGaSpNum;
	}
	public RechargeLogDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RechargeLogDto(String brandName,BigDecimal rechargeCount, BigDecimal rechargeNum, BigDecimal rechargeGaNum,
			BigDecimal rechargeWeChat, BigDecimal rechargePos, BigDecimal rechargeCsNum, BigDecimal rechargeGaCsNum,
			BigDecimal rechargeSpNum, BigDecimal rechargeGaSpNum) {
		super();
		this.brandName = brandName;
		this.rechargeCount = rechargeCount;
		this.rechargeNum = rechargeNum;
		this.rechargeGaNum = rechargeGaNum;
		this.rechargeWeChat = rechargeWeChat;
		this.rechargePos = rechargePos;
		this.rechargeCsNum = rechargeCsNum;
		this.rechargeGaCsNum = rechargeGaCsNum;
		this.rechargeSpNum = rechargeSpNum;
		this.rechargeGaSpNum = rechargeGaSpNum;
	}
	public RechargeLogDto(String shopId,String shopName, BigDecimal shopCount, BigDecimal shopNum, BigDecimal shopGaNum,
			BigDecimal shopWeChat, BigDecimal shopPos, BigDecimal shopCsNum, BigDecimal shopGaCsNum) {
		super();
		this.shopId = shopId;
		this.shopName = shopName;
		this.shopCount = shopCount;
		this.shopNum = shopNum;
		this.shopGaNum = shopGaNum;
		this.shopWeChat = shopWeChat;
		this.shopPos = shopPos;
		this.shopCsNum = shopCsNum;
		this.shopGaCsNum = shopGaCsNum;
	}

    public RechargeLogDto(String date,BigDecimal shopCount, BigDecimal shopNum, BigDecimal shopGaNum,
                          BigDecimal shopWeChat, BigDecimal shopPos, BigDecimal shopCsNum, BigDecimal shopGaCsNum) {
        super();
        this.date = date;
        this.shopCount = shopCount;
        this.shopNum = shopNum;
        this.shopGaNum = shopGaNum;
        this.shopWeChat = shopWeChat;
        this.shopPos = shopPos;
        this.shopCsNum = shopCsNum;
        this.shopGaCsNum = shopGaCsNum;
    }
	
}
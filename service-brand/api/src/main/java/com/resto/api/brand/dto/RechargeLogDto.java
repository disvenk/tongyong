package com.resto.api.brand.dto;

import lombok.Data;

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
@Data
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
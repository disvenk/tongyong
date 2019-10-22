package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ChargeSetting implements Serializable{
    private String id;
    
    @NotNull(message="充值金额不能为空")
    @Min(message="充值金额最小为0",value=0)
    private BigDecimal chargeMoney;
    
    @NotNull(message="返还金额不能为空")
    @Min(message="返还金额最小为0",value=0)
    private BigDecimal rewardMoney;
    
    @NotNull(message="是否显示到菜单栏上不能为空")
    private Byte showIn;

    private String labelText;
    
    @NotNull(message="排序不能为空")
    private Integer sort;
    
    @NotNull(message="活动状态不能为空")
    private Byte state;

    private Date createTime;

    private String brandId;
    
    private String shopDetailId;

    @NotNull(message="赠送金额到账天数不能为空")
    @Min(message="赠送金额到账天数最小值为1",value=1)
    private Integer numberDay;

    // 1.首冲 2多冲 1,2首冲+多冲
    private  String firstCharge;

    public String getFirstCharge() {
        return firstCharge;
    }

    public void setFirstCharge(String firstCharge) {
        this.firstCharge = firstCharge;
    }

    public String getShopDetailId() {
		return shopDetailId;
	}

	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public BigDecimal getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(BigDecimal rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public Byte getShowIn() {
        return showIn;
    }

    public void setShowIn(Byte showIn) {
        this.showIn = showIn;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText == null ? null : labelText.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public Integer getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(Integer numberDay) {
        this.numberDay = numberDay;
    }
}
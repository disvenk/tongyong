package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ChargePayment implements Serializable {
    private String id;

    private BigDecimal paymentMoney;

    private Date createTime;

    private String chargeOrderId;

    private String payData;
    
    //关联查询充值订单的内容
    //1.返还的金额
    private BigDecimal rewardMoney;
    
    //关联查询客户内容
    //1.客户的电话
    private String telephone;
    
    //查询品牌的名称 可以直接从session中获取
    private String brandName;
    //查询店铺的名称
    private String shopDetailName;
    
    //记录店铺的id
    private String shopDetailId;

    private Integer isUseBonus;

    public Integer getIsUseBonus() {
        return isUseBonus;
    }

    public void setIsUseBonus(Integer isUseBonus) {
        this.isUseBonus = isUseBonus;
    }

    public String getShopDetailId() {
		return shopDetailId;
	}

	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getShopDetailName() {
		return shopDetailName;
	}

	public void setShopDetailName(String shopDetailName) {
		this.shopDetailName = shopDetailName;
	}

	public BigDecimal getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(BigDecimal rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

	public String getTelephoe() {
		return telephone;
	}

	public void setTelephoe(String telephone) {
		this.telephone = telephone;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(BigDecimal paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChargeOrderId() {
        return chargeOrderId;
    }

    public void setChargeOrderId(String chargeOrderId) {
        this.chargeOrderId = chargeOrderId == null ? null : chargeOrderId.trim();
    }

    public String getPayData() {
        return payData;
    }

    public void setPayData(String payData) {
        this.payData = payData == null ? null : payData.trim();
    }
}
package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BrandAccountLog implements Serializable {
    private Long id;

    private Date createTime;

    //主题V ｛注册+短信+充值 指的是品牌名字｝｛消费指的是店铺名字｝
    private String groupName;

    //行为V 10注册 20消费  30 短信
    private Integer behavior;


    private String behaviorName;//行为名字 用于报表显示

    //资金变动
    private BigDecimal foundChange;

    //账户余额
    private BigDecimal remain;

    //详情V
    private Integer detail;

    private String detailName;//详情名字 用于报表显示

    private Integer accountId;

    private String brandId;

    private String shopId;

    //流水号
    private String serialNumber;


    private  BigDecimal orderMoney; //如果是消费类型 则记录订单金额


	private Boolean isParent;

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean parent) {
		isParent = parent;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Integer getBehavior() {
        return behavior;
    }

    public void setBehavior(Integer behavior) {
        this.behavior = behavior;
    }

    public BigDecimal getFoundChange() {
        return foundChange;
    }

    public void setFoundChange(BigDecimal foundChange) {
        this.foundChange = foundChange;
    }

    public BigDecimal getRemain() {
        return remain;
    }

    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    public Integer getDetail() {
        return detail;
    }

    public void setDetail(Integer detail) {
        this.detail = detail;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

	public String getBehaviorName() {
		return behaviorName;
	}

	public void setBehaviorName(String behaviorName) {
		this.behaviorName = behaviorName;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
}
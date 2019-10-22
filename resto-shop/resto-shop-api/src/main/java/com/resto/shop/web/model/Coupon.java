package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Coupon implements Serializable {
    private String id;

    private String name;

    private BigDecimal value;

    private BigDecimal minAmount;

    private Date beginDate;

    private Date endDate;

    private Date beginTime;

    private Date endTime;

    private Boolean isUsed;

    private Date usingTime;

    private String couponSource;

    private Boolean useWithAccount;

    private String remark;

    private Integer distributionModeId;

    private String customerId;
    
    private Integer couponType;//优惠券类型(-1:通用,0:新用户注册,1:邀请注册,2:生日)

    private  String shopDetailId;

    private  String brandId;

    private Integer pushDay;//推送天数(默认3天)

    private Integer recommendDelayTime;

    private Date addTime;

    private Long newCustomCouponId;

    private Integer smallProgramId;

    private String useShopId;

    private String shopName;

    private Integer deductionType;

    public Integer getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(Integer deductionType) {
        this.deductionType = deductionType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getSmallProgramId() {
        return smallProgramId;
    }

    public void setSmallProgramId(Integer smallProgramId) {
        this.smallProgramId = smallProgramId;
    }

    public String getUseShopId() {
        return useShopId;
    }

    public void setUseShopId(String useShopId) {
        this.useShopId = useShopId;
    }

    private List<CouponShopArticles> couponShopArticlesList;

    public List<CouponShopArticles> getCouponShopArticlesList() {
        return couponShopArticlesList;
    }

    public void setCouponShopArticlesList(List<CouponShopArticles> couponShopArticlesList) {
        this.couponShopArticlesList = couponShopArticlesList;
    }

    public Long getNewCustomCouponId() {
        return newCustomCouponId;
    }

    public void setNewCustomCouponId(Long newCustomCouponId) {
        this.newCustomCouponId = newCustomCouponId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getRecommendDelayTime() {
        return recommendDelayTime;
    }

    public void setRecommendDelayTime(Integer recommendDelayTime) {
        this.recommendDelayTime = recommendDelayTime;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Date getUsingTime() {
        return usingTime;
    }

    public void setUsingTime(Date usingTime) {
        this.usingTime = usingTime;
    }

    public String getCouponSource() {
        return couponSource;
    }

    public void setCouponSource(String couponSource) {
        this.couponSource = couponSource == null ? null : couponSource.trim();
    }

    public Boolean getUseWithAccount() {
        return useWithAccount;
    }

    public void setUseWithAccount(Boolean useWithAccount) {
        this.useWithAccount = useWithAccount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getDistributionModeId() {
        return distributionModeId;
    }

    public void setDistributionModeId(Integer distributionModeId) {
        this.distributionModeId = distributionModeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Integer getPushDay() {
		return pushDay;
	}

	public void setPushDay(Integer pushDay) {
		this.pushDay = pushDay;
	}
    
}
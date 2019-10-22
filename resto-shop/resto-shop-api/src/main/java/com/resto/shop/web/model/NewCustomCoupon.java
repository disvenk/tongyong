package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.resto.brand.web.model.ShopDetail;
import org.springframework.format.annotation.DateTimeFormat;

public class NewCustomCoupon implements Serializable{
    private Long id;

    private String name;

    //@NumberFormat(style=Style.NUMBER,pattern="#.##")
    @NotNull(message="优惠券的价值不能为空")
    @Min(message="优惠券的价值最小值0" ,value=0)
    private BigDecimal couponValue;//优惠券的价值

    private Integer couponValiday;

    @NotNull(message="优惠券的个数不能为空")
    private Integer couponNumber;

    private Date createTime;

    private Boolean useWithAccount;

    private String couponName;

    @NotNull(message="优惠券的最低消费额度不能为空")
    @Min(message="最低消费额度为0",value=0)
    private BigDecimal couponMinMoney;

    @DateTimeFormat(pattern=("HH:mm"))
    private Date beginTime;

    @DateTimeFormat(pattern=("HH:mm"))
    private Date endTime;

    @NotNull(message="是否启用不能为空")
    private Boolean isActivty;

    private String brandId;

    private Integer couponType;//优惠券类型(-1:通用,0:新用户注册,1:邀请注册)

    @NotNull(message="配送模式不能为空")
    private Integer distributionModeId;

    /**
     * 新增字段用来区别优惠券使用时间的类型
     * 1.按天算 2.按范围算
     */
    @NotNull(message="优惠券类型不能为空")
    private Integer timeConsType;



    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date beginDateTime;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date endDateTime;

    private String shopDetailId;

    private Integer pushDay;//到期推送时间（默认为3天）

    private Integer recommendDelayTime;

    private Integer distanceBirthdayDay;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date realTimeCouponBeginTime;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date realTimeCouponEndTime;

    private String realTimeCouponBeginTimeString;

    private String realTimeCouponEndTimeString;

    private BigDecimal minimumAmount;

    private Integer nextHour;

    private String couponPhoto;

    private Integer deductionType;

    public Integer getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(Integer deductionType) {
        this.deductionType = deductionType;
    }

    public String getCouponPhoto() {
        return couponPhoto;
    }

    public void setCouponPhoto(String couponPhoto) {
        this.couponPhoto = couponPhoto;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public Integer getNextHour() {
        return nextHour;
    }

    public void setNextHour(Integer nextHour) {
        this.nextHour = nextHour;
    }

    public String getRealTimeCouponBeginTimeString() {
        return realTimeCouponBeginTimeString;
    }

    public void setRealTimeCouponBeginTimeString(String realTimeCouponBeginTimeString) {
        this.realTimeCouponBeginTimeString = realTimeCouponBeginTimeString;
    }

    public String getRealTimeCouponEndTimeString() {
        return realTimeCouponEndTimeString;
    }

    public void setRealTimeCouponEndTimeString(String realTimeCouponEndTimeString) {
        this.realTimeCouponEndTimeString = realTimeCouponEndTimeString;
    }

    public Date getRealTimeCouponBeginTime() {
        return realTimeCouponBeginTime;
    }

    public void setRealTimeCouponBeginTime(Date realTimeCouponBeginTime) {
        this.realTimeCouponBeginTime = realTimeCouponBeginTime;
    }

    public Date getRealTimeCouponEndTime() {
        return realTimeCouponEndTime;
    }

    public void setRealTimeCouponEndTime(Date realTimeCouponEndTime) {
        this.realTimeCouponEndTime = realTimeCouponEndTime;
    }

    public Integer getDistanceBirthdayDay() {
        return distanceBirthdayDay;
    }

    public void setDistanceBirthdayDay(Integer distanceBirthdayDay) {
        this.distanceBirthdayDay = distanceBirthdayDay;
    }

    public Integer getRecommendDelayTime() {
        return recommendDelayTime;
    }

    public void setRecommendDelayTime(Integer recommendDelayTime) {
        this.recommendDelayTime = recommendDelayTime;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    private  Integer isBrand;//是否是品牌优惠券 1，是 0，店铺优惠券

    private String shopName;//如果是店铺优惠券，显示店铺的名字

    public Boolean getActivty() {
        return isActivty;
    }

    public void setActivty(Boolean activty) {
        isActivty = activty;
    }

    public Integer getTimeConsType() {
        return timeConsType;
    }

    public void setTimeConsType(Integer timeConsType) {
        this.timeConsType = timeConsType;
    }

    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(BigDecimal couponValue) {
        this.couponValue = couponValue;
    }

    public Integer getCouponValiday() {
        return couponValiday;
    }

    public void setCouponValiday(Integer couponValiday) {
        this.couponValiday = couponValiday;
    }

    public Integer getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(Integer couponNumber) {
        this.couponNumber = couponNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getUseWithAccount() {
        return useWithAccount;
    }

    public void setUseWithAccount(Boolean useWithAccount) {
        this.useWithAccount = useWithAccount;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    public BigDecimal getCouponMinMoney() {
        return couponMinMoney;
    }

    public void setCouponMinMoney(BigDecimal couponMinMoney) {
        this.couponMinMoney = couponMinMoney;
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

    public Boolean getIsActivty() {
        return isActivty;
    }

    public void setIsActivty(Boolean isActivty) {
        this.isActivty = isActivty;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public Integer getDistributionModeId() {
        return distributionModeId;
    }

    public void setDistributionModeId(Integer distributionModeId) {
        this.distributionModeId = distributionModeId;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Integer getIsBrand() {
        return isBrand;
    }

    public void setIsBrand(Integer isBrand) {
        this.isBrand = isBrand;
    }

	public Integer getPushDay() {
		return pushDay;
	}

	public void setPushDay(Integer pushDay) {
		this.pushDay = pushDay;
	}
    
}
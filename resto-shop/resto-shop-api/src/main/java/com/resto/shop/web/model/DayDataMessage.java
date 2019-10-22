package com.resto.shop.web.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DayDataMessage implements Serializable{
    private String id;

    private String shopId;

    private Integer type;

    private String shopName;//店铺名称

    private Date dateTime;

    private Integer weekDay;

    private Date date;

    private Byte state;

    private Integer times;

    private String wether;

    private Integer temperature;

    private Integer orderNumber;

    private BigDecimal orderSum;

    private Integer customerOrderNumber;

    private BigDecimal customerOrderSum;

    private String customerOrderRatio;//用户消费比率

    private String backCustomerOrderRatio;//回头消费比率

    private String newCustomerOrderRatio;//新增用户比率

    private Integer newCuostomerOrderNum;

    private BigDecimal newCustomerOrderSum;

    private String newCustomerOrder;//新用户消费

    private Integer newNormalCustomerOrderNum;

    private BigDecimal newNormalCustomerOrderSum;

    private  String newNormalCustomerOrder;//自然用户消费

    private Integer newShareCustomerOrderNum;

    private BigDecimal newShareCustomerOrderSum;

    private String newShareCutomerOrder;//分享用户消费

    private Integer backCustomerOrderNum;

    private BigDecimal backCustomerOrderSum;

    private  String backCustomerOrder;//回头用户消费

    private Integer backTwoCustomerOrderNum;

    private BigDecimal backTwoCustomerOrderSum;

    private  String backTwoCustomerOrder;//二次回头用户消费

    private String backTwoMoreCustomerOrder;//多次回头用户消费

    private Integer backTwoMoreCustomerOrderNum;

    private BigDecimal backTwoMoreCustomerOrderSum;

    private BigDecimal discountTotal;

    private BigDecimal redPack;

    private BigDecimal coupon;

    private BigDecimal chargeReward;

    private String discountRatio;

    private BigDecimal takeawayTotal;

    private BigDecimal bussinessTotal;

    private BigDecimal monthTotal;


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getWether() {
        return wether;
    }

    public void setWether(String wether) {
        this.wether = wether == null ? null : wether.trim();
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(BigDecimal orderSum) {
        this.orderSum = orderSum;
    }

    public Integer getCustomerOrderNumber() {
        return customerOrderNumber;
    }

    public void setCustomerOrderNumber(Integer customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
    }

    public BigDecimal getCustomerOrderSum() {
        return customerOrderSum;
    }

    public void setCustomerOrderSum(BigDecimal customerOrderSum) {
        this.customerOrderSum = customerOrderSum;
    }

    public String getCustomerOrderRatio() {
        return customerOrderRatio;
    }

    public void setCustomerOrderRatio(String customerOrderRatio) {
        this.customerOrderRatio = customerOrderRatio == null ? null : customerOrderRatio.trim();
    }

    public String getBackCustomerOrderRatio() {
        return backCustomerOrderRatio;
    }

    public void setBackCustomerOrderRatio(String backCustomerOrderRatio) {
        this.backCustomerOrderRatio = backCustomerOrderRatio == null ? null : backCustomerOrderRatio.trim();
    }

    public String getNewCustomerOrderRatio() {
        return newCustomerOrderRatio;
    }

    public void setNewCustomerOrderRatio(String newCustomerOrderRatio) {
        this.newCustomerOrderRatio = newCustomerOrderRatio == null ? null : newCustomerOrderRatio.trim();
    }

    public Integer getNewCuostomerOrderNum() {
        return newCuostomerOrderNum;
    }

    public void setNewCuostomerOrderNum(Integer newCuostomerOrderNum) {
        this.newCuostomerOrderNum = newCuostomerOrderNum;
    }

    public BigDecimal getNewCustomerOrderSum() {
        return newCustomerOrderSum;
    }

    public void setNewCustomerOrderSum(BigDecimal newCustomerOrderSum) {
        this.newCustomerOrderSum = newCustomerOrderSum;
    }

    public Integer getNewNormalCustomerOrderNum() {
        return newNormalCustomerOrderNum;
    }

    public void setNewNormalCustomerOrderNum(Integer newNormalCustomerOrderNum) {
        this.newNormalCustomerOrderNum = newNormalCustomerOrderNum;
    }

    public BigDecimal getNewNormalCustomerOrderSum() {
        return newNormalCustomerOrderSum;
    }

    public void setNewNormalCustomerOrderSum(BigDecimal newNormalCustomerOrderSum) {
        this.newNormalCustomerOrderSum = newNormalCustomerOrderSum;
    }

    public Integer getNewShareCustomerOrderNum() {
        return newShareCustomerOrderNum;
    }

    public void setNewShareCustomerOrderNum(Integer newShareCustomerOrderNum) {
        this.newShareCustomerOrderNum = newShareCustomerOrderNum;
    }

    public BigDecimal getNewShareCustomerOrderSum() {
        return newShareCustomerOrderSum;
    }

    public void setNewShareCustomerOrderSum(BigDecimal newShareCustomerOrderSum) {
        this.newShareCustomerOrderSum = newShareCustomerOrderSum;
    }

    public Integer getBackCustomerOrderNum() {
        return backCustomerOrderNum;
    }

    public void setBackCustomerOrderNum(Integer backCustomerOrderNum) {
        this.backCustomerOrderNum = backCustomerOrderNum;
    }

    public BigDecimal getBackCustomerOrderSum() {
        return backCustomerOrderSum;
    }

    public void setBackCustomerOrderSum(BigDecimal backCustomerOrderSum) {
        this.backCustomerOrderSum = backCustomerOrderSum;
    }

    public Integer getBackTwoCustomerOrderNum() {
        return backTwoCustomerOrderNum;
    }

    public void setBackTwoCustomerOrderNum(Integer backTwoCustomerOrderNum) {
        this.backTwoCustomerOrderNum = backTwoCustomerOrderNum;
    }

    public BigDecimal getBackTwoCustomerOrderSum() {
        return backTwoCustomerOrderSum;
    }

    public void setBackTwoCustomerOrderSum(BigDecimal backTwoCustomerOrderSum) {
        this.backTwoCustomerOrderSum = backTwoCustomerOrderSum;
    }

    public Integer getBackTwoMoreCustomerOrderNum() {
        return backTwoMoreCustomerOrderNum;
    }

    public void setBackTwoMoreCustomerOrderNum(Integer backTwoMoreCustomerOrderNum) {
        this.backTwoMoreCustomerOrderNum = backTwoMoreCustomerOrderNum;
    }

    public BigDecimal getBackTwoMoreCustomerOrderSum() {
        return backTwoMoreCustomerOrderSum;
    }

    public void setBackTwoMoreCustomerOrderSum(BigDecimal backTwoMoreCustomerOrderSum) {
        this.backTwoMoreCustomerOrderSum = backTwoMoreCustomerOrderSum;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getRedPack() {
        return redPack;
    }

    public void setRedPack(BigDecimal redPack) {
        this.redPack = redPack;
    }

    public BigDecimal getCoupon() {
        return coupon;
    }

    public void setCoupon(BigDecimal coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getChargeReward() {
        return chargeReward;
    }

    public void setChargeReward(BigDecimal chargeReward) {
        this.chargeReward = chargeReward;
    }

    public String getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(String discountRatio) {
        this.discountRatio = discountRatio == null ? null : discountRatio.trim();
    }

    public BigDecimal getTakeawayTotal() {
        return takeawayTotal;
    }

    public void setTakeawayTotal(BigDecimal takeawayTotal) {
        this.takeawayTotal = takeawayTotal;
    }

    public BigDecimal getBussinessTotal() {
        return bussinessTotal;
    }

    public void setBussinessTotal(BigDecimal bussinessTotal) {
        this.bussinessTotal = bussinessTotal;
    }

    public BigDecimal getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(BigDecimal monthTotal) {
        this.monthTotal = monthTotal;
    }

    public DayDataMessage() {
    }

    public DayDataMessage(String id, Integer type, String shopName, Date dateTime, Integer weekDay, Date date, Byte state, Integer times, String wether, Integer temperature, Integer orderNumber, BigDecimal orderSum, Integer customerOrderNumber, BigDecimal customerOrderSum, String customerOrderRatio, String backCustomerOrderRatio, String newCustomerOrderRatio, Integer newCuostomerOrderNum, BigDecimal newCustomerOrderSum, Integer newNormalCustomerOrderNum, BigDecimal newNormalCustomerOrderSum, Integer newShareCustomerOrderNum, BigDecimal newShareCustomerOrderSum, Integer backCustomerOrderNum, BigDecimal backCustomerOrderSum, Integer backTwoCustomerOrderNum, BigDecimal backTwoCustomerOrderSum, Integer backTwoMoreCustomerOrderNum, BigDecimal backTwoMoreCustomerOrderSum, BigDecimal discountTotal, BigDecimal redPack, BigDecimal coupon, BigDecimal chargeReward, String discountRatio, BigDecimal takeawayTotal, BigDecimal bussinessTotal, BigDecimal monthTotal) {
        this.id = id;
        this.type = type;
        this.shopName = shopName;
        this.dateTime = dateTime;
        this.weekDay = weekDay;
        this.date = date;
        this.state = state;
        this.times = times;
        this.wether = wether;
        this.temperature = temperature;
        this.orderNumber = orderNumber;
        this.orderSum = orderSum;
        this.customerOrderNumber = customerOrderNumber;
        this.customerOrderSum = customerOrderSum;
        this.customerOrderRatio = customerOrderRatio;
        this.backCustomerOrderRatio = backCustomerOrderRatio;
        this.newCustomerOrderRatio = newCustomerOrderRatio;
        this.newCuostomerOrderNum = newCuostomerOrderNum;
        this.newCustomerOrderSum = newCustomerOrderSum;
        this.newNormalCustomerOrderNum = newNormalCustomerOrderNum;
        this.newNormalCustomerOrderSum = newNormalCustomerOrderSum;
        this.newShareCustomerOrderNum = newShareCustomerOrderNum;
        this.newShareCustomerOrderSum = newShareCustomerOrderSum;
        this.backCustomerOrderNum = backCustomerOrderNum;
        this.backCustomerOrderSum = backCustomerOrderSum;
        this.backTwoCustomerOrderNum = backTwoCustomerOrderNum;
        this.backTwoCustomerOrderSum = backTwoCustomerOrderSum;
        this.backTwoMoreCustomerOrderNum = backTwoMoreCustomerOrderNum;
        this.backTwoMoreCustomerOrderSum = backTwoMoreCustomerOrderSum;
        this.discountTotal = discountTotal;
        this.redPack = redPack;
        this.coupon = coupon;
        this.chargeReward = chargeReward;
        this.discountRatio = discountRatio;
        this.takeawayTotal = takeawayTotal;
        this.bussinessTotal = bussinessTotal;
        this.monthTotal = monthTotal;
    }

    public String getNewCustomerOrder() {
        return newCustomerOrder;
    }

    public void setNewCustomerOrder(String newCustomerOrder) {
        this.newCustomerOrder = newCustomerOrder;
    }

    public String getNewNormalCustomerOrder() {
        return newNormalCustomerOrder;
    }

    public void setNewNormalCustomerOrder(String newNormalCustomerOrder) {
        this.newNormalCustomerOrder = newNormalCustomerOrder;
    }

    public String getNewShareCutomerOrder() {
        return newShareCutomerOrder;
    }

    public void setNewShareCutomerOrder(String newShareCutomerOrder) {
        this.newShareCutomerOrder = newShareCutomerOrder;
    }

    public String getBackCustomerOrder() {
        return backCustomerOrder;
    }

    public void setBackCustomerOrder(String backCustomerOrder) {
        this.backCustomerOrder = backCustomerOrder;
    }

    public String getBackTwoCustomerOrder() {
        return backTwoCustomerOrder;
    }

    public void setBackTwoCustomerOrder(String backTwoCustomerOrder) {
        this.backTwoCustomerOrder = backTwoCustomerOrder;
    }

    public String getBackTwoMoreCustomerOrder() {
        return backTwoMoreCustomerOrder;
    }

    public void setBackTwoMoreCustomerOrder(String backTwoMoreCustomerOrder) {
        this.backTwoMoreCustomerOrder = backTwoMoreCustomerOrder;
    }
}
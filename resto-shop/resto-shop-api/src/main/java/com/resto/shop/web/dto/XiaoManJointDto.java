package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/16/0016 14:52
 * @Description:
 */
public class XiaoManJointDto implements Serializable {

    //商铺 ID
    private String shopId;
    //POS 机号
    private String posId;
    //流水号
    private String serialNumber;
    //商品编码/条码
    private String productCode;
    //商品名称
    private String productName;
    //商品数量
    private Integer productCount;
    //整单应收总金额
    private BigDecimal totalMoney;
    //整单优惠金额
    private BigDecimal totalDiscountsMoney;
    //付款金额（现金）
    private BigDecimal cashMoney;
    //付款金额（银行 卡
    private BigDecimal bankCardMoney;
    //付款金额（外挂 POS）
    private BigDecimal posMoney;
    //付款金额（储值 卡
    private BigDecimal petCardMoney;
    //付款金额（支票
    private BigDecimal chequeMoney;
    //付款方式（礼券
    private BigDecimal CouponMoney;
    //销售时间
    private Date marketTime;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getTotalDiscountsMoney() {
        return totalDiscountsMoney;
    }

    public void setTotalDiscountsMoney(BigDecimal totalDiscountsMoney) {
        this.totalDiscountsMoney = totalDiscountsMoney;
    }

    public BigDecimal getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(BigDecimal cashMoney) {
        this.cashMoney = cashMoney;
    }

    public BigDecimal getBankCardMoney() {
        return bankCardMoney;
    }

    public void setBankCardMoney(BigDecimal bankCardMoney) {
        this.bankCardMoney = bankCardMoney;
    }

    public BigDecimal getPosMoney() {
        return posMoney;
    }

    public void setPosMoney(BigDecimal posMoney) {
        this.posMoney = posMoney;
    }

    public BigDecimal getPetCardMoney() {
        return petCardMoney;
    }

    public void setPetCardMoney(BigDecimal petCardMoney) {
        this.petCardMoney = petCardMoney;
    }

    public BigDecimal getChequeMoney() {
        return chequeMoney;
    }

    public void setChequeMoney(BigDecimal chequeMoney) {
        this.chequeMoney = chequeMoney;
    }

    public BigDecimal getCouponMoney() {
        return CouponMoney;
    }

    public void setCouponMoney(BigDecimal couponMoney) {
        CouponMoney = couponMoney;
    }

    public Date getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
    }
}

package com.resto.brand.web.dto;

import com.resto.brand.core.util.ExcelAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2017/8/11.
 * 订单列表统计品牌数据
 */
public class BrandOrderReportDto implements Serializable {

    @ExcelAnnotation(exportName = "日期")
    private String brandName;//品牌名称

    @ExcelAnnotation(exportName = "订单总数")
    private Integer orderCount;//订单总数

    @ExcelAnnotation(exportName = "微信端订单总数")
    private Integer wechatOrderCount;//微信端点单数

    @ExcelAnnotation(exportName = "pos端订单总数")
    private Integer posOrderCount;//pos点单数

    @ExcelAnnotation(exportName = "订单总额")
    private BigDecimal orderPrice;//订单总额

    @ExcelAnnotation(exportName = "就餐人数")
    private Integer peopleCount;//就餐人数

    @ExcelAnnotation(exportName = "堂吃订单数")
    private Integer tangshiCount;//堂食订单总数

    @ExcelAnnotation(exportName = "堂吃订单额")
    private BigDecimal tangshiPrice;//堂食订单总额

    @ExcelAnnotation(exportName = "外带订单数")
    private Integer waidaiCount;//外带订单总数

    @ExcelAnnotation(exportName = "外带订单额")
    private BigDecimal waidaiPrice;//外带订单总额

    @ExcelAnnotation(exportName = "R+外卖订单数")
    private Integer waimaiCount;//外卖订单总数

    @ExcelAnnotation(exportName = "R+外卖订单额")
    private BigDecimal waimaiPrice;//外卖订单总额

    @ExcelAnnotation(exportName = "单均")
    private BigDecimal singlePrice;//单均

    @ExcelAnnotation(exportName = "人均")
    private BigDecimal perPersonPrice;//人均

    @Setter
    @Getter
    private BigDecimal orderPosDiscountMoney;

    @Setter
    @Getter
    private BigDecimal memberDiscountMoney;

    @Setter
    @Getter
    private BigDecimal realEraseMoney;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getWechatOrderCount() {
        return wechatOrderCount;
    }

    public void setWechatOrderCount(Integer wechatOrderCount) {
        this.wechatOrderCount = wechatOrderCount;
    }

    public Integer getPosOrderCount() {
        return posOrderCount;
    }

    public void setPosOrderCount(Integer posOrderCount) {
        this.posOrderCount = posOrderCount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getTangshiCount() {
        return tangshiCount;
    }

    public void setTangshiCount(Integer tangshiCount) {
        this.tangshiCount = tangshiCount;
    }

    public BigDecimal getTangshiPrice() {
        return tangshiPrice;
    }

    public void setTangshiPrice(BigDecimal tangshiPrice) {
        this.tangshiPrice = tangshiPrice;
    }

    public Integer getWaidaiCount() {
        return waidaiCount;
    }

    public void setWaidaiCount(Integer waidaiCount) {
        this.waidaiCount = waidaiCount;
    }

    public BigDecimal getWaidaiPrice() {
        return waidaiPrice;
    }

    public void setWaidaiPrice(BigDecimal waidaiPrice) {
        this.waidaiPrice = waidaiPrice;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getWaimaiCount() {
        return waimaiCount;
    }

    public void setWaimaiCount(Integer waimaiCount) {
        this.waimaiCount = waimaiCount;
    }

    public BigDecimal getWaimaiPrice() {
        return waimaiPrice;
    }

    public void setWaimaiPrice(BigDecimal waimaiPrice) {
        this.waimaiPrice = waimaiPrice;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
    }

    public BigDecimal getPerPersonPrice() {
        return perPersonPrice;
    }

    public void setPerPersonPrice(BigDecimal perPersonPrice) {
        this.perPersonPrice = perPersonPrice;
    }

    public BrandOrderReportDto(String brandName, Integer orderCount, BigDecimal orderPrice, Integer peopleCount, Integer tangshiCount, BigDecimal tangshiPrice, Integer waidaiCount, BigDecimal waidaiPrice, Integer waimaiCount, BigDecimal waimaiPrice, BigDecimal singlePrice, BigDecimal perPersonPrice) {
        this.brandName = brandName;
        this.orderCount = orderCount;
        this.orderPrice = orderPrice;
        this.peopleCount = peopleCount;
        this.tangshiCount = tangshiCount;
        this.tangshiPrice = tangshiPrice;
        this.waidaiCount = waidaiCount;
        this.waidaiPrice = waidaiPrice;
        this.waimaiCount = waimaiCount;
        this.waimaiPrice = waimaiPrice;
        this.singlePrice = singlePrice;
        this.perPersonPrice = perPersonPrice;
    }

    public BrandOrderReportDto(Integer orderCount, BigDecimal orderPrice, Integer peopleCount, Integer tangshiCount, BigDecimal tangshiPrice, Integer waidaiCount, BigDecimal waidaiPrice, Integer waimaiCount, BigDecimal waimaiPrice, BigDecimal singlePrice, BigDecimal perPersonPrice) {
        this.orderCount = orderCount;
        this.orderPrice = orderPrice;
        this.peopleCount = peopleCount;
        this.tangshiCount = tangshiCount;
        this.tangshiPrice = tangshiPrice;
        this.waidaiCount = waidaiCount;
        this.waidaiPrice = waidaiPrice;
        this.waimaiCount = waimaiCount;
        this.waimaiPrice = waimaiPrice;
        this.singlePrice = singlePrice;
        this.perPersonPrice = perPersonPrice;
    }

    public BrandOrderReportDto() {
        this.orderCount = 0;
        this.wechatOrderCount = 0;
        this.posOrderCount = 0;
        this.orderPrice = BigDecimal.ZERO;
        this.peopleCount = 0;
        this.tangshiCount = 0;
        this.tangshiPrice = BigDecimal.ZERO;
        this.waidaiCount = 0;
        this.waidaiPrice = BigDecimal.ZERO;
        this.waimaiCount = 0;
        this.waimaiPrice = BigDecimal.ZERO;
        this.singlePrice = BigDecimal.ZERO;
        this.perPersonPrice = BigDecimal.ZERO;
    }

    public BrandOrderReportDto(String brandName, Integer orderCount, Integer wechatOrderCount, Integer posOrderCount, BigDecimal orderPrice, Integer peopleCount, Integer tangshiCount, BigDecimal tangshiPrice, Integer waidaiCount, BigDecimal waidaiPrice, Integer waimaiCount, BigDecimal waimaiPrice, BigDecimal singlePrice, BigDecimal perPersonPrice) {
        this.brandName = brandName;
        this.orderCount = orderCount;
        this.wechatOrderCount = wechatOrderCount;
        this.posOrderCount = posOrderCount;
        this.orderPrice = orderPrice;
        this.peopleCount = peopleCount;
        this.tangshiCount = tangshiCount;
        this.tangshiPrice = tangshiPrice;
        this.waidaiCount = waidaiCount;
        this.waidaiPrice = waidaiPrice;
        this.waimaiCount = waimaiCount;
        this.waimaiPrice = waimaiPrice;
        this.singlePrice = singlePrice;
        this.perPersonPrice = perPersonPrice;
    }
}

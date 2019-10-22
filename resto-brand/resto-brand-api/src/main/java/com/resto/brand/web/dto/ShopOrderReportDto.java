package com.resto.brand.web.dto;

import com.resto.brand.core.util.ExcelAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xielc on 2017/8/11.
 * 订单列表统计店铺数据
 */
public class ShopOrderReportDto implements Serializable {


    private String shopDetailId;//店铺id
    @ExcelAnnotation(exportName = "品牌/店铺")
    private String shopName;//品牌名称

    @ExcelAnnotation(exportName = "订单总数")
    private Integer shop_orderCount;//订单总数

    @ExcelAnnotation(exportName = "微信端订单总数")
    private Integer shop_wechatOrderCount;//微信端订单总数

    @ExcelAnnotation(exportName = "pos端订单总数")
    private Integer shop_posOrderCount;//pos端订单总数

    @ExcelAnnotation(exportName = "订单总额")
    private BigDecimal shop_orderPrice;//订单总额
    @ExcelAnnotation(exportName = "就餐人数")
    private Integer shop_peopleCount;//就餐人数

    @ExcelAnnotation(exportName = "堂吃订单数")
    private Integer shop_tangshiCount;//堂食订单总数

    @ExcelAnnotation(exportName = "堂吃订单额")
    private BigDecimal shop_tangshiPrice;//堂食订单总额

    @ExcelAnnotation(exportName = "外带订单数")
    private Integer shop_waidaiCount;//外带订单总数

    @ExcelAnnotation(exportName = "外带订单额")
    private BigDecimal shop_waidaiPrice;//外带订单总额

    @ExcelAnnotation(exportName = "R+外卖订单数")
    private Integer shop_waimaiCount;//外卖订单总数

    @ExcelAnnotation(exportName = "R+外卖订单额")
    private BigDecimal shop_waimaiPrice;//外卖订单总额

    @ExcelAnnotation(exportName = "单均")
    private BigDecimal shop_singlePrice;//单均
    @ExcelAnnotation(exportName = "人均")
    private BigDecimal shop_perPersonPrice;//人均

    private Map<String, Object> brandOrderDto;

    private List<Map<String, Object>> shopOrderDtos;

    @Setter
    @Getter
    @ExcelAnnotation(exportName = "POS折扣")
    private BigDecimal orderPosDiscountMoney;

    @Setter
    @Getter
    @ExcelAnnotation(exportName = "会员折扣")
    private BigDecimal memberDiscountMoney;

    @Setter
    @Getter
    @ExcelAnnotation(exportName = "抹零金额")
    private BigDecimal realEraseMoney;

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShop_orderCount() {
        return shop_orderCount;
    }

    public void setShop_orderCount(Integer shop_orderCount) {
        this.shop_orderCount = shop_orderCount;
    }

    public Integer getShop_peopleCount() {
        return shop_peopleCount;
    }

    public void setShop_peopleCount(Integer shop_peopleCount) {
        this.shop_peopleCount = shop_peopleCount;
    }

    public BigDecimal getShop_orderPrice() {
        return shop_orderPrice;
    }

    public void setShop_orderPrice(BigDecimal shop_orderPrice) {
        this.shop_orderPrice = shop_orderPrice;
    }

    public Integer getShop_tangshiCount() {
        return shop_tangshiCount;
    }

    public void setShop_tangshiCount(Integer shop_tangshiCount) {
        this.shop_tangshiCount = shop_tangshiCount;
    }

    public BigDecimal getShop_tangshiPrice() {
        return shop_tangshiPrice;
    }

    public void setShop_tangshiPrice(BigDecimal shop_tangshiPrice) {
        this.shop_tangshiPrice = shop_tangshiPrice;
    }

    public Integer getShop_waidaiCount() {
        return shop_waidaiCount;
    }

    public void setShop_waidaiCount(Integer shop_waidaiCount) {
        this.shop_waidaiCount = shop_waidaiCount;
    }

    public BigDecimal getShop_waidaiPrice() {
        return shop_waidaiPrice;
    }

    public void setShop_waidaiPrice(BigDecimal shop_waidaiPrice) {
        this.shop_waidaiPrice = shop_waidaiPrice;
    }

    public Integer getShop_waimaiCount() {
        return shop_waimaiCount;
    }

    public void setShop_waimaiCount(Integer shop_waimaiCount) {
        this.shop_waimaiCount = shop_waimaiCount;
    }

    public BigDecimal getShop_waimaiPrice() {
        return shop_waimaiPrice;
    }

    public void setShop_waimaiPrice(BigDecimal shop_waimaiPrice) {
        this.shop_waimaiPrice = shop_waimaiPrice;
    }

    public BigDecimal getShop_singlePrice() {
        return shop_singlePrice;
    }

    public void setShop_singlePrice(BigDecimal shop_singlePrice) {
        this.shop_singlePrice = shop_singlePrice;
    }

    public BigDecimal getShop_perPersonPrice() {
        return shop_perPersonPrice;
    }

    public void setShop_perPersonPrice(BigDecimal shop_perPersonPrice) {
        this.shop_perPersonPrice = shop_perPersonPrice;
    }

    public Map<String, Object> getBrandOrderDto() {
        return brandOrderDto;
    }

    public void setBrandOrderDto(Map<String, Object> brandOrderDto) {
        this.brandOrderDto = brandOrderDto;
    }

    public List<Map<String, Object>> getShopOrderDtos() {
        return shopOrderDtos;
    }

    public void setShopOrderDtos(List<Map<String, Object>> shopOrderDtos) {
        this.shopOrderDtos = shopOrderDtos;
    }

    public Integer getShop_wechatOrderCount() {
        return shop_wechatOrderCount;
    }

    public void setShop_wechatOrderCount(Integer shop_wechatOrderCount) {
        this.shop_wechatOrderCount = shop_wechatOrderCount;
    }

    public Integer getShop_posOrderCount() {
        return shop_posOrderCount;
    }

    public void setShop_posOrderCount(Integer shop_posOrderCount) {
        this.shop_posOrderCount = shop_posOrderCount;
    }
}

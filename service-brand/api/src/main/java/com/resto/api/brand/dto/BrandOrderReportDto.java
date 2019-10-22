package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2017/8/11.
 * 订单列表统计品牌数据
 */
@Data
public class BrandOrderReportDto implements Serializable {

    private static final long serialVersionUID = 4516505541496596293L;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "订单总数")
    private Integer orderCount;

    @ApiModelProperty(value = "订单总额")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "就餐人数")
    private Integer peopleCount;

    @ApiModelProperty(value = "堂食订单总数")
    private Integer tangshiCount;

    @ApiModelProperty(value = "堂食订单总额")
    private BigDecimal tangshiPrice;

    @ApiModelProperty(value = "外带订单总数")
    private Integer waidaiCount;

    @ApiModelProperty(value = "外带订单总额")
    private BigDecimal waidaiPrice;

    @ApiModelProperty(value = "外卖订单总数")
    private Integer waimaiCount;

    @ApiModelProperty(value = "外卖订单总额")
    private BigDecimal waimaiPrice;

    @ApiModelProperty(value = "单均")
    private BigDecimal singlePrice;

    @ApiModelProperty(value = "人均")
    private BigDecimal perPersonPrice;

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
}

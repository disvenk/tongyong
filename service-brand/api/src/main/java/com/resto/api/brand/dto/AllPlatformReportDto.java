package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2017/7/11.
 * 通过品牌每个店铺数据累加的数据
 */
@Data
public class AllPlatformReportDto implements Serializable {

    private static final long serialVersionUID = 6674123782656671059L;

    @ApiModelProperty(value = "品牌名称")
    private String  brandName;
    @ApiModelProperty(value = "品牌外卖订单数")
    private Integer count;
    @ApiModelProperty(value = "品牌外卖订单额")
    private BigDecimal price;
    @ApiModelProperty(value = "品牌饿了吗外卖订单数")
    private Integer allElmCount;
    @ApiModelProperty(value = "品牌饿了吗外卖订单额")
    private BigDecimal allElmPrice;
    @ApiModelProperty(value = "品牌美团外卖订单数")
    private Integer allMtCount;
    @ApiModelProperty(value = "品牌美团外卖订单额")
    private BigDecimal allMtPrice;
    @ApiModelProperty(value = "品牌百度外卖订单数")
    private Integer allBdCount;
    @ApiModelProperty(value = "品牌百度外卖订单额")
    private BigDecimal allBdPrice;

}

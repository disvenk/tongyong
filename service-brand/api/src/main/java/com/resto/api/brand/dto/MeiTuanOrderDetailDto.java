package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  美团订单的菜品详情
 * Created by Lmx on 2017/3/23.
 */
@Data
public class MeiTuanOrderDetailDto implements Serializable {

    private static final long serialVersionUID = 1855579812629127777L;

    @ApiModelProperty(value = "菜品名")
    private String food_name;

    @ApiModelProperty(value = "菜品份数")
    private int quantity;

    @ApiModelProperty(value = "菜品原价")
    private double price;

    @ApiModelProperty(value = "菜品规格")
    private String spec;

    @ApiModelProperty(value = "餐盒费")
    private double  box_price;

    @ApiModelProperty(value = "餐盒数量")
    private Integer box_num;

}

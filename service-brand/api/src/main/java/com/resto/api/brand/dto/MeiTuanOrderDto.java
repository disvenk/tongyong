package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  美团外卖订单 dto
 * Created by Lmx on 2017/3/22.
 */
@Data
public class MeiTuanOrderDto implements Serializable {

    private static final long serialVersionUID = 341961176132209300L;

    @ApiModelProperty(value = "美团订单ID")
    private String orderId;

    @ApiModelProperty(value = "Resto+ 店铺ID")
    private String ePoiId;

    @ApiModelProperty(value = "订单原价")
    private Double originalPrice;

    @ApiModelProperty(value = "订单总价 （用户实际支付金额）")
    private Double total;

    @ApiModelProperty(value = "收货人地址")
    private String recipientAddress;

    @ApiModelProperty(value = "收货人姓名")
    private String recipientName;

    @ApiModelProperty(value = "收货人电话")
    private String recipientPhone;

    @ApiModelProperty(value = "订单备注")
    private String caution;

    @ApiModelProperty(value = "美团订单创建时间")
    private long ctime;

    @ApiModelProperty(value = "订单配送费")
    private Double shippingFee;

    @ApiModelProperty(value = "订单菜品详情")
    private String detail;

    @ApiModelProperty(value = "订单额外费用")
    private String extras;

    @ApiModelProperty(value = "订单支付方式")
    private Integer payType;

    @ApiModelProperty(value = "美团推送的订单全部JSON数据")
    private String sourceText;

}

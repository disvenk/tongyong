package com.resto.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShareMoneyDto implements Serializable{

    private static final long serialVersionUID = -5580123352536043238L;

    @ApiModelProperty(value = "red_packet表的主键")
    private String id;

    @ApiModelProperty(value = "收到返利的人")
    private String cid;

    @ApiModelProperty(value = "消费的订单Id")
    private String orderId;

    @ApiModelProperty(value = "获得的返利金额")
    private BigDecimal value;

    @ApiModelProperty(value = "返利时间")
    private Date createTime;

    @ApiModelProperty(value = "消费人")
    private String customerId;

    @ApiModelProperty(value = "消费人的昵称")
    private String nickName;

    @ApiModelProperty(value = "消费人的头像")
    private String headPhoto;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

}

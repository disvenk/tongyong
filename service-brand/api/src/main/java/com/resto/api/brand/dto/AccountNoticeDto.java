package com.resto.api.brand.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 品牌账户余额不足或者欠费通知
 */
@Data
public class AccountNoticeDto implements Serializable {

    private static final long serialVersionUID = -5396906669893511219L;

    private Integer id;

    @ApiModelProperty(value = "品牌账户id")
    private Integer accountId;

    @ApiModelProperty(value = "通知时的价格(低于该价格则发送通知)")
    private BigDecimal noticePrice;

    @ApiModelProperty(value = "0发送余额不足通知 1发送账户欠费通知")
    private Integer type;

}
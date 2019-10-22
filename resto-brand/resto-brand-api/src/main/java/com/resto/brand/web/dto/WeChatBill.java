package com.resto.brand.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信对账单DTO
 */
@Data
public class WeChatBill implements Serializable{

    private String weChatPayId;

    private String orderId;

    private String serialNumber;

    private BigDecimal payValue;

    private Date createTime;

    private Date payTime;

    private String refundWeChatId;

    private String refundOrderId;

    private BigDecimal refundValue;

    private Date refundTime;

    private String refundInfo;

    private String paymentMethod;
}

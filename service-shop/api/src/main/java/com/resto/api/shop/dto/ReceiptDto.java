package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReceiptDto implements Serializable{

    private static final long serialVersionUID = 1403515496742813282L;

    private Long id;

    private String orderNumber;

    private Date payTime;

    private BigDecimal orderMoney;

    private BigDecimal receiptMoney;

    private Long receiptTitleId;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private String shopId;//店铺id

}
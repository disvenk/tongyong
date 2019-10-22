package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReceiptOrderDto implements Serializable{

    private static final long serialVersionUID = -8757361785290841741L;

    private String orderNumber;

    private Date payTime;

    private BigDecimal orderMoney;

    private BigDecimal receiptMoney;

    private Integer state;

}

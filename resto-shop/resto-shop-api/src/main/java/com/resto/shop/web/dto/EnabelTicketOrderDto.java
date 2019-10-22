package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by disvenk.dai on 2018-06-04 20:46
 */

@Data
public class EnabelTicketOrderDto implements Serializable {
    private static final long serialVersionUID = -6531376408061747610L;

    private String id;

    private String serialNumber;

    private BigDecimal payValue;

    private BigDecimal orderMoney;

    private String createTime;
}

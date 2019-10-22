package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class BrandOperationDto implements Serializable {

    private static final long serialVersionUID = 3596176544916678063L;

    private String brandName;

    private String shopName;

    private Integer orderCount;

    private BigDecimal orderMoney;

    private BigDecimal weChatPay;

    private BigDecimal aliPay;

    private Integer customerCount;

    private Integer newCustomerCount;

    private Integer newRegiestCustomerCount;

    private Integer chargeCount;

    private BigDecimal chargeMoney;

    private Map<String, Object> brandOperationCount;

    private List<Map<String, Object>> brandOperationDtos;

    private List<Map<String, Object>> shopOperationDtos;

}

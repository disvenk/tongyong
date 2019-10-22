package com.resto.api.brand.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by KONATA on 2017/2/8.
 */
@Data
public class LRFExcelDto {

    private String posdate;

    private String addTime;

    private String addName;

    private String posId;

    private String tableNo;

    private String pfName;

    private String department;

    private String deputy;

    private String menuType;

    private String menuCode;

    private String menuName;

    private Integer quantity;

    private BigDecimal amount_1;

    private BigDecimal amount_2;

    private String accountName;

    private String pay_method;

    private String remark;
}

package com.resto.shop.web.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AdditionalData implements Serializable {

    private String additional_code;

    private String additional_name;

    private BigDecimal additional_unitprice;

    private BigDecimal additional_num;

    private BigDecimal additional_totalmoney;

}

package com.resto.shop.web.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DepositData implements Serializable {

    private String deposit_name;

    private BigDecimal deposit_money;
}

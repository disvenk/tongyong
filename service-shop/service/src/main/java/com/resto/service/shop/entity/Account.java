package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Account implements Serializable {

    private static final long serialVersionUID = -8220463511815263645L;

    private String id;

    private BigDecimal remain;

    private Byte status;

    private BigDecimal frozenRemain;

    /**
     * 用于保存 用户的交易明细
     */
    private List<AccountLog> accountLogs;

}
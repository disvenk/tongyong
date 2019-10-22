package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 品牌账户余额不足或者欠费通知
 */
@Data
public class AccountNotice implements Serializable {

    private static final long serialVersionUID = -834428918304167110L;

    private Integer id;

    private Integer accountId;//品牌账户id

    private BigDecimal noticePrice;//通知时的价格(低于该价格则发送通知)

    private Integer type;//0发送余额不足通知 1发送账户欠费通知

}
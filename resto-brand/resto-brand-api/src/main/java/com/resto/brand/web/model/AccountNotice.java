package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 品牌账户余额不足或者欠费通知
 */
public class AccountNotice implements Serializable {
    private Integer id;

    private Integer accountId;//品牌账户id

    private BigDecimal noticePrice;//通知时的价格(低于该价格则发送通知)

    private Integer type;//0发送余额不足通知 1发送账户欠费通知

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getNoticePrice() {
        return noticePrice;
    }

    public void setNoticePrice(BigDecimal noticePrice) {
        this.noticePrice = noticePrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
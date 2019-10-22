package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Account implements Serializable {
    private String id;

    private BigDecimal remain;

    private Byte status;

    private BigDecimal frozenRemain;

    /**
     * 用于保存 用户的交易明细
     */
    List<AccountLog> accountLogs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getRemain() {
        return remain;
    }

    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

	public List<AccountLog> getAccountLogs() {
		return accountLogs;
	}

	public void setAccountLogs(List<AccountLog> accountLogs) {
		this.accountLogs = accountLogs == null ? null : accountLogs;
	}

    public BigDecimal getFrozenRemain() {
        return frozenRemain;
    }

    public void setFrozenRemain(BigDecimal frozenRemain) {
        this.frozenRemain = frozenRemain;
    }
}
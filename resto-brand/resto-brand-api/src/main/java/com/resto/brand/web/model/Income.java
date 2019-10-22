package com.resto.brand.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Income implements Serializable{
    private Long id;

    private Long contractId;

    private Byte payType;

    private String payAccount;

    private String paySerialnumber;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private BigDecimal payMoney;

    //一个进款对应一个合同
	private Contract contract;

	//进款对应的合同编号 用来接收页面传的值
    private String constractNum;

    public String getConstractNum() {
        return constractNum;
    }

    public void setConstractNum(String constractNum) {
        this.constractNum = constractNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount == null ? null : payAccount.trim();
    }

    public String getPaySerialnumber() {
        return paySerialnumber;
    }

    public void setPaySerialnumber(String paySerialnumber) {
        this.paySerialnumber = paySerialnumber == null ? null : paySerialnumber.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
}
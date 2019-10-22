package com.resto.service.brand.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Income implements Serializable{

    private static final long serialVersionUID = 1674750413592986139L;

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

}
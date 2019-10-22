package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountTicket implements Serializable {

    private static final long serialVersionUID = 7663972216759681410L;

    private Long id;

    private String header;

    private String content;

    private Date createTime;

    private Date pushTime;

    private Byte ticketStatus;

    private BigDecimal money;

    private String proposer;

    private Byte type;

    private String taxpayerCode;

    private String registeredAddress;

    private String registeredPhone;

    private String bankName;

    private String bankAccount;

    private String consigneceId;

    private String name;

    private String phone;

    private String address;

    private String expersage;

    private String remark;

    private String brandId;

    private Byte status;

    private Integer accountId;

}
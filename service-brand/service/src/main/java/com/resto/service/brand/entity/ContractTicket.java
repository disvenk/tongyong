package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ContractTicket implements Serializable{

    private static final long serialVersionUID = -5956794389344416124L;

    private Long id;

    private Long contractId;

    private Byte type;

    private String header;

    private String content;

    private Date createTime;

    private Date pushTime;

    //物流状态
    private Byte ticketStatus; //0 1.运输中 2.已送达

    private BigDecimal money;

    private String proposer;

    private String taxpayerCode;

    private String registeredAddress;

    private String registeredPhone;

    private String name;

    private String phone;

    private String address;

    private String expersage;

    private String remark;

    private Byte status; //数据状态 1.正常 0删除

    private String bankName;

    private String bankAccount;

    private Contract contract;//一个发票 对应一个合同

}
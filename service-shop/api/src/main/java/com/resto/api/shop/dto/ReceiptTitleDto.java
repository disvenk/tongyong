package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReceiptTitleDto implements Serializable{

    private static final long serialVersionUID = -7720547139070323991L;

    private Long id;

    private Integer type;

    private String name;

    private String dutyParagraph;

    private String companyAddress;

    private String mobileNo;

    private String bankOfDeposit;

    private String bankNumber;

    private String customerId;

    private Integer state;

    private Date createTime;

    private Date updateTime;

}
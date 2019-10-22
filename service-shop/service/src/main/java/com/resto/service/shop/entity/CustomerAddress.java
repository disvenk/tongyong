package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerAddress implements Serializable {

    private static final long serialVersionUID = -3854966633867634877L;

    private String id;

    private String name;

    private Integer sex;

    private String mobileNo;

    private String address;

    private String addressReality;

    private String label;

    private Integer state;

    private String customerId;

    private Double longitude;

    private Double latitude;

    private Date createTime;

    private Date updateTime;

}
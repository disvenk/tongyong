package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerDetail implements Serializable {

    private static final long serialVersionUID = -9215748088074659514L;

    private String id;

    private Date birthDate;

    private Integer age;

    private String constellation;   //星座

    private String vocation;     //职业

    private String company;     //公司

    private String school;      //学校

    private String personalNote;      //个人说明

    private String shopDetailId;    //录入生日信息时所在店铺

}

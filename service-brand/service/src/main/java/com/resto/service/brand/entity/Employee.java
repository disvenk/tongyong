package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/9/5.
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 3483982880056061088L;

    //主键
    private String id;
    //手机号
    private String telephone;
    //品牌id
    private String brandId;

    private  Integer state; //状态

}

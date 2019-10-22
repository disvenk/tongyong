package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class City implements Serializable {

    private static final long serialVersionUID = 7495162836171499662L;
    
    private Integer id;

    private String cityName;

    private String zipCode;

    private Integer provinceId;
}
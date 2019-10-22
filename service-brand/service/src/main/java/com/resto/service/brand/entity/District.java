package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class District implements Serializable {
    
    private static final long serialVersionUID = -5826096408021904333L;
    private Integer id;
    private String districtName;
    private Integer cityId;
    private String zipCode;

}
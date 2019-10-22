package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Province implements Serializable {
    private static final long serialVersionUID = -7728139586112221465L;
    private Integer id;
    private String provinceName;
    private String zipCode;

}
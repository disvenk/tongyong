package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModuleList implements Serializable {

    private static final long serialVersionUID = 5842819099548646469L;

    private Integer id;

    private String name;

    private String sign;
    
    private Boolean isOpen;

}
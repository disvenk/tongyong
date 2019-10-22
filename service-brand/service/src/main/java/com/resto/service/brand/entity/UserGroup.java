package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 5972178081459483632L;
    private Long id;
    private String name;

}
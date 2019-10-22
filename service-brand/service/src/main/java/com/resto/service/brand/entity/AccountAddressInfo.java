package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountAddressInfo implements Serializable {

    private static final long serialVersionUID = 1488435642837627561L;

    private String id;

    private String brandId;

    private String name;

    private String phone;

    private String address;

    private Byte state;

}
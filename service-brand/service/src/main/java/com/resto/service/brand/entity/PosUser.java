package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2017/6/20.
 */
@Data
public class PosUser implements Serializable {

    private static final long serialVersionUID = 130906667004623971L;

    private Long id;

    private String ip;

    private String userName;

    private String passWord;

}

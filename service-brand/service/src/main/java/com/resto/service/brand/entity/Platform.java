package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/11/1.
 * 第三方平台
 */
@Data
public class Platform implements Serializable {

    private static final long serialVersionUID = 7296062867684147758L;

    private Long id;

    private String name;
    
    private String platformId;

}

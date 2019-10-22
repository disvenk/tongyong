package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by user on 2016/3/17.
 */
@Data
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 6864034413324212971L;
    private String token;

    private String sign;

    private String userId;

}

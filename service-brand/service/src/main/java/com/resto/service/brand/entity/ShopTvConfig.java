package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by carl on 2017/7/14.
 */
@Data
public class ShopTvConfig implements Serializable {

    private static final long serialVersionUID = 6780807214273389017L;

    private Long id;

    private String shopDetailId;

    private String brandId;

    private String readyBackColor;

    private String takeMealBackColor;

    private String callBackColor;

    private String tvBackground;

    private String numberColor;

    private String callNumberColor;

    private String text;

    private String labelColor;

}

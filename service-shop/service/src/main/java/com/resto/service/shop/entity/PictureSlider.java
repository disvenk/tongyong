package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureSlider implements Serializable {

    private static final long serialVersionUID = -3560529760639580433L;

    private Integer id;

    private String title;

    private String pictureUrl;

    private String pictureLink;

    private Byte sort;

    private Byte state;

    private String shopDetailId;

}
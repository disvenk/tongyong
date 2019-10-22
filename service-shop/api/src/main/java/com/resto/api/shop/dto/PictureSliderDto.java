package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureSliderDto implements Serializable {

    private static final long serialVersionUID = -3560529760639580433L;

    private Integer id;

    private String title;

    private String pictureUrl;

    private String pictureLink;

    private Byte sort;

    private Byte state;

    private String shopDetailId;

}
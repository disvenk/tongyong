package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Experience implements Serializable {

    private static final long serialVersionUID = -3118990511090484705L;

    private Integer id;

    private Integer showType;

    private String title;

    private String picUrl;

    private String shopDetailId;

    private String photoSquare;

    private Integer showSort;

}
package com.resto.service.brand.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class ShowPhoto implements Serializable {

    private static final long serialVersionUID = -982454067308811439L;

    private Integer id;

    private Integer showType;

    private String title;

    private String picUrl;

    private String shopDetailId;

    private String photoSquare;

    private Integer showSort;

    private Boolean choiceIt;

}
package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShowPhotoDto implements Serializable {

    private static final long serialVersionUID = 6275712298556114472L;

    private Integer id;

    private Integer showType;

    private String title;

    private String picUrl;

    private String shopDetailId;

    private String photoSquare;
    
    private Integer showSort;

}
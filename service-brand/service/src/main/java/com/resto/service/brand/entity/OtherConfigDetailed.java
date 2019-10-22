package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OtherConfigDetailed implements Serializable {

    private static final long serialVersionUID = 8376739802226330797L;

    private Long id;

    private String data;

    private Date updateTime;

    private String otherConfigSign;

    private String shopDetailId;

}
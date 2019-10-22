package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WeBrandScore implements Serializable {

    private static final long serialVersionUID = -3269339236918264575L;

    private Integer id;

    private String brandId;

    private String brandScore;

    private Date createTime;

    private  Boolean flag;

}
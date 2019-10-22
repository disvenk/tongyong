package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleTop implements Serializable {

    private static final long serialVersionUID = 4519248197108699529L;

    private Long id;

    private Date createTime;

    private String name;

    private Integer type;

    private String shopDetailId;

}
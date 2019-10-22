package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RecommendCategory implements Serializable {

    private static final long serialVersionUID = -3687364004793088952L;

    private String id;

    private Integer type;

    private String name;

    private Integer sort;

    private Integer state;

    private String shopDetailId;

    private Date createTime;

    private Date updateTime;

    private List<RecommendCategoryArticle> articles;

}
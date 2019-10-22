package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RecommendCategoryArticle implements Serializable {

    private static final long serialVersionUID = 6970009257365002231L;

    private String id;

    private String articleName;

    private Integer recommendSort;

    private String recommendCategoryId;

    private String articleId;

    private Date createTime;

    private Date updateTime;
}
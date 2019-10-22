package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopCart implements Serializable {

    private static final long serialVersionUID = 4039632860998800248L;

    private Integer id;

    private Integer number;

    private String customerId;

    private String articleId;

    private String shopDetailId;

    private Integer distributionModeId;

    private String userId;

    private String shopType;

    private Integer pid;

    private List<ShopCart> currentItem;

    private Integer attrId;

    private String recommendId;

    private String recommendArticleId;

    private String unitNewId;

    private List<ShopCart> unitList;

    private String uuid;

}
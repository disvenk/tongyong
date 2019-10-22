package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KONATA on 2016/9/11.
 */
public class Unit implements Serializable {

    private String id;

    private String name;

    private Integer sort;

    private String shopId;

    // 上面那个 shopId 是个假的，但是不敢删
    private String shopDetailId;

    private Integer choiceType;

    private Integer isUsed;

    private List<UnitDetail> details;

    private Boolean openArticleLibrary;

    final public Integer getIsUsed() {
        return isUsed;
    }

    final public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    final public Integer getChoiceType() {
        return choiceType;
    }

    final public void setChoiceType(Integer choiceType) {
        this.choiceType = choiceType;
    }

    final public Integer getSort() {
        return sort;
    }

    final public void setSort(Integer sort) {
        this.sort = sort;
    }

    final public String getShopId() {
        return shopId;
    }

    final public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    final public String getId() {
        return id;
    }

    final public void setId(String id) {
        this.id = id;
    }

    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
    }

    final public List<UnitDetail> getDetails() {
        return details;
    }

    final public void setDetails(List<UnitDetail> details) {
        this.details = details;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public Boolean getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Boolean openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }
}

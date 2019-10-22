package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WeightPackage implements Serializable {

    private Long id;

    private String name;

    private Date createTime;

    private String shopId;

    private List<WeightPackageDetail> details;

    private Boolean openArticleLibrary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<WeightPackageDetail> getDetails() {
        return details;
    }

    public void setDetails(List<WeightPackageDetail> details) {
        this.details = details;
    }

    public Boolean getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Boolean openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }
}

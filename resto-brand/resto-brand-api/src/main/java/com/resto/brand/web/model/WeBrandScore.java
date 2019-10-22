package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

public class WeBrandScore implements Serializable {
    private Integer id;

    private String brandId;

    private String brandScore;

    private Date createTime;

    private  Boolean flag;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getBrandScore() {
        return brandScore;
    }

    public void setBrandScore(String brandScore) {
        this.brandScore = brandScore == null ? null : brandScore.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
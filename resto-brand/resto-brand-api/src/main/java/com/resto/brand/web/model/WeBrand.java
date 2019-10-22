package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.List;

public class WeBrand implements Serializable {
    private Long id;

    private String brandId;

    private String brandName;

    List<WeBrandScore> weBrandScores;

    public List<WeBrandScore> getWeBrandScores() {
        return weBrandScores;
    }

    public void setWeBrandScores(List<WeBrandScore> weBrandScores) {
        this.weBrandScores = weBrandScores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }
}
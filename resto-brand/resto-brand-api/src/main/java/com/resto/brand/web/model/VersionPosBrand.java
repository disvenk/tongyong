package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.List;

public class VersionPosBrand implements Serializable {
    private Integer id;

    private String brandId;

    private String versionNo;

    private String downloadAddress;

    private Integer type;

    private String brandName;

    private Integer versionId;

    private Integer voluntarily;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getVoluntarily() {
        return voluntarily;
    }

    public void setVoluntarily(Integer voluntarily) {
        this.voluntarily = voluntarily;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    private List<VersionPosShopDetail> versionPosShopDetails;

    public List<VersionPosShopDetail> getVersionPosShopDetails() {
        return versionPosShopDetails;
    }

    public void setVersionPosShopDetails(List<VersionPosShopDetail> versionPosShopDetails) {
        this.versionPosShopDetails = versionPosShopDetails;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo == null ? null : versionNo.trim();
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress == null ? null : downloadAddress.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
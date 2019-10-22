package com.resto.brand.web.model;

import java.io.Serializable;

public class VersionPosShopDetail implements Serializable {
    private Integer id;

    private String brandId;

    private String shopdetailId;

    private String versionNo;

    private String downloadAddress;

    private Integer type;

    private String shopName;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getShopdetailId() {
        return shopdetailId;
    }

    public void setShopdetailId(String shopdetailId) {
        this.shopdetailId = shopdetailId == null ? null : shopdetailId.trim();
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
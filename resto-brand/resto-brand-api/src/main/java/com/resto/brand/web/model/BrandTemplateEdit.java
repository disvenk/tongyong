package com.resto.brand.web.model;

import java.io.Serializable;

public class BrandTemplateEdit implements Serializable {
    private Integer id;

    private String templateNumber;

    private String appid;

    private String brandId;

    private String shopDetailId;

    private Integer templateSign;

    private Integer index;

    private String pattern;

    private String pushType;

    private String pushTitle;

    private String content;

    private String startSign;

    private String endSign;

    private String oldStartSign;

    private String oldEndSign;

    private Boolean bigOpen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateNumber() {
        return templateNumber;
    }

    public void setTemplateNumber(String templateNumber) {
        this.templateNumber = templateNumber == null ? null : templateNumber.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public Integer getTemplateSign() {
        return templateSign;
    }

    public void setTemplateSign(Integer templateSign) {
        this.templateSign = templateSign;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern == null ? null : pattern.trim();
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType == null ? null : pushType.trim();
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle == null ? null : pushTitle.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getStartSign() {
        return startSign;
    }

    public void setStartSign(String startSign) {
        this.startSign = startSign == null ? null : startSign.trim();
    }

    public String getEndSign() {
        return endSign;
    }

    public void setEndSign(String endSign) {
        this.endSign = endSign == null ? null : endSign.trim();
    }

    public String getOldStartSign() {
        return oldStartSign;
    }

    public void setOldStartSign(String oldStartSign) {
        this.oldStartSign = oldStartSign == null ? null : oldStartSign.trim();
    }

    public String getOldEndSign() {
        return oldEndSign;
    }

    public void setOldEndSign(String oldEndSign) {
        this.oldEndSign = oldEndSign == null ? null : oldEndSign.trim();
    }

    public Boolean getBigOpen() {
        return bigOpen;
    }

    public void setBigOpen(Boolean bigOpen) {
        this.bigOpen = bigOpen;
    }
}
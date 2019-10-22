package com.resto.brand.web.model;


import java.io.Serializable;

public class BrandTemplateRoot implements Serializable {
    private Integer id;

    private String templateNumber;

    private String pattern;

    private String pushType;

    private String pushTitle;

    private String content;

    private String startSign;

    private String endSign;

    private Integer templateSign;

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

    public Integer getTemplateSign() {
        return templateSign;
    }

    public void setTemplateSign(Integer templateSign) {
        this.templateSign = templateSign;
    }
}
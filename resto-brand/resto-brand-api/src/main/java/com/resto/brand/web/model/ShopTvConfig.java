package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by carl on 2017/7/14.
 */
public class ShopTvConfig implements Serializable {

    private static final long serialVersionUID = 6780807214273389017L;

    private Long id;

    private String shopDetailId;

    private String brandId;

    private String readyBackColor;

    private String takeMealBackColor;

    private String callBackColor;

    private String tvBackground;

    private String numberColor;

    private String callNumberColor;

    private String text;

    private String labelColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getReadyBackColor() {
        return readyBackColor;
    }

    public void setReadyBackColor(String readyBackColor) {
        this.readyBackColor = readyBackColor;
    }

    public String getTakeMealBackColor() {
        return takeMealBackColor;
    }

    public void setTakeMealBackColor(String takeMealBackColor) {
        this.takeMealBackColor = takeMealBackColor;
    }

    public String getCallBackColor() {
        return callBackColor;
    }

    public void setCallBackColor(String callBackColor) {
        this.callBackColor = callBackColor;
    }

    public String getTvBackground() {
        return tvBackground;
    }

    public void setTvBackground(String tvBackground) {
        this.tvBackground = tvBackground;
    }

    public String getNumberColor() {
        return numberColor;
    }

    public void setNumberColor(String numberColor) {
        this.numberColor = numberColor;
    }

    public String getCallNumberColor() {
        return callNumberColor;
    }

    public void setCallNumberColor(String callNumberColor) {
        this.callNumberColor = callNumberColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }
}

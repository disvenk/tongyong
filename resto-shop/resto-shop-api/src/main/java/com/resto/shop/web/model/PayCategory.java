package com.resto.shop.web.model;

import java.io.Serializable;

public class PayCategory implements Serializable {
    private Integer restoPayMode;

    private String weqianPatyMode;

    public Integer getRestoPayMode() {
        return restoPayMode;
    }

    public void setRestoPayMode(Integer restoPayMode) {
        this.restoPayMode = restoPayMode;
    }

    public String getWeqianPatyMode() {
        return weqianPatyMode;
    }

    public void setWeqianPatyMode(String weqianPatyMode) {
        this.weqianPatyMode = weqianPatyMode == null ? null : weqianPatyMode.trim();
    }
}
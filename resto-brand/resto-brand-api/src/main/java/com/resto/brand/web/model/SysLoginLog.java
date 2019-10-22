package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by user on 2016/3/17.
 */
public class SysLoginLog implements Serializable {
    private String token;

    private String sign;

    private String userId;

    final public String getUserId() {
        return userId;
    }

    final public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

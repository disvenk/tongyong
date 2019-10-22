package com.resto.geekpos.web.model;

import java.io.Serializable;

/**
 * Created by user on 2016/3/11.
 */
public class CheckResult implements Serializable{
    private Boolean success;

    private String token;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}

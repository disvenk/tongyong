package com.resto.brand.web.model;

import java.io.Serializable;

/**
 * Created by KONATA on 2016/9/5.
 */
public class Employee implements Serializable {

    //主键
    private String id;
    //手机号
    private String telephone;
    //品牌id
    private String brandId;

    private  Integer state; //状态

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    final public String getTelephone() {
        return telephone;
    }

    final public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    final public String getBrandId() {
        return brandId;
    }

    final public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}

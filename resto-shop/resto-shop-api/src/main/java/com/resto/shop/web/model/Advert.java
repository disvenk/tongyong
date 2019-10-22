package com.resto.shop.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class Advert implements Serializable {
    private Integer id;
    
    @NotBlank(message="{标题不能为空}")
    private String slogan;

    @NotBlank(message="{描述不能为空}")
    private String description;
    
   
    private Byte state;

    private String shopDetailId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan == null ? null : slogan.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
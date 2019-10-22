package com.resto.shop.web.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2016/10/31.
 * 饿了吗订单额外字段
 */
public class HungerOrderExtra implements Serializable {
    //主键
    private Long id;
    //订单项的名称
    private String name;
    //金额（单位：元），金额分正负，比如优惠为负，配送费为正
    private BigDecimal price;
    //说明
    private String description;
    //id唯一标识这个实体（可能是活动，可能是打包费、配送费等）
    private String extraId;
    //唯一标识该订单项目的ID
    private String categoryId;
    //项目类型，不同category_id下的type会不一样
    private Integer type;
    //数量
    private Integer quantity;
    //订单id
    private String orderId;

    final public Long getId() {
        return id;
    }

    final public void setId(Long id) {
        this.id = id;
    }

    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
    }

    final public BigDecimal getPrice() {
        return price;
    }

    final public void setPrice(BigDecimal price) {
        this.price = price;
    }

    final public String getDescription() {
        return description;
    }

    final public void setDescription(String description) {
        this.description = description;
    }

    final public String getExtraId() {
        return extraId;
    }

    final public void setExtraId(String extraId) {
        this.extraId = extraId;
    }

    final public String getCategoryId() {
        return categoryId;
    }

    final public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    final public Integer getType() {
        return type;
    }

    final public void setType(Integer type) {
        this.type = type;
    }

    final public Integer getQuantity() {
        return quantity;
    }

    final public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    final public String getOrderId() {
        return orderId;
    }

    final public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public HungerOrderExtra() {}

    public HungerOrderExtra(JSONObject object,String orderId){
        this.orderId = orderId;
        name = object.optString("name");
        price = object.optBigDecimal("price",new BigDecimal(0));
        description = object.optString("description");
        extraId = object.optString("id");
        categoryId = object.optString("category_id");
        type = object.optInt("type");
        quantity = object.optInt("quantity");
    }
}

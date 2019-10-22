package com.resto.shop.web.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2016/10/31.
 * 饿了吗订单明细
 */
public class HungerOrderDetail implements Serializable {
    //主键
    private Long id;
    //饿了吗订单id
    private String orderId;
    //分组id
    private Long groupId;
    //分类ID
    private String categoryId;
    //名称
    private String name;
    //价格
    private BigDecimal price;
    //饿了吗菜品id
    private String articleId;
    //数量
    private Integer quantity;
    //第三方id
    private String tpFoodId;
    //规格名称 JsonArray
    private String specs;

    final public Long getId() {
        return id;
    }

    final public void setId(Long id) {
        this.id = id;
    }

    final public String getOrderId() {
        return orderId;
    }

    final public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    final public Long getGroupId() {
        return groupId;
    }

    final public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    final public String getCategoryId() {
        return categoryId;
    }

    final public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    final public String getArticleId() {
        return articleId;
    }

    final public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    final public Integer getQuantity() {
        return quantity;
    }

    final public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    final public String getTpFoodId() {
        return tpFoodId;
    }

    final public void setTpFoodId(String tpFoodId) {
        this.tpFoodId = tpFoodId;
    }

    final public String getSpecs() {
        return specs;
    }

    final public void setSpecs(String specs) {
        this.specs = specs;
    }

    public HungerOrderDetail() {}

    public HungerOrderDetail(JSONObject object,Long groupId,String orderId) {
        this.groupId = groupId;
        this.orderId = orderId;
        categoryId = object.optString("category_id");
        name =  object.optString("name");
        price = object.optBigDecimal("price",new BigDecimal(0));
        articleId = object.optString("id");
        quantity = object.optInt("quantity");
        tpFoodId = object.optString("tp_food_id");
        specs = object.optString("specs");
    }
}

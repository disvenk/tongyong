package com.resto.shop.web.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2016/10/31.
 * 饿了吗订单明细 の浇头
 */
public class HungerOrderGarnish implements Serializable {
    //主键
    private Long id;
    //父类id
    private String parentId;
    //分组id
    private Long groupId;
    //饿了吗订单id
    private String orderId;
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

    final public Long getGroupId() {
        return groupId;
    }

    final public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    final public Long getId() {
        return id;
    }

    final public void setId(Long id) {
        this.id = id;
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

    final public String getOrderId() {
        return orderId;
    }

    final public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    final public String getParentId() {
        return parentId;
    }

    final public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public HungerOrderGarnish() {}

    public HungerOrderGarnish(JSONObject object,String parentId,String orderId,Long groupId) {
        this.groupId = groupId;
        this.orderId = orderId;
        this.parentId = parentId;
        categoryId = object.optString("category_id");
        name =  object.optString("name");
        price = object.optBigDecimal("price",new BigDecimal(0));
        articleId = object.optString("id");
        quantity = object.optInt("quantity");
        tpFoodId = object.optString("tp_food_id");
    }
}

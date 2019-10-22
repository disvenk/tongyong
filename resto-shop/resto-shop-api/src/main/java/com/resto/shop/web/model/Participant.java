package com.resto.shop.web.model;

import java.io.Serializable;

/**
 * Created by carl on 2017/9/25.
 */
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String groupId;

    private String customerId;

    private String orderId;

    private String shopDetailId;

    private String brandId;

    private Integer isPay;     // 0 非他买单仅加菜  1 买单并且未加过菜    2 买单且加过菜

    private Integer appraise;  //  0 未领取红包   1 领取过红包

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public Integer getAppraise() {
        return appraise;
    }

    public void setAppraise(Integer appraise) {
        this.appraise = appraise;
    }
}

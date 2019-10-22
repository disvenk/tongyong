package com.resto.shop.web.posDto;

import com.resto.shop.web.model.PlatformOrder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2017/8/17.
 */
public class PlatformOrderDto implements Serializable {
    private static final long serialVersionUID = 3601116055898435297L;

    public PlatformOrderDto() {
    }

    public PlatformOrderDto(PlatformOrder platformOrder) {
        this.id = platformOrder.getId() == null ? "" : platformOrder.getId();
        this.type = platformOrder.getType() == null ? 0 : platformOrder.getType();
        this.platformOrderId = platformOrder.getPlatformOrderId() == null ? "" : platformOrder.getPlatformOrderId();
        this.shopDetailId = platformOrder.getShopDetailId() == null ? "" : platformOrder.getShopDetailId();
        this.originalPrice = platformOrder.getOriginalPrice() == null ? BigDecimal.valueOf(0) : platformOrder.getOriginalPrice();
        this.totalPrice = platformOrder.getTotalPrice() == null ? BigDecimal.valueOf(0) : platformOrder.getTotalPrice();
        this.address = platformOrder.getAddress() == null ? "" : platformOrder.getAddress();
        this.phone = platformOrder.getPhone() == null ? "" : platformOrder.getPhone();
        this.name = platformOrder.getName() == null ? "" : platformOrder.getName();
        this.orderCreateTime = platformOrder.getOrderCreateTime().getTime();
        this.payType = platformOrder.getPayType() == null ? "" : platformOrder.getPayType();
        this.remark = platformOrder.getRemark() == null ? "" : platformOrder.getRemark();
    }

    private String id;

    private Integer type;

    private String platformOrderId;

    private String shopDetailId;

    private BigDecimal originalPrice;

    private BigDecimal totalPrice;

    private String address;

    private String phone;

    private String name;

    private Long orderCreateTime;

    private String payType;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Long orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

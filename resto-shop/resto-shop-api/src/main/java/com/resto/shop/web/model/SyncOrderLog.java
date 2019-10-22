package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class SyncOrderLog implements Serializable{

    private static final long serialVersionUID = 3706649123357721052L;

    private Long id;//newpos订单记录日志id

    private String orderId;//订单id

    private String shopId;//店铺id

    private String brandId;//品牌id

    private Date createTime;//创建时间

    private String contentOrder;//订单内容json

    private String contentOrderItem;//订单菜品项json

    private String contentOrderPaymentItem;//订单支付项json

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContentOrder() {
        return contentOrder;
    }

    public void setContentOrder(String contentOrder) {
        this.contentOrder = contentOrder == null ? null : contentOrder.trim();
    }

    public String getContentOrderItem() {
        return contentOrderItem;
    }

    public void setContentOrderItem(String contentOrderItem) {
        this.contentOrderItem = contentOrderItem == null ? null : contentOrderItem.trim();
    }

    public String getContentOrderPaymentItem() {
        return contentOrderPaymentItem;
    }

    public void setContentOrderPaymentItem(String contentOrderPaymentItem) {
        this.contentOrderPaymentItem = contentOrderPaymentItem == null ? null : contentOrderPaymentItem.trim();
    }
}
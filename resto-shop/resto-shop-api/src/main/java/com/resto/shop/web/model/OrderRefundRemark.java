package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class OrderRefundRemark implements Serializable {
    private Long id;

    private String articleId;

    private String orderId;

    private Integer refundRemarkId;

    private String refundRemark;

    private String remarkSupply;

    private Integer refundCount;

    private Date createTime;

    private String shopId;

    private String brandId;

    private String dataSyncId;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getRefundRemarkId() {
        return refundRemarkId;
    }

    public void setRefundRemarkId(Integer refundRemarkId) {
        this.refundRemarkId = refundRemarkId;
    }

    public String getRefundRemark() {
        return refundRemark;
    }

    public void setRefundRemark(String refundRemark) {
        this.refundRemark = refundRemark == null ? null : refundRemark.trim();
    }

    public String getRemarkSupply() {
        return remarkSupply;
    }

    public void setRemarkSupply(String remarkSupply) {
        this.remarkSupply = remarkSupply == null ? null : remarkSupply.trim();
    }

    public Integer getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getDataSyncId() {
        return dataSyncId;
    }

    public void setDataSyncId(String dataSyncId) {
        this.dataSyncId = dataSyncId;
    }
}
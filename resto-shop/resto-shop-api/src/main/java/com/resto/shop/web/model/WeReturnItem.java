package com.resto.shop.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WeReturnItem implements Serializable {
    private Long id;

    private String returnItemName;

    private String shopId;

    private Integer returnItemCount;

    private BigDecimal returnItemTotal;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReturnItemName() {
        return returnItemName;
    }

    public void setReturnItemName(String returnItemName) {
        this.returnItemName = returnItemName == null ? null : returnItemName.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public Integer getReturnItemCount() {
        return returnItemCount;
    }

    public void setReturnItemCount(Integer returnItemCount) {
        this.returnItemCount = returnItemCount;
    }

    public BigDecimal getReturnItemTotal() {
        return returnItemTotal;
    }

    public void setReturnItemTotal(BigDecimal returnItemTotal) {
        this.returnItemTotal = returnItemTotal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
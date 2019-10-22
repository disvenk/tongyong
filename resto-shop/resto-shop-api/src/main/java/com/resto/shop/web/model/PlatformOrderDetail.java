package com.resto.shop.web.model;

import com.resto.brand.core.util.ApplicationUtils;
import eleme.openapi.sdk.api.entity.order.OGoodsItem;
import eleme.openapi.sdk.api.entity.order.OGroupItemAttribute;
import eleme.openapi.sdk.api.entity.order.OGroupItemSpec;

import java.io.Serializable;
import java.math.BigDecimal;

public class PlatformOrderDetail implements Serializable {
    private String id;

    private String platformOrderId;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private String showName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId == null ? null : platformOrderId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName == null ? null : showName.trim();
    }

    public PlatformOrderDetail() {
    }

    public PlatformOrderDetail(HungerOrderDetail detail) {
        id = ApplicationUtils.randomUUID();
        platformOrderId = detail.getOrderId();
        name = detail.getName();
        price = detail.getPrice();
        quantity = detail.getQuantity();
        if(detail.getSpecs().contains(",")){
            showName = detail.getName() + detail.getSpecs();
        }else{
            showName = detail.getName();
        }

    }

    public PlatformOrderDetail(OGoodsItem detail, String orderId) {
        id = ApplicationUtils.randomUUID();
        platformOrderId = orderId;
        name = detail.getName();
        price = new BigDecimal(detail.getPrice());
        quantity = detail.getQuantity();
        showName = detail.getName();
        if(detail.getNewSpecs().size() > 0){
            for (int i = 0; i < detail.getNewSpecs().size(); i++) {
                OGroupItemSpec spec = detail.getNewSpecs().get(i);
                showName += spec.getName();
            }
        }
        if(detail.getAttributes().size() > 0){
            for (int j = 0; j < detail.getAttributes().size(); j++) {
                OGroupItemAttribute attribute = detail.getAttributes().get(j);
                showName += detail.getName() + attribute.getName();
            }
        }

    }
}
package com.resto.shop.web.posDto;

import com.resto.shop.web.model.PlatformOrderDetail;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2017/8/17.
 */
public class PlatformOrderDetailDto implements Serializable {
    private static final long serialVersionUID = 7545076503652097302L;

    public PlatformOrderDetailDto(){}

    public PlatformOrderDetailDto(PlatformOrderDetail platformOrderDetail){
        this.id = platformOrderDetail.getId() == null ? "" : platformOrderDetail.getId();
        this.name = platformOrderDetail.getName() == null ? "" : platformOrderDetail.getName();
        this.platformOrderId = platformOrderDetail.getPlatformOrderId() == null ? "" : platformOrderDetail.getPlatformOrderId();
        this.price = platformOrderDetail.getPrice() == null ? BigDecimal.valueOf(0) : platformOrderDetail.getPrice();
        this.quantity = platformOrderDetail.getQuantity() == null ? 0 : platformOrderDetail.getQuantity();
        this.showName = platformOrderDetail.getShowName() == null ? "" : platformOrderDetail.getShowName();
    }


    private String id;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private String showName;

    private String platformOrderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.showName = showName;
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }
}

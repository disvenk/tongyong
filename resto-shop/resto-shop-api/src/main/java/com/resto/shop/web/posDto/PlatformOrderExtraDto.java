package com.resto.shop.web.posDto;

import com.resto.shop.web.model.PlatformOrderExtra;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by KONATA on 2017/8/17.
 */
public class PlatformOrderExtraDto implements Serializable {
    private static final long serialVersionUID = -8874628541223149295L;

    public PlatformOrderExtraDto(){}

    public PlatformOrderExtraDto(PlatformOrderExtra extra){
        this.id = extra.getId() == null ? "" : extra.getId();
        this.platformOrderId = extra.getPlatformOrderId() == null ?  "" : extra.getPlatformOrderId();
        this.name = extra.getName() == null ? "" : extra.getName();
        this.price = extra.getPrice() == null ? BigDecimal.valueOf(0) : extra.getPrice();
        this.quantity = extra.getQuantity() == null ? 0 : extra.getQuantity();
    }

    private String id;

    private String platformOrderId;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
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
}

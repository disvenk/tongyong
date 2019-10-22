package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KONATA on 2016/10/31.
 * 饿了吗订单明细分组
 */
public class HungerOrderGroup implements Serializable {

    private Long id ;

    private String orderId;

    private List<HungerOrderDetail> details;

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

    final public List<HungerOrderDetail> getDetails() {
        return details;
    }

    final public void setDetails(List<HungerOrderDetail> details) {
        this.details = details;
    }
}

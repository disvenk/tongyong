package com.resto.shop.web.dto;

import java.io.Serializable;

/**
 * Created by xielc on 2017/9/6.
 */
public class ReceiptPos implements Serializable {

    private String receiptId;

    private Integer state;

    private String name;

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

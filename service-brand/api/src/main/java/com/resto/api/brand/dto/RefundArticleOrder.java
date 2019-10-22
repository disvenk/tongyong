package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 退菜报表
 */
@Data
public class RefundArticleOrder implements Serializable {

    private String shopId;

    private String shopName;

    private String orderId;

    private String tableNumber;

    private String telephone;

    private String nickName;

    private Integer refundCount;

    private BigDecimal refundMoney;

    private String pushOrderTime;

    private List<Map<String, String>> refundArticleOrderList;

    public RefundArticleOrder(String shopId, String shopName, String orderId, String tableNumber, String telephone, String nickName, Integer refundCount, BigDecimal refundMoney) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.telephone = telephone;
        this.nickName = nickName;
        this.refundCount = refundCount;
        this.refundMoney = refundMoney;
    }
}
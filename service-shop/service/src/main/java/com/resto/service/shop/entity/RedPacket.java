package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RedPacket implements Serializable {

    private static final long serialVersionUID = 4934247686475536884L;

    private String id;
    private BigDecimal redMoney;
    private Date createTime;
    private Date finishTime;
    private String customerId;
    private String brandId;
    private String shopDetailId;
    private BigDecimal redRemainderMoney;
    private Integer redType;
    private String orderId;

}

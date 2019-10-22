package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderRefundRemarkDto implements Serializable{

    private static final long serialVersionUID = 6420386482237210791L;

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

}
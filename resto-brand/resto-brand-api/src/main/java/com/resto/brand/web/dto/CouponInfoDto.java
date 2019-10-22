package com.resto.brand.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CouponInfoDto implements Serializable {

    private String couponId;

    private String couponName;

    private Integer isUsed;

    private String couponState;

    private String telephone;

    private String addTime;

    private String useTime;

    private String articleName;

    private String orderId;

    private String useShopId;

    private String useShopName;

    private String couponSource;

    private BigDecimal couponValue;

    private BigDecimal useCouponValue;

    private List<Map<String, String>> couponInfoList;
}

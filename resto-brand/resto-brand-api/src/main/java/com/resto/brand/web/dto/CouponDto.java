package com.resto.brand.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CouponDto implements Serializable {
    private String newCustomCouponId;
    private String brandName;
    private String couponClassification;
    private String couponType;
    private Integer couponTypeInt;
    private String couponSoure;
    private String couponShopName;
    private String couponName;
    private BigDecimal couponCount;
    private BigDecimal couponMoney;
    private BigDecimal useCouponCount;
    private BigDecimal useCouponMoney;
    private String useCouponCountRatio;
    private BigDecimal useCouponOrderCount;
    private BigDecimal useCouponOrderMoney;
    private BigDecimal customerCount;
    private String shopDetailId;
    private Map<String, Object> brandCouponInfo;
    private List<Map<String, Object>> shopCouponInfoList;
    private Integer couponType1;
    private Integer deductionType;
}

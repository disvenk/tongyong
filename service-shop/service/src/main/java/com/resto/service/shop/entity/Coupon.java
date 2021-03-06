package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Coupon implements Serializable {

    private static final long serialVersionUID = -7532825529754729046L;

    private String id;

    private String name;

    private BigDecimal value;

    private BigDecimal minAmount;

    private Date beginDate;

    private Date endDate;

    private Date beginTime;

    private Date endTime;

    private Boolean isUsed;

    private Date usingTime;

    private String couponSource;

    private Boolean useWithAccount;

    private String remark;

    private Integer distributionModeId;

    private String customerId;
    
    private Integer couponType;//优惠券类型(-1:通用,0:新用户注册,1:邀请注册,2:生日)

    private  String shopDetailId;

    private  String brandId;

    private Integer pushDay;//推送天数(默认3天)

    private Integer recommendDelayTime;

    private Date addTime;

    private Long newCustomCouponId;

}
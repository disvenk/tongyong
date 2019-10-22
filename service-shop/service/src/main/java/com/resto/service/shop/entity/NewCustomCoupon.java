package com.resto.service.shop.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class NewCustomCoupon implements Serializable{

    private static final long serialVersionUID = -590687909574182076L;

    private Long id;

    private String name;

    //@NumberFormat(style=Style.NUMBER,pattern="#.##")
    @NotNull(message="优惠券的价值不能为空")
    @Min(message="优惠券的价值最小值0" ,value=0)
    private BigDecimal couponValue;//优惠券的价值

    private Integer couponValiday;

    @NotNull(message="优惠券的个数不能为空")
    private Integer couponNumber;

    private Date createTime;

    private Boolean useWithAccount;

    private String couponName;

    @NotNull(message="优惠券的最低消费额度不能为空")
    @Min(message="最低消费额度为0",value=0)
    private BigDecimal couponMinMoney;

    @DateTimeFormat(pattern=("HH:mm"))
    private Date beginTime;

    @DateTimeFormat(pattern=("HH:mm"))
    private Date endTime;

    @NotNull(message="是否启用不能为空")
    private Boolean isActivty;

    private String brandId;

    private Integer couponType;//优惠券类型(-1:通用,0:新用户注册,1:邀请注册)

    @NotNull(message="配送模式不能为空")
    private Integer distributionModeId;

    /**
     * 新增字段用来区别优惠券使用时间的类型
     * 1.按天算 2.按范围算
     */
    @NotNull(message="优惠券类型不能为空")
    private Integer timeConsType;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date beginDateTime;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date endDateTime;

    private String shopDetailId;

    private Integer pushDay;//到期推送时间（默认为3天）

    private Integer recommendDelayTime;

    private Integer distanceBirthdayDay;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date realTimeCouponBeginTime;

    @DateTimeFormat(pattern=("yyyy-MM-dd HH:mm:ss"))
    private Date realTimeCouponEndTime;

    private String realTimeCouponBeginTimeString;

    private String realTimeCouponEndTimeString;

    private  Integer isBrand;//是否是品牌优惠券 1，是 0，店铺优惠券

    private String shopName;//如果是店铺优惠券，显示店铺的名字

}
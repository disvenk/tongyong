package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Customer implements Serializable{

    private static final long serialVersionUID = -428250781207055245L;

    private String id;

    private String wechatId;

    private String nickname;

    private String telephone;

    private String headPhoto;
    
    private Date birthday;

    private Integer defaultDeliveryPoint;

    private Boolean isBindPhone;

    private Date createTime;

    private Date regiestTime;

    private Date firstOrderTime;

    private Date lastLoginTime;

    private String accountId;

    private String brandId;

    private Integer sex;

    private String province;

    private String city;

    private String country;
    
    private BigDecimal account;
    
    private String lastOrderShop;

    private String lastTableNumber;

    private Date newNoticeTime;
    
    private String shareCustomer;

    private Integer isNowRegister;
    
    /**
     * 附加属性用来接收账户的余额
     */
    private BigDecimal remain;

    private BigDecimal frozenRemain;

    private BigDecimal sumRemain;

    private Integer isShare;

    private String registerShopId;

    /**
     * 个人信息详细
     */
    private CustomerDetail customerDetail;
    private String customerDetailId;

    private Date bindPhoneTime;

    private String bindPhoneShop;

    private String realTimeCouponIds;

    private String birthdayCouponIds;

    private String shareCouponIds;

    //是否关注
    private Integer subscribe;

    //会员序号
    private Long serialNumber;

    private String shareLink;

    private String orderId;

    private String shopDetailId;

    //表示该用户是否有订单
    private Boolean useOrder;

    private String cardId;

    private String code;

    //表示该用户是否有充值订单
    private Boolean chargeOrder;

}
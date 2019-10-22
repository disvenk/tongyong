package com.resto.shop.web.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Customer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String wechatId;

    private String unionId;

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

    private Boolean isSvip;

    private String svipId;

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

    private Account accountInfo;

    private List<RedPacket> redPacketList;

    private List<ChargeOrder> chargeOrderList;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Customer{");
        sb.append("id='").append(id).append('\'');
        sb.append(", wechatId='").append(wechatId).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append(", headPhoto='").append(headPhoto).append('\'');
        sb.append(", defaultDeliveryPoint=").append(defaultDeliveryPoint);
        sb.append(", isBindPhone=").append(isBindPhone);
        sb.append(", regiestTime=").append(regiestTime);
        sb.append(", firstOrderTime=").append(firstOrderTime);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", accountId='").append(accountId).append('\'');
        sb.append(", brandId='").append(brandId).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", province='").append(province).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", account=").append(account);
        sb.append(", lastOrderShop='").append(lastOrderShop).append('\'');
        sb.append(", newNoticeTime=").append(newNoticeTime);
        sb.append(", shareCustomer='").append(shareCustomer).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
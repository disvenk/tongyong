package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccountLog implements Serializable {
	public static final Integer SOURCE_PAYMENT = -1;  //支付订 单
	public static final Integer APPRAISE_RED_PACKAGE=1; //评论红包奖励余额
	public static final Integer SOURCE_CHARGE=2;	  //充值余额
	public static final Integer SOURCE_CHARGE_REWARD=3; //充值赠送余额
	public static final Integer SOURCE_SHARE_REWARD=4;  //分享返利余额
	public static final Integer SOURCE_CANCEL_ORDER = 5; //取消订单返还余额
	public static final Integer CHARGE_PAY_REFUND = 6;  //取消订单返回充值的金额
	public static final Integer REWARD_PAY_REFUND = 7;  //取消订单返回充值赠送的金额
    public static final Integer REFUND_ARTICLE_RED_PACKAGE = 7;  //退菜红包返回余额
    public static final Integer THIRD_MONEY = 8;  //第三方账户储值余额
    public static final Integer FREEZE_RED_MONEY = 9;  //消费1:1返利 红包余额   有冻结期
    public static final Integer WITHDRAWALS = 9;  //提现
	
    private String id;

    private BigDecimal money;

    private Date createTime;

    private Integer paymentType;

    private BigDecimal remain;

    private String remark;

    private String accountId;
    
    private Integer source;
    
    private String shopDetailId;

    private Date freezeReturnDate;

    private String orderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public BigDecimal getMoney() {    
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getRemain() {
        return remain;
    }

    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getShopDetailId() {
		return shopDetailId;
	}

	public void setShopDetailId(String shopDetailId) {
		this.shopDetailId = shopDetailId;
	}

    public Date getFreezeReturnDate() {
        return freezeReturnDate;
    }

    public void setFreezeReturnDate(Date freezeReturnDate) {
        this.freezeReturnDate = freezeReturnDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
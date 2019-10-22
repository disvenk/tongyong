package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 收入统计  （用于报表）
 * @author lmx
 *
 */
@Data
public class ShopIncomeDto implements Serializable{

    private static final long serialVersionUID = -1079994949730960392L;

    private BigDecimal totalIncome;//营收总额
	private BigDecimal redIncome;//系统账户收入
	private BigDecimal couponIncome;//优惠券支付收入
	private BigDecimal wechatIncome;//微信支付收入
	private BigDecimal chargeAccountIncome;//充值账户支付
	private BigDecimal chargeGifAccountIncome;//充值赠送账户支付
	private BigDecimal waitNumberIncome;//等位红包支付
	private String brandName;//品牌名称
	private String shopName;//店铺名称
	private String shopDetailId;
	private Integer payMentModeId;
	private BigDecimal payValue;
	private Integer shopModel;
	private BigDecimal otherPayment; //其他方式支付
	private BigDecimal aliPayment;
	private BigDecimal articleBackPay;//退款支付
	private BigDecimal  originalAmount;//原价销售总额
	private   BigDecimal moneyPay  ;//现金支付
	private  BigDecimal   backCartPay;//银行卡支付
    private List<Map<String, Object>> brandIncomeDtos;
    private List<Map<String, Object>> shopIncomeDtos;
    private BigDecimal integralPayment;
    private BigDecimal shanhuiPayment;
    private String date;
    private BigDecimal giveChangePayment;
    private Integer orderCount;
    /**
     * 连接率 Resto+交易笔数 / (Resto交易笔数+线下交易笔数)
     */
    private String connctRatio;

    private Integer restoOrderNum;

    private Integer offLineOrderNum;

    private Integer totatlOrderNum;

    public void setTotalIncome(BigDecimal wechatIncome,BigDecimal redIncome,BigDecimal couponIncome,BigDecimal chargeAccountIncome,BigDecimal chargeGifAccountIncome,BigDecimal waitNumberIncome,BigDecimal otherPayment,BigDecimal aliPayment,BigDecimal articleBackPay,BigDecimal moneyPay,BigDecimal backCartPay,
                               BigDecimal shanhuiPayment, BigDecimal integralPayment, BigDecimal giveChangePayment) {
        if(wechatIncome==null){
            wechatIncome = BigDecimal.ZERO;
        }
        if(redIncome==null){
            redIncome = BigDecimal.ZERO;
        }
        if(couponIncome==null){
            couponIncome = BigDecimal.ZERO;
        }
        if(chargeAccountIncome==null){
            chargeAccountIncome = BigDecimal.ZERO;
        }
        if(chargeGifAccountIncome==null){
            chargeGifAccountIncome = BigDecimal.ZERO;
        }
        if(waitNumberIncome==null){
            waitNumberIncome = BigDecimal.ZERO;
        }
        if(otherPayment == null){
            otherPayment = BigDecimal.ZERO;
        }
        if(aliPayment == null){
            aliPayment = BigDecimal.ZERO;
        }
        if(articleBackPay==null){
            articleBackPay =BigDecimal.ZERO;
        }
		if(moneyPay==null){
			moneyPay =BigDecimal.ZERO;
		}

		if(backCartPay==null){
			backCartPay =BigDecimal.ZERO;
		}

		if (shanhuiPayment == null){
		    shanhuiPayment = BigDecimal.ZERO;
        }

        if(integralPayment == null){
            integralPayment = BigDecimal.ZERO;
        }

        if (giveChangePayment == null){
            giveChangePayment = BigDecimal.ZERO;
        }

        this.totalIncome = wechatIncome.add(redIncome).add(couponIncome).add(chargeAccountIncome).add(chargeGifAccountIncome).add(waitNumberIncome).add(otherPayment).add(aliPayment).add(articleBackPay).add(moneyPay).add(backCartPay).add(shanhuiPayment).add(integralPayment).add(giveChangePayment);
    }

    public ShopIncomeDto(BigDecimal originalAmount ,BigDecimal totalIncome, BigDecimal redIncome, BigDecimal couponIncome, BigDecimal wechatIncome,
                         BigDecimal chargeAccountIncome, BigDecimal chargeGifAccountIncome, BigDecimal waitNumberIncome, BigDecimal aliPayment,
                         BigDecimal backCartPay,BigDecimal moneyPay,BigDecimal otherPayment,BigDecimal articleBackPay, String shopName,
                         String shopDetailId,BigDecimal integralPayment, BigDecimal shanhuiPayment, BigDecimal giveChangePayment) {
        super();
        this.originalAmount = originalAmount;
        this.totalIncome = totalIncome;
        this.redIncome = redIncome;
        this.couponIncome = couponIncome;
        this.wechatIncome = wechatIncome;
        this.chargeAccountIncome = chargeAccountIncome;
        this.chargeGifAccountIncome = chargeGifAccountIncome;
        this.waitNumberIncome = waitNumberIncome;
        this.aliPayment=aliPayment;
        this.backCartPay = backCartPay;
        this.moneyPay = moneyPay;
        this.otherPayment=otherPayment;
        this.articleBackPay=articleBackPay;
        this.shopName = shopName;
        this.shopDetailId = shopDetailId;
        this.integralPayment = integralPayment;
        this.shanhuiPayment = shanhuiPayment;
        this.giveChangePayment = giveChangePayment;
    }

    public ShopIncomeDto(String date,BigDecimal originalAmount ,BigDecimal totalIncome, BigDecimal redIncome, BigDecimal couponIncome, BigDecimal wechatIncome,
                         BigDecimal chargeAccountIncome, BigDecimal chargeGifAccountIncome, BigDecimal waitNumberIncome, BigDecimal aliPayment,
                         BigDecimal backCartPay,BigDecimal moneyPay,BigDecimal otherPayment,BigDecimal articleBackPay, String shopName,
                         String shopDetailId,BigDecimal integralPayment, BigDecimal shanhuiPayment, BigDecimal giveChangePayment) {
        super();
        this.date = date;
        this.originalAmount = originalAmount;
        this.totalIncome = totalIncome;
        this.redIncome = redIncome;
        this.couponIncome = couponIncome;
        this.wechatIncome = wechatIncome;
        this.chargeAccountIncome = chargeAccountIncome;
        this.chargeGifAccountIncome = chargeGifAccountIncome;
        this.waitNumberIncome = waitNumberIncome;
        this.aliPayment=aliPayment;
        this.backCartPay = backCartPay;
        this.moneyPay = moneyPay;
        this.otherPayment=otherPayment;
        this.articleBackPay=articleBackPay;
        this.shopName = shopName;
        this.shopDetailId = shopDetailId;
        this.integralPayment = integralPayment;
        this.shanhuiPayment = shanhuiPayment;
        this.giveChangePayment = giveChangePayment;
    }

    private String parentOrderId;

    private String orderId;

    private Date createTime;

    public ShopIncomeDto(Integer orderCount, BigDecimal originalAmount, BigDecimal totalIncome, BigDecimal redIncome, BigDecimal couponIncome, BigDecimal wechatIncome, BigDecimal chargeAccountIncome, BigDecimal chargeGifAccountIncome, BigDecimal waitNumberIncome, BigDecimal aliPayment, BigDecimal backCartPay, BigDecimal moneyPay, BigDecimal otherPayment, BigDecimal articleBackPay, String shopName, String shopDetailId, BigDecimal integralPayment, BigDecimal shanhuiPayment, BigDecimal giveChangePayment) {
        this.orderCount = orderCount;
        this.originalAmount = originalAmount;
        this.totalIncome = totalIncome;
        this.redIncome = redIncome;
        this.couponIncome = couponIncome;
        this.wechatIncome = wechatIncome;
        this.chargeAccountIncome = chargeAccountIncome;
        this.chargeGifAccountIncome = chargeGifAccountIncome;
        this.waitNumberIncome = waitNumberIncome;
        this.aliPayment = aliPayment;
        this.backCartPay = backCartPay;
        this.moneyPay = moneyPay;
        this.otherPayment = otherPayment;
        this.articleBackPay = articleBackPay;
        this.shopName = shopName;
        this.shopDetailId = shopDetailId;
        this.integralPayment = integralPayment;
        this.shanhuiPayment = shanhuiPayment;
        this.giveChangePayment = giveChangePayment;
    }
}

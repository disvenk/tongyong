package com.resto.brand.web.dto;

import com.resto.brand.core.util.ExcelAnnotation;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 收入统计  （用于报表）
 * @author wtl
 *
 */
@Data
public class ShopIncomeDto implements Serializable{
    @ExcelAnnotation(exportName = "日期")
    private String date;

    @ExcelAnnotation(exportName = "品牌/店铺")
    private String shopName;

    @ExcelAnnotation(exportName = "原价销售总额")
    private BigDecimal  originalAmount;

    @ExcelAnnotation(exportName = "订单总额")
    private BigDecimal totalIncome;//营收总额

    @ExcelAnnotation(exportName = "实收金额")
    private BigDecimal amountCollected;

    @ExcelAnnotation(exportName = "折扣金额")
    private BigDecimal discountTotalMoney;

    @ExcelAnnotation(exportName = "退款金额")
    private BigDecimal refundTotalMoney;

    private BigDecimal wechatIncome;

    private BigDecimal aliPayment;

    @ExcelAnnotation(exportName = "线上微信支付")
    private BigDecimal onLineWechatIncome;

    @ExcelAnnotation(exportName = "线上支付宝支付")
    private BigDecimal onLineAliPayment;

    @ExcelAnnotation(exportName = "线下微信支付")
    private BigDecimal offLineWechatIncome;

    @ExcelAnnotation(exportName = "线下支付宝支付")
    private BigDecimal offLineAliPayment;

    @ExcelAnnotation(exportName = "充值账户支付")
    private BigDecimal chargeAccountIncome;

    @ExcelAnnotation(exportName = "刷卡支付")
    private  BigDecimal backCartPay;

    @ExcelAnnotation(exportName = "现金支付")
    private  BigDecimal moneyPay;

    @ExcelAnnotation(exportName = "新美大支付")
    private BigDecimal shanhuiPayment;

    @ExcelAnnotation(exportName = "团购支付")
    private BigDecimal groupPurchasePayment;

    @ExcelAnnotation(exportName = "红包支付")
    private BigDecimal redIncome;//系统账户收入

    @ExcelAnnotation(exportName = "优惠券支付")
    private BigDecimal couponIncome;

    @ExcelAnnotation(exportName = "充值赠送支付")
    private BigDecimal chargeGifAccountIncome;

    @ExcelAnnotation(exportName = "等位红包支付")
    private BigDecimal waitNumberIncome;

    @ExcelAnnotation(exportName = "会员支付")
    private BigDecimal integralPayment;

    @ExcelAnnotation(exportName = "POS端折扣")
    private BigDecimal orderPosDiscountMoney;

    @ExcelAnnotation(exportName = "会员折扣")
    private BigDecimal memberDiscountMoney;

    @ExcelAnnotation(exportName = "抹零")
    private BigDecimal realEraseMoney;

    @ExcelAnnotation(exportName = "免单")
    private BigDecimal exemptionMoney;

    @ExcelAnnotation(exportName = "代金券支付")
    private BigDecimal cashCouponPayment;

    @ExcelAnnotation(exportName = "退菜返还红包")
    private BigDecimal articleBackPay;

    @ExcelAnnotation(exportName = "线下现金退款")
    private BigDecimal refundCrashPayment;

    @ExcelAnnotation(exportName = "实体卡充值本金支付")
    private BigDecimal cardRechargePay;

    @ExcelAnnotation(exportName = "实体卡充值赠送支付")
    private BigDecimal cardRechargeFreePay;

    @ExcelAnnotation(exportName = "实体卡退款金额")
    private BigDecimal refundCardPay;

    @ExcelAnnotation(exportName = "实体卡折扣")
    private BigDecimal cardDiscountPay;

    @ExcelAnnotation(exportName = "连接率")
    private String connctRatio;

    @ExcelAnnotation(exportName = "赠菜数量")
    private Integer grantArtcileCount;       //赠菜数量

    @ExcelAnnotation(exportName = "赠菜金额")
    private BigDecimal grantArticleMoney;    //赠菜金额

    private String brandName;//品牌名称

    private String shopDetailId;

    private Integer payMentModeId;

    private BigDecimal payValue;

    private Integer shopModel;

    private BigDecimal otherPayment;

    private List<Map<String, Object>> brandIncomeDtos;

    private List<Map<String, Object>> shopIncomeDtos;

    private BigDecimal giveChangePayment;

    private Integer orderCount;

    private String parentOrderId;

    private String orderId;

    private Date createTime;

    /**
     * 连接率 Resto+交易笔数 / (Resto交易笔数+线下交易笔数)
     */

    private Integer restoOrderNum;
    private Integer offLineOrderNum;
    private Integer totatlOrderNum;
    public ShopIncomeDto() {
    }

    public ShopIncomeDto(String shopName, String shopDetailId) {
        this.date = "--";
        this.shopName = shopName;
        this.shopDetailId = shopDetailId;
        this.setValue();
    }

    public ShopIncomeDto(String date, String shopName, String shopDetailId) {
        this.date = date;
        this.shopName = shopName;
        this.shopDetailId = shopDetailId;
        this.setValue();
    }

    private void setValue(){
        this.totalIncome = BigDecimal.ZERO;
        this.redIncome = BigDecimal.ZERO;
        this.couponIncome = BigDecimal.ZERO;
        this.onLineAliPayment = BigDecimal.ZERO;
        this.onLineAliPayment = BigDecimal.ZERO;
        this.offLineAliPayment = BigDecimal.ZERO;
        this.offLineAliPayment = BigDecimal.ZERO;
        this.chargeAccountIncome = BigDecimal.ZERO;
        this.chargeGifAccountIncome = BigDecimal.ZERO;
        this.waitNumberIncome = BigDecimal.ZERO;
        this.payValue = BigDecimal.ZERO;
        this.otherPayment = BigDecimal.ZERO;
        this.articleBackPay = BigDecimal.ZERO;
        this.originalAmount = BigDecimal.ZERO;
        this.moneyPay = BigDecimal.ZERO;
        this.backCartPay = BigDecimal.ZERO;
        this.integralPayment = BigDecimal.ZERO;
        this.shanhuiPayment = BigDecimal.ZERO;
        this.giveChangePayment = BigDecimal.ZERO;
        this.refundCrashPayment = BigDecimal.ZERO;
        this.amountCollected = BigDecimal.ZERO;
        this.orderPosDiscountMoney = BigDecimal.ZERO;
        this.memberDiscountMoney = BigDecimal.ZERO;
        this.realEraseMoney = BigDecimal.ZERO;
        this.refundTotalMoney = BigDecimal.ZERO;
        this.discountTotalMoney = BigDecimal.ZERO;
        this.exemptionMoney = BigDecimal.ZERO;
        this.groupPurchasePayment = BigDecimal.ZERO;
        this.cashCouponPayment = BigDecimal.ZERO;
        this.orderCount = 0;
        this.cardDiscountPay = BigDecimal.ZERO;
        this.cardRechargePay = BigDecimal.ZERO;
        this.cardRechargeFreePay = BigDecimal.ZERO;
        this.refundCardPay = BigDecimal.ZERO;
        this.grantArtcileCount = 0;
        this.grantArticleMoney = BigDecimal.ZERO;
    }
}

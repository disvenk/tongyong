package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/27/0027 15:05
 * @Description:
 */
public class SalesTotalLiteV61 implements Serializable {
    //收款员编号
    private String cashier;
    //会员编号
    private String vipCode;
    //净销售数量
    private BigDecimal netQty;
    //净销售金额
    private BigDecimal netAmount;
    //扩展参数
    private String extendParameter;
    //6	calculateVipBonus	String	是	0	1 – Yes, 0 – No        是否按BonusRule计算会员积分。
    private String calculateVipBonus;

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getVipCode() {
        return vipCode;
    }

    public void setVipCode(String vipCode) {
        this.vipCode = vipCode;
    }

    public BigDecimal getNetQty() {
        return netQty;
    }

    public void setNetQty(BigDecimal netQty) {
        this.netQty = netQty;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getExtendParameter() {
        return extendParameter;
    }

    public void setExtendParameter(String extendParameter) {
        this.extendParameter = extendParameter;
    }

    public String getCalculateVipBonus() {
        return calculateVipBonus;
    }

    public void setCalculateVipBonus(String calculateVipBonus) {
        this.calculateVipBonus = calculateVipBonus;
    }
}

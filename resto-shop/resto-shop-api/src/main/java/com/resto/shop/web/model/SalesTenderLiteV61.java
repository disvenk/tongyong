package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/27/0027 15:15
 * @Description:
 */
public class SalesTenderLiteV61 implements Serializable {
    //本地货币编号
    private String baseCurrencyCode;
    //付款方式编号
    private String tenderCode;
    //付款金额
    private BigDecimal payAmount;
    //本位币付款金额
    private BigDecimal baseAmount;
    //多收金额（例如礼券不设找零）
    private BigDecimal excessAmount;
    //扩展参数
    private String extendParameter;

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTenderCode() {
        return tenderCode;
    }

    public void setTenderCode(String tenderCode) {
        this.tenderCode = tenderCode;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(BigDecimal excessAmount) {
        this.excessAmount = excessAmount;
    }

    public String getExtendParameter() {
        return extendParameter;
    }

    public void setExtendParameter(String extendParameter) {
        this.extendParameter = extendParameter;
    }
}

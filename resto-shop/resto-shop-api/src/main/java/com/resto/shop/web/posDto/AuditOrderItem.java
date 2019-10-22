package com.resto.shop.web.posDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AuditOrderItem implements Serializable {
    //主键
    private String id;
    //店铺id
    private String shopId;
    //操作人
    private String operator;
    //支付类型
    private Integer paymentModeId;
    //支付类型名称
    private String payModeName;
    //上报金额
    private BigDecimal reportMoney;
    //复核金额
    private BigDecimal auditMoney;
    //日志表主键
    private String dailyLogId;
    //结店时间
    private Date closeShopTime;

    private String createTime;

    private Integer sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Integer getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Integer paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public BigDecimal getReportMoney() {
        return reportMoney;
    }

    public void setReportMoney(BigDecimal reportMoney) {
        this.reportMoney = reportMoney;
    }

    public BigDecimal getAuditMoney() {
        return auditMoney;
    }

    public void setAuditMoney(BigDecimal auditMoney) {
        this.auditMoney = auditMoney;
    }

    public String getDailyLogId() {
        return dailyLogId;
    }

    public void setDailyLogId(String dailyLogId) {
        this.dailyLogId = dailyLogId == null ? null : dailyLogId.trim();
    }

    public Date getCloseShopTime() {
        return closeShopTime;
    }

    public void setCloseShopTime(Date closeShopTime) {
        this.closeShopTime = closeShopTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
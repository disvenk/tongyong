package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/8/27/0027 15:09
 * @Description:
 */
public class SalesItemLiteV61 implements Serializable {
    //销售行号
    private Long salesLineNumber;
    //销售员
    private String salesman;
    //货号或者条形码
    private String itemCode;
    //货品所属机构的识别码
    private String itemOrgId;
    //货品批号
    private String itemLotNum;
    //预留字段
    private String serialNumber;
    //库存类型 : 0 to 5 0 – 正常类型
    private Integer inventoryType;
    //销售数量
    private BigDecimal qty;
    //货品折扣金额
    private BigDecimal itemDiscountLess;
    //整单折扣所摊分的金额
    private BigDecimal totalDiscountLess;
    //净销售金额（即实收金额）
    private BigDecimal netAmount;
    //货品备注
    private String salesItemRemark;
    //扩展参数
    private String extendParameter;

    public Long getSalesLineNumber() {
        return salesLineNumber;
    }

    public void setSalesLineNumber(Long salesLineNumber) {
        this.salesLineNumber = salesLineNumber;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemOrgId() {
        return itemOrgId;
    }

    public void setItemOrgId(String itemOrgId) {
        this.itemOrgId = itemOrgId;
    }

    public String getItemLotNum() {
        return itemLotNum;
    }

    public void setItemLotNum(String itemLotNum) {
        this.itemLotNum = itemLotNum;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(Integer inventoryType) {
        this.inventoryType = inventoryType;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getItemDiscountLess() {
        return itemDiscountLess;
    }

    public void setItemDiscountLess(BigDecimal itemDiscountLess) {
        this.itemDiscountLess = itemDiscountLess;
    }

    public BigDecimal getTotalDiscountLess() {
        return totalDiscountLess;
    }

    public void setTotalDiscountLess(BigDecimal totalDiscountLess) {
        this.totalDiscountLess = totalDiscountLess;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getSalesItemRemark() {
        return salesItemRemark;
    }

    public void setSalesItemRemark(String salesItemRemark) {
        this.salesItemRemark = salesItemRemark;
    }

    public String getExtendParameter() {
        return extendParameter;
    }

    public void setExtendParameter(String extendParameter) {
        this.extendParameter = extendParameter;
    }
}

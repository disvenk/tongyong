package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by bruce on 2017-11-03 16:28
 */
public class MdBillDo implements Serializable{

    private Long id;

    private String billNumber;

    private String shopDetailId;

    private String shopDetailName;

    private Long stockPlanId;

    private String stockPlanName;

    private String stockPlanNumber;

    private BigDecimal billAmount;

    private Long supplierId;

    private String supplierTax;

    private String supplierName;

    private String gmtCreate;

    private String gmtModified;

    private Integer createId;

    private String createName;

    private Integer updateId;

    private String updateName;

    private Integer state;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getShopDetailName() {
        return shopDetailName;
    }

    public void setShopDetailName(String shopDetailName) {
        this.shopDetailName = shopDetailName;
    }

    public Long getStockPlanId() {
        return stockPlanId;
    }

    public void setStockPlanId(Long stockPlanId) {
        this.stockPlanId = stockPlanId;
    }

    public String getStockPlanName() {
        return stockPlanName;
    }

    public void setStockPlanName(String stockPlanName) {
        this.stockPlanName = stockPlanName;
    }

    public String getStockPlanNumber() {
        return stockPlanNumber;
    }

    public void setStockPlanNumber(String stockPlanNumber) {
        this.stockPlanNumber = stockPlanNumber;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierTax() {
        return supplierTax;
    }

    public void setSupplierTax(String supplierTax) {
        this.supplierTax = supplierTax;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}

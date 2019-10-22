package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdBill  implements  Serializable  {
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

    private Date gmtCreate;

    private Date gmtModified;

    private Integer createId;

    private String createName;

    private Integer updateId;

    private String updateName;

    private Integer state;

    private String remark;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public String getBillNumber(){

        return this.billNumber;
    }
    public void setBillNumber(String billNumber){

        this.billNumber = billNumber;
    }
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getShopDetailName(){

        return this.shopDetailName;
    }
    public void setShopDetailName(String shopDetailName){

        this.shopDetailName = shopDetailName;
    }
    public Long getStockPlanId(){

        return this.stockPlanId;
    }
    public void setStockPlanId(Long stockPlanId){

        this.stockPlanId = stockPlanId;
    }
    public String getStockPlanName(){

        return this.stockPlanName;
    }
    public void setStockPlanName(String stockPlanName){

        this.stockPlanName = stockPlanName;
    }
    public String getStockPlanNumber(){

        return this.stockPlanNumber;
    }
    public void setStockPlanNumber(String stockPlanNumber){

        this.stockPlanNumber = stockPlanNumber;
    }
    public BigDecimal getBillAmount(){

        return this.billAmount;
    }
    public void setBillAmount(BigDecimal billAmount){

        this.billAmount = billAmount;
    }
    public Long getSupplierId(){

        return this.supplierId;
    }
    public void setSupplierId(Long supplierId){

        this.supplierId = supplierId;
    }
    public String getSupplierTax(){

        return this.supplierTax;
    }
    public void setSupplierTax(String supplierTax){

        this.supplierTax = supplierTax;
    }
    public Date getGmtCreate(){

        return this.gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate){

        this.gmtCreate = gmtCreate;
    }
    public Date getGmtModified(){

        return this.gmtModified;
    }
    public void setGmtModified(Date gmtModified){

        this.gmtModified = gmtModified;
    }
    public Integer getCreateId(){

        return this.createId;
    }
    public void setCreateId(Integer createId){

        this.createId = createId;
    }
    public String getCreateName(){

        return this.createName;
    }
    public void setCreateName(String createName){

        this.createName = createName;
    }
    public Integer getUpdateId(){

        return this.updateId;
    }
    public void setUpdateId(Integer updateId){

        this.updateId = updateId;
    }
    public String getUpdateName(){

        return this.updateName;
    }
    public void setUpdateName(String updateName){

        this.updateName = updateName;
    }
    public Integer getState(){

        return this.state;
    }
    public void setState(Integer state){

        this.state = state;
    }
    public String getRemark(){

        return this.remark;
    }
    public void setRemark(String remark){

        this.remark = remark;
    }
}

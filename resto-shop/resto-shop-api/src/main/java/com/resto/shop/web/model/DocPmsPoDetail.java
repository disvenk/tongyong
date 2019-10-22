package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocPmsPoDetail  implements  Serializable  {
    private Long id;

    private Long pmsHeaderId;

    private String pmsHeaderCode;

    private Long supPriceDetailId;

    private BigDecimal purchaseMoney;

    private BigDecimal purchaseTaxMoney;

    private BigDecimal purchaseRealMoney;

    private BigDecimal purchaseRealTaxMoney;

    private BigDecimal receivedMoney;

    private BigDecimal receivedTaxMoney;

    private Integer orderDetailStatus;

    private Long materialId;

    private BigDecimal planQty;

    private BigDecimal actQty;

    private String payStatus;

    private Long unitId;

    private String unitName;

    private String specName;

    private Long specId;

    private String note;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private Integer isDelete;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getPmsHeaderId(){

        return this.pmsHeaderId;
    }
    public void setPmsHeaderId(Long pmsHeaderId){

        this.pmsHeaderId = pmsHeaderId;
    }
    public String getPmsHeaderCode(){

        return this.pmsHeaderCode;
    }
    public void setPmsHeaderCode(String pmsHeaderCode){

        this.pmsHeaderCode = pmsHeaderCode;
    }
    public Long getSupPriceDetailId(){

        return this.supPriceDetailId;
    }
    public void setSupPriceDetailId(Long supPriceDetailId){

        this.supPriceDetailId = supPriceDetailId;
    }
    public BigDecimal getPurchaseMoney(){

        return this.purchaseMoney;
    }
    public void setPurchaseMoney(BigDecimal purchaseMoney){

        this.purchaseMoney = purchaseMoney;
    }
    public BigDecimal getPurchaseTaxMoney(){

        return this.purchaseTaxMoney;
    }
    public void setPurchaseTaxMoney(BigDecimal purchaseTaxMoney){

        this.purchaseTaxMoney = purchaseTaxMoney;
    }
    public BigDecimal getPurchaseRealMoney(){

        return this.purchaseRealMoney;
    }
    public void setPurchaseRealMoney(BigDecimal purchaseRealMoney){

        this.purchaseRealMoney = purchaseRealMoney;
    }
    public BigDecimal getPurchaseRealTaxMoney(){

        return this.purchaseRealTaxMoney;
    }
    public void setPurchaseRealTaxMoney(BigDecimal purchaseRealTaxMoney){

        this.purchaseRealTaxMoney = purchaseRealTaxMoney;
    }
    public BigDecimal getReceivedMoney(){

        return this.receivedMoney;
    }
    public void setReceivedMoney(BigDecimal receivedMoney){

        this.receivedMoney = receivedMoney;
    }
    public BigDecimal getReceivedTaxMoney(){

        return this.receivedTaxMoney;
    }
    public void setReceivedTaxMoney(BigDecimal receivedTaxMoney){

        this.receivedTaxMoney = receivedTaxMoney;
    }
    public Integer getOrderDetailStatus(){

        return this.orderDetailStatus;
    }
    public void setOrderDetailStatus(Integer orderDetailStatus){

        this.orderDetailStatus = orderDetailStatus;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public BigDecimal getPlanQty(){

        return this.planQty;
    }
    public void setPlanQty(BigDecimal planQty){

        this.planQty = planQty;
    }
    public BigDecimal getActQty(){

        return this.actQty;
    }
    public void setActQty(BigDecimal actQty){

        this.actQty = actQty;
    }
    public String getPayStatus(){

        return this.payStatus;
    }
    public void setPayStatus(String payStatus){

        this.payStatus = payStatus;
    }
    public Long getUnitId(){

        return this.unitId;
    }
    public void setUnitId(Long unitId){

        this.unitId = unitId;
    }
    public String getUnitName(){

        return this.unitName;
    }
    public void setUnitName(String unitName){

        this.unitName = unitName;
    }
    public String getSpecName(){

        return this.specName;
    }
    public void setSpecName(String specName){

        this.specName = specName;
    }
    public Long getSpecId(){

        return this.specId;
    }
    public void setSpecId(Long specId){

        this.specId = specId;
    }
    public String getNote(){

        return this.note;
    }
    public void setNote(String note){

        this.note = note;
    }
    public String getCreaterId(){

        return this.createrId;
    }
    public void setCreaterId(String createrId){

        this.createrId = createrId;
    }
    public String getCreaterName(){

        return this.createrName;
    }
    public void setCreaterName(String createrName){

        this.createrName = createrName;
    }
    public Date getGmtCreate(){

        return this.gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate){

        this.gmtCreate = gmtCreate;
    }
    public String getUpdaterId(){

        return this.updaterId;
    }
    public void setUpdaterId(String updaterId){

        this.updaterId = updaterId;
    }
    public String getUpdaterName(){

        return this.updaterName;
    }
    public void setUpdaterName(String updaterName){

        this.updaterName = updaterName;
    }
    public Date getGmtModified(){

        return this.gmtModified;
    }
    public void setGmtModified(Date gmtModified){

        this.gmtModified = gmtModified;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
}

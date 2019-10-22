package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocStkInPlanDetail  implements  Serializable  {
    private Long id;

    private Long stkInHeaderId;

    private String stkInHeaderCode;

    //TODO delete
    private Long supPriceDetailId;

    private Long pmsDetailId;


    private String orderDetailStatus;

    private Long materialId;

    private BigDecimal planQty;

    private BigDecimal actQty;
    private BigDecimal purchaseMoney;

    private Long unitId;

    private String unitName;

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
    public Long getStkInHeaderId(){

        return this.stkInHeaderId;
    }
    public void setStkInHeaderId(Long stkInHeaderId){

        this.stkInHeaderId = stkInHeaderId;
    }
    public String getStkInHeaderCode(){

        return this.stkInHeaderCode;
    }
    public void setStkInHeaderCode(String stkInHeaderCode){

        this.stkInHeaderCode = stkInHeaderCode;
    }
    public Long getSupPriceDetailId(){

        return this.supPriceDetailId;
    }
    public void setSupPriceDetailId(Long supPriceDetailId){

        this.supPriceDetailId = supPriceDetailId;
    }
    public String getOrderDetailStatus(){

        return this.orderDetailStatus;
    }
    public void setOrderDetailStatus(String orderDetailStatus){

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
    public String getNote(){

        return this.note;
    }
    public void setNote(String note){

        this.note = note;
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

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }


    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }


    public Long getPmsDetailId() {
        return pmsDetailId;
    }

    public void setPmsDetailId(Long pmsDetailId) {
        this.pmsDetailId = pmsDetailId;
    }

    public BigDecimal getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(BigDecimal purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    @Override
    public String toString() {
        return "DocStkInPlanDetail{" +
                "id=" + id +
                ", stkInHeaderId=" + stkInHeaderId +
                ", stkInHeaderCode='" + stkInHeaderCode + '\'' +
                ", supPriceDetailId=" + supPriceDetailId +
                ", pmsDetailId=" + pmsDetailId +
                ", orderDetailStatus='" + orderDetailStatus + '\'' +
                ", materialId=" + materialId +
                ", planQty=" + planQty +
                ", actQty=" + actQty +
                ", purchaseMoney=" + purchaseMoney +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", note='" + note + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", gmtModified=" + gmtModified +
                ", isDelete=" + isDelete +
                '}';
    }
}

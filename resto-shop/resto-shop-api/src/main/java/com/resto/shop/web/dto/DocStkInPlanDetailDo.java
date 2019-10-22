package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocStkInPlanDetailDo implements  Serializable  {
    private static final long serialVersionUID = -7842784688749979616L;
    private Long id;

    private Long stkInHeaderId;

    private String stkInHeaderCode;

    private Long supPriceDetailId;

    private Long pmsDetailId;

    private String orderDetailStatus;

    private Long materialId;

    private BigDecimal planQty;//计划采购数量

    private BigDecimal actQty;//实际入库数量

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
    private String topEmail;
    private String topMobile;
    private String topContact;
    private String supName;
    private String categoryOneName;
    private String categoryThirdName;
    private String categoryTwoName;
    private String cityName;
    private String materialName;
    private String districtName;
    private String materialCode;
    private BigDecimal measureUnit;
    private BigDecimal purchaseMoney;
    private String provinceName;
    private String materialType;
    private String specName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStkInHeaderId() {
        return stkInHeaderId;
    }

    public void setStkInHeaderId(Long stkInHeaderId) {
        this.stkInHeaderId = stkInHeaderId;
    }

    public String getStkInHeaderCode() {
        return stkInHeaderCode;
    }

    public void setStkInHeaderCode(String stkInHeaderCode) {
        this.stkInHeaderCode = stkInHeaderCode;
    }

    public Long getSupPriceDetailId() {
        return supPriceDetailId;
    }

    public void setSupPriceDetailId(Long supPriceDetailId) {
        this.supPriceDetailId = supPriceDetailId;
    }

    public String getOrderDetailStatus() {
        return orderDetailStatus;
    }

    public void setOrderDetailStatus(String orderDetailStatus) {
        this.orderDetailStatus = orderDetailStatus;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public BigDecimal getActQty() {
        return actQty;
    }

    public void setActQty(BigDecimal actQty) {
        this.actQty = actQty;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }


    public String getTopEmail() {
        return topEmail;
    }

    public void setTopEmail(String topEmail) {
        this.topEmail = topEmail;
    }

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
    }

    public String getTopContact() {
        return topContact;
    }

    public void setTopContact(String topContact) {
        this.topContact = topContact;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryThirdName() {
        return categoryThirdName;
    }

    public void setCategoryThirdName(String categoryThirdName) {
        this.categoryThirdName = categoryThirdName;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public BigDecimal getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(BigDecimal measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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
        return "DocStkInPlanDetailDo{" +
                "id=" + id +
                ", stkInHeaderId=" + stkInHeaderId +
                ", stkInHeaderCode='" + stkInHeaderCode + '\'' +
                ", supPriceDetailId=" + supPriceDetailId +
                ", pmsDetailId=" + pmsDetailId +
                ", orderDetailStatus='" + orderDetailStatus + '\'' +
                ", materialId=" + materialId +
                ", planQty=" + planQty +
                ", actQty=" + actQty +
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
                ", topEmail='" + topEmail + '\'' +
                ", topMobile='" + topMobile + '\'' +
                ", topContact='" + topContact + '\'' +
                ", supName='" + supName + '\'' +
                ", categoryOneName='" + categoryOneName + '\'' +
                ", categoryThirdName='" + categoryThirdName + '\'' +
                ", categoryTwoName='" + categoryTwoName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", measureUnit=" + measureUnit +
                ", purchaseMoney=" + purchaseMoney +
                ", provinceName='" + provinceName + '\'' +
                ", materialType='" + materialType + '\'' +
                ", specName='" + specName + '\'' +
                '}';
    }
}

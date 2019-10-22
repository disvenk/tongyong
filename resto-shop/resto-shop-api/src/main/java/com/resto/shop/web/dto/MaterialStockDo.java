package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MaterialStockDo implements  Serializable  {

    private Long id;

    private Long materialId;

    private BigDecimal theoryStockCount; //不需要存到数据库

    private BigDecimal alertStockCount;

    private BigDecimal actStockCount;

    private String note;


    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private String categoryOneName;

    private String categoryThirdName;

    private String categoryTwoName;

    private String cityName;

    private String materialName;

    private String districtName;

    private String materialCode;

    private String measureUnit;

    private String provinceName;

    private String materialType;

    private String specName;

    private String unitName;

    private String initials;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getTheoryStockCount() {
        return theoryStockCount;
    }

    public void setTheoryStockCount(BigDecimal theoryStockCount) {
        this.theoryStockCount = theoryStockCount;
    }

    public BigDecimal getActStockCount() {
        return actStockCount;
    }

    public void setActStockCount(BigDecimal actStockCount) {
        this.actStockCount = actStockCount;
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

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getAlertStockCount() {
        return alertStockCount;
    }

    public void setAlertStockCount(BigDecimal alertStockCount) {
        this.alertStockCount = alertStockCount;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Override
    public String toString() {
        return "MaterialStockDo{" +
                "id=" + id +
                ", materialId=" + materialId +
                ", theoryStockCount=" + theoryStockCount +
                ", alertStockCount=" + alertStockCount +
                ", actStockCount=" + actStockCount +
                ", note='" + note + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", gmtModified=" + gmtModified +
                ", categoryOneName='" + categoryOneName + '\'' +
                ", categoryThirdName='" + categoryThirdName + '\'' +
                ", categoryTwoName='" + categoryTwoName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", measureUnit='" + measureUnit + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", materialType='" + materialType + '\'' +
                ", specName='" + specName + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}

package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocReturnDetailDo implements  Serializable {


    private static final long serialVersionUID = -1691528338039608486L;
    private Long id;

    private Long returnHeaderId;

    private String returnHeaderCode;

    private String lineStatus;

    private Integer returnCount;

    private Long materialId;

    private String note;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private String auditor;

    private Integer isDelete;


    private String categoryOneName;
    private String categoryThirdName;
    private String categoryTwoName;
    private String cityName;
    private String materialName;
    private String districtName;
    private String materialCode;
    private BigDecimal measureUnit;
    private String provinceName;
    private String materialType;
    private String specName;

    private String unitName;
    private BigDecimal minMeasureUnit;
    private String minUnitName;
    private String topEmail;

    private String topContact;

    private String topMobile;
    private String materialTypeShow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReturnHeaderId() {
        return returnHeaderId;
    }

    public void setReturnHeaderId(Long returnHeaderId) {
        this.returnHeaderId = returnHeaderId;
    }

    public String getReturnHeaderCode() {
        return returnHeaderCode;
    }

    public void setReturnHeaderCode(String returnHeaderCode) {
        this.returnHeaderCode = returnHeaderCode;
    }

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getMinMeasureUnit() {
        return minMeasureUnit;
    }

    public void setMinMeasureUnit(BigDecimal minMeasureUnit) {
        this.minMeasureUnit = minMeasureUnit;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public String getTopEmail() {
        return topEmail;
    }

    public void setTopEmail(String topEmail) {
        this.topEmail = topEmail;
    }

    public String getTopContact() {
        return topContact;
    }

    public void setTopContact(String topContact) {
        this.topContact = topContact;
    }

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
    }

    public String getMaterialTypeShow() {
        return materialTypeShow;
    }

    public void setMaterialTypeShow(String materialTypeShow) {
        this.materialTypeShow = materialTypeShow;
    }
}

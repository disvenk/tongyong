package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DocStockCountDetailDo implements  Serializable  {

    private static final long serialVersionUID = 5424592574903513648L;

    private Long stockCountHeaderId;//盘点单id
    private Long id;//
    private Long materialId;//物料id
    private String materialType;//类型
    private String categoryOneName;//一级分类名称
    private String categoryTwoName;//二级分类名称
    private String categoryThirdName;//三级分类名称
    private String materialName;//材料名称
    private String materialCode;//材料编码
    private String unitName;//规格名称
    private String measureUnit;
    private String specName;
    private String provinceName;//省
    private String cityName;//市
    private String districtName;//区
    private BigDecimal theoryStockCount;//理论库存
    private BigDecimal actStockCount;//盘点库存
    private String defferentReason;//差异原因

    public Long getStockCountHeaderId() {
        return stockCountHeaderId;
    }

    public void setStockCountHeaderId(Long stockCountHeaderId) {
        this.stockCountHeaderId = stockCountHeaderId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCategoryThirdName() {
        return categoryThirdName;
    }

    public void setCategoryThirdName(String categoryThirdName) {
        this.categoryThirdName = categoryThirdName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public String getDefferentReason() {
        return defferentReason;
    }

    public void setDefferentReason(String defferentReason) {
        this.defferentReason = defferentReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MaterialDo implements Serializable {
    private  Long id;
    private  String  barcode;
    private  String  brandName;

    private  String  materialType;
    private  String  description;
    private  String  materialCode;
    private  String  materialName;

    //规格名称
    private  String  specName;
    private  Long  specId;

    //标准单位转换系数  //标准单位id //标准单位名称
    private  BigDecimal  measureUnit;
    private  String  unitName;
    private  Long  unitId;

    private BigDecimal rate;

    //转换单位id
    private  Long  convertUnitId;
    private  String  convertUnitName;

    //最小单位
    private  String  minUnitName;
    private  BigDecimal minMeasureUnit;
    private  Long minUnitId;

    private BigDecimal coefficient;

    //一级分类信息
    private  Long  categoryOneId;
    private  String  categoryOneName;
    private  String  categoryOneCode;
    //二级分类信息
    private  Long  categoryTwoId;
    private  String  categoryTwoName;
    private  String  categoryTwoCode;
    //三级分类信息
    private  Long  categoryThirdId;
    private  String  categoryThirdName;
    private  String  categoryThirdCode;

    private  Long provinceId;
    private  String provinceName;
    private  Long cityId;
    private  String cityName;
    private  String districtName;
    private  Long districtId;

    private Integer priority;

    private Integer state;
    private String printName;
    //报价单价格
    private BigDecimal purchasePrice;
    private Long supPriceId;
    //计划采购数量
    private BigDecimal planQty =BigDecimal.ONE;
    private Integer number=0;




    //实际入库数量
    private BigDecimal   actQty;
    //采购单
    private Long pmsHeaderId;

    private  Long  supPriceDetailId;

    private  BigDecimal purchaseMoney;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getConvertUnitName() {
        return convertUnitName;
    }

    public void setConvertUnitName(String convertUnitName) {
        this.convertUnitName = convertUnitName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(BigDecimal measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryOneCode() {
        return categoryOneCode;
    }

    public void setCategoryOneCode(String categoryOneCode) {
        this.categoryOneCode = categoryOneCode;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCategoryTwoCode() {
        return categoryTwoCode;
    }

    public void setCategoryTwoCode(String categoryTwoCode) {
        this.categoryTwoCode = categoryTwoCode;
    }

    public Long getCategoryThirdId() {
        return categoryThirdId;
    }

    public void setCategoryThirdId(Long categoryThirdId) {
        this.categoryThirdId = categoryThirdId;
    }

    public String getCategoryThirdName() {
        return categoryThirdName;
    }

    public void setCategoryThirdName(String categoryThirdName) {
        this.categoryThirdName = categoryThirdName;
    }

    public String getCategoryThirdCode() {
        return categoryThirdCode;
    }

    public void setCategoryThirdCode(String categoryThirdCode) {
        this.categoryThirdCode = categoryThirdCode;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getConvertUnitId() {
        return convertUnitId;
    }

    public void setConvertUnitId(Long convertUnitId) {
        this.convertUnitId = convertUnitId;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public BigDecimal getMinMeasureUnit() {
        return minMeasureUnit;
    }

    public void setMinMeasureUnit(BigDecimal minMeasureUnit) {
        this.minMeasureUnit = minMeasureUnit;
    }

    public Long getMinUnitId() {
        return minUnitId;
    }

    public void setMinUnitId(Long minUnitId) {
        this.minUnitId = minUnitId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Long getSupPriceId() {
        return supPriceId;
    }

    public void setSupPriceId(Long supPriceId) {
        this.supPriceId = supPriceId;
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

    public Long getPmsHeaderId() {
        return pmsHeaderId;
    }

    public void setPmsHeaderId(Long pmsHeaderId) {
        this.pmsHeaderId = pmsHeaderId;
    }

    public Long getSupPriceDetailId() {
        return supPriceDetailId;
    }

    public void setSupPriceDetailId(Long supPriceDetailId) {
        this.supPriceDetailId = supPriceDetailId;
    }

    public BigDecimal getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(BigDecimal purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "MaterialDo{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", brandName='" + brandName + '\'' +
                ", materialType='" + materialType + '\'' +
                ", description='" + description + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specName='" + specName + '\'' +
                ", specId=" + specId +
                ", measureUnit=" + measureUnit +
                ", unitName='" + unitName + '\'' +
                ", unitId=" + unitId +
                ", rate=" + rate +
                ", convertUnitId=" + convertUnitId +
                ", convertUnitName='" + convertUnitName + '\'' +
                ", minUnitName='" + minUnitName + '\'' +
                ", minMeasureUnit=" + minMeasureUnit +
                ", minUnitId=" + minUnitId +
                ", coefficient=" + coefficient +
                ", categoryOneId=" + categoryOneId +
                ", categoryOneName='" + categoryOneName + '\'' +
                ", categoryOneCode='" + categoryOneCode + '\'' +
                ", categoryTwoId=" + categoryTwoId +
                ", categoryTwoName='" + categoryTwoName + '\'' +
                ", categoryTwoCode='" + categoryTwoCode + '\'' +
                ", categoryThirdId=" + categoryThirdId +
                ", categoryThirdName='" + categoryThirdName + '\'' +
                ", categoryThirdCode='" + categoryThirdCode + '\'' +
                ", provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", districtId=" + districtId +
                ", priority=" + priority +
                ", state=" + state +
                ", printName='" + printName + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", supPriceId=" + supPriceId +
                ", planQty=" + planQty +
                ", number=" + number +
                ", actQty=" + actQty +
                ", pmsHeaderId=" + pmsHeaderId +
                ", supPriceDetailId=" + supPriceDetailId +
                ", purchaseMoney=" + purchaseMoney +
                '}';
    }
}

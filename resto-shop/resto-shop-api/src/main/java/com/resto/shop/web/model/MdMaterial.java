package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdMaterial  implements  Serializable  {
    private Long id;

    private String shopDetailId;

    private String materialCode;

    private String barcode;

    private String materialName;

    private Long categoryOneId;

    private Long categoryTwoId;

    private Long categoryThirdId;

    private String description;

    //库存规格  --示范   1箱
    private String specName;

    private Long specId;


    /**
     *核算规格的单位名称  50kg/箱
     *  核算规格的单位数值   核算规格的单位名称/规格名称
     *        50                    kg    /  箱
     * */
    private Long unitId;

    //核算规格的单位数值
    private BigDecimal measureUnit;

    //核算规格的单位名称  50千克
    private String unitName;

    /**
     *  核算规格的单位---最小单位的转化率
     *  kg    --------  g    1000 rate
     *  还有一些自己定义转化率
     *  件  -------------个    每件多少个 10个/件
     * */
     //转化率
    private BigDecimal rate;

    //最小转化系数
    private BigDecimal coefficient;

    //最小单位
    private String minUnitName;

    private Long minUnitId;

    //最小单位转化系数
    private BigDecimal minMeasureUnit;

    private Long convertUnitId;

    private String convertUnitName;


    private String materialType;

    private String printName;

    private String createrId;

    private String createrName;

    private String updaterId;

    private String updaterName;

    private Long version;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer provinceId;

    private String provinceName;

    private Integer cityId;

    private String cityName;

    private String districtName;

    private Integer districtId;

    private Integer priority;

    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getCategoryOneId() {
        return categoryOneId;
    }

    public void setCategoryOneId(Long categoryOneId) {
        this.categoryOneId = categoryOneId;
    }

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
    }

    public Long getCategoryThirdId() {
        return categoryThirdId;
    }

    public void setCategoryThirdId(Long categoryThirdId) {
        this.categoryThirdId = categoryThirdId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(BigDecimal measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public Long getMinUnitId() {
        return minUnitId;
    }

    public void setMinUnitId(Long minUnitId) {
        this.minUnitId = minUnitId;
    }

    public BigDecimal getMinMeasureUnit() {
        return minMeasureUnit;
    }

    public void setMinMeasureUnit(BigDecimal minMeasureUnit) {
        this.minMeasureUnit = minMeasureUnit;
    }

    public Long getConvertUnitId() {
        return convertUnitId;
    }

    public void setConvertUnitId(Long convertUnitId) {
        this.convertUnitId = convertUnitId;
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

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
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

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MdMaterial{" +
                "id=" + id +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", barcode='" + barcode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", categoryOneId=" + categoryOneId +
                ", categoryTwoId=" + categoryTwoId +
                ", categoryThirdId=" + categoryThirdId +
                ", description='" + description + '\'' +
                ", specName='" + specName + '\'' +
                ", specId=" + specId +
                ", unitId=" + unitId +
                ", measureUnit=" + measureUnit +
                ", unitName='" + unitName + '\'' +
                ", rate=" + rate +
                ", coefficient=" + coefficient +
                ", minUnitName='" + minUnitName + '\'' +
                ", minUnitId=" + minUnitId +
                ", minMeasureUnit=" + minMeasureUnit +
                ", convertUnitId=" + convertUnitId +
                ", convertUnitName='" + convertUnitName + '\'' +
                ", materialType='" + materialType + '\'' +
                ", printName='" + printName + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", version=" + version +
                ", isDelete=" + isDelete +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", districtId=" + districtId +
                ", priority=" + priority +
                ", state=" + state +
                '}';
    }
}

package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdSupplierPriceDetail  implements  Serializable  {
    private Long id;

    private Long materialId;

    private String materialCode;

    private String supDetailStatus;

    private Long supPriceId;

    private String supPriceNo;

    private BigDecimal purchasePrice;

    private Integer version;

    private Integer isDelete;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private Long supplierId;

    private String shopDetailId;





    //查询返回
    private Long categoryIdOne;
    private String categoryOneCode;
    private String categoryOneName;

    private String categoryTwoName;
    private Long categoryTwoId;
    private String categoryTwoCode;

    private Long categoryThirdId;
    private String categoryThirdCode;
    private String categoryThirdName;
    private String cityName;
    private String provinceName;
    private String districtName;

    private String minUnitName;
    private BigDecimal minMeasureUnit;
    private String unitName;
    private String specName;

    private String materialName;

    private BigDecimal measureUnit;


    private String materialType;


    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public String getMaterialCode(){

        return this.materialCode;
    }
    public void setMaterialCode(String materialCode){

        this.materialCode = materialCode;
    }
    public String getSupDetailStatus(){

        return this.supDetailStatus;
    }
    public void setSupDetailStatus(String supDetailStatus){

        this.supDetailStatus = supDetailStatus;
    }
    public Long getSupPriceId(){

        return this.supPriceId;
    }
    public void setSupPriceId(Long supPriceId){

        this.supPriceId = supPriceId;
    }
    public String getSupPriceNo(){

        return this.supPriceNo;
    }
    public void setSupPriceNo(String supPriceNo){

        this.supPriceNo = supPriceNo;
    }
    public BigDecimal getPurchasePrice(){

        return this.purchasePrice;
    }
    public void setPurchasePrice(BigDecimal purchasePrice){

        this.purchasePrice = purchasePrice;
    }
    public Integer getVersion(){

        return this.version;
    }
    public void setVersion(Integer version){

        this.version = version;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }

    public Long getCategoryIdOne() {
        return categoryIdOne;
    }

    public void setCategoryIdOne(Long categoryIdOne) {
        this.categoryIdOne = categoryIdOne;
    }

    public String getCategoryOneCode() {
        return categoryOneCode;
    }

    public void setCategoryOneCode(String categoryOneCode) {
        this.categoryOneCode = categoryOneCode;
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

    public Long getCategoryTwoId() {
        return categoryTwoId;
    }

    public void setCategoryTwoId(Long categoryTwoId) {
        this.categoryTwoId = categoryTwoId;
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

    public String getCategoryThirdCode() {
        return categoryThirdCode;
    }

    public void setCategoryThirdCode(String categoryThirdCode) {
        this.categoryThirdCode = categoryThirdCode;
    }

    public String getCategoryThirdName() {
        return categoryThirdName;
    }

    public void setCategoryThirdName(String categoryThirdName) {
        this.categoryThirdName = categoryThirdName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    @Override
    public String toString() {
        return "MdSupplierPriceDetail{" +
                "id=" + id +
                ", materialId=" + materialId +
                ", materialCode='" + materialCode + '\'' +
                ", supDetailStatus='" + supDetailStatus + '\'' +
                ", supPriceId=" + supPriceId +
                ", supPriceNo='" + supPriceNo + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", version=" + version +
                ", isDelete=" + isDelete +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", gmtModified=" + gmtModified +
                ", supplierId=" + supplierId +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", categoryIdOne=" + categoryIdOne +
                ", categoryOneCode='" + categoryOneCode + '\'' +
                ", categoryOneName='" + categoryOneName + '\'' +
                ", categoryTwoName='" + categoryTwoName + '\'' +
                ", categoryTwoId=" + categoryTwoId +
                ", categoryTwoCode='" + categoryTwoCode + '\'' +
                ", categoryThirdId=" + categoryThirdId +
                ", categoryThirdCode='" + categoryThirdCode + '\'' +
                ", categoryThirdName='" + categoryThirdName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", minUnitName='" + minUnitName + '\'' +
                ", minMeasureUnit=" + minMeasureUnit +
                ", unitName='" + unitName + '\'' +
                ", specName='" + specName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", measureUnit=" + measureUnit +
                ", materialType='" + materialType + '\'' +
                '}';
    }
}

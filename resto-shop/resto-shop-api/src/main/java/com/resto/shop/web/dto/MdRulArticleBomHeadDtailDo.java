package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdRulArticleBomHeadDtailDo implements  Serializable  {
    private Long id;
    private String bomCode;

    private String articleFamilyId;

    private String articleId;

    private String shopDetailId;

    private String productCode;

    private String productCategory;

    private String productName;

    private String measurementUnit;

    private String createrId;

    private String createrName;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer priority;

    private Integer version;

    private Integer state;

    private Date startEffect;

    private Date endEffect;

    private Integer maxVersion;

    private String producer;

    // bom 明细

    private Long bomDetailId;

    private Long articleBomHeadId;

    private BigDecimal minMeasureUnit;

    private String unitName;

    private String materialName;

    private String specName;

    private String materialCode;

    private Long materialId;

    private BigDecimal materialCount;

    private String articleName;

    private String familyName;

    private String measureUnit;

    private String minUnitName;

    private String materialType;

    private String materialTypeShow;

    private BigDecimal lossFactor;

    private BigDecimal actLossFactor;

    private BigDecimal rate;

    private BigDecimal coefficient;


    //明细版本号取表头
    // private Integer version;
    //
    // private Integer state;

    // private Date startEffect;
    //
    // private Date endEffect;

    public String getMaterialTypeShow() {
        return materialTypeShow;
    }

    public void setMaterialTypeShow(String materialTypeShow) {
        this.materialTypeShow = materialTypeShow;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }

    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getProductCode(){

        return this.productCode;
    }
    public void setProductCode(String productCode){

        this.productCode = productCode;
    }
    public String getProductCategory(){

        return this.productCategory;
    }
    public void setProductCategory(String productCategory){

        this.productCategory = productCategory;
    }
    public String getProductName(){

        return this.productName;
    }
    public void setProductName(String productName){

        this.productName = productName;
    }
    public String getMeasurementUnit(){

        return this.measurementUnit;
    }
    public void setMeasurementUnit(String measurementUnit){

        this.measurementUnit = measurementUnit;
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
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
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
    public Integer getPriority(){

        return this.priority;
    }
    public void setPriority(Integer priority){

        this.priority = priority;
    }


    public String getArticleFamilyId() {
        return articleFamilyId;
    }

    public void setArticleFamilyId(String articleFamilyId) {
        this.articleFamilyId = articleFamilyId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }


    public Long getBomDetailId() {
        return bomDetailId;
    }

    public void setBomDetailId(Long bomDetailId) {
        this.bomDetailId = bomDetailId;
    }

    public Long getArticleBomHeadId() {
        return articleBomHeadId;
    }

    public void setArticleBomHeadId(Long articleBomHeadId) {
        this.articleBomHeadId = articleBomHeadId;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(BigDecimal materialCount) {
        this.materialCount = materialCount;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getStartEffect() {
        return startEffect;
    }

    public void setStartEffect(Date startEffect) {
        this.startEffect = startEffect;
    }

    public Date getEndEffect() {
        return endEffect;
    }

    public void setEndEffect(Date endEffect) {
        this.endEffect = endEffect;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(Integer maxVersion) {
        this.maxVersion = maxVersion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }


    public BigDecimal getLossFactor() {
        return lossFactor;
    }

    public void setLossFactor(BigDecimal lossFactor) {
        this.lossFactor = lossFactor;
    }

    public BigDecimal getActLossFactor() {
        return actLossFactor;
    }

    public void setActLossFactor(BigDecimal actLossFactor) {
        this.actLossFactor = actLossFactor;
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

    @Override
    public String toString() {
        return "MdRulArticleBomHeadDtailDo{" +
                "id=" + id +
                ", bomCode='" + bomCode + '\'' +
                ", articleFamilyId='" + articleFamilyId + '\'' +
                ", articleId='" + articleId + '\'' +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productName='" + productName + '\'' +
                ", measurementUnit='" + measurementUnit + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", isDelete=" + isDelete +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", priority=" + priority +
                ", version=" + version +
                ", state=" + state +
                ", startEffect=" + startEffect +
                ", endEffect=" + endEffect +
                ", maxVersion=" + maxVersion +
                ", producer='" + producer + '\'' +
                ", bomDetailId=" + bomDetailId +
                ", articleBomHeadId=" + articleBomHeadId +
                ", minMeasureUnit=" + minMeasureUnit +
                ", unitName='" + unitName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specName='" + specName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialId=" + materialId +
                ", materialCount=" + materialCount +
                ", articleName='" + articleName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", measureUnit='" + measureUnit + '\'' +
                ", minUnitName='" + minUnitName + '\'' +
                ", materialType='" + materialType + '\'' +
                ", materialTypeShow='" + materialTypeShow + '\'' +
                ", lossFactor=" + lossFactor +
                ", actLossFactor=" + actLossFactor +
                ", minMeasureUnit=" + minMeasureUnit +
                ", rate=" + rate +
                ", coefficient=" + coefficient +
                '}';
    }
}

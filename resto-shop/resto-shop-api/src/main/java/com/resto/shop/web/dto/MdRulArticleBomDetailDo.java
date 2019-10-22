package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdRulArticleBomDetailDo implements  Serializable  {
    private Long id;

    private Long articleBomHeadId;

    private BigDecimal minMeasureUnit;

    private String unitName;

    private String materialName;

    private String specName;

    private String materialCode;

    private Long materialId;

    private BigDecimal lossFactor;

    private BigDecimal actLossFactor;

    private BigDecimal materialCount;

    private String createrId;

    private String createrName;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtModified;


    private String measureUnit;
    private String minUnitName;

    private String materialType;
    private String materialTypeShow;

    private Integer version;

    private Integer state;


    private Date startEffect;

    private Date endEffect;

    private BigDecimal rate;

    private BigDecimal coefficient;



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
    public Long getArticleBomHeadId(){

        return this.articleBomHeadId;
    }
    public void setArticleBomHeadId(Long articleBomHeadId){

        this.articleBomHeadId = articleBomHeadId;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public BigDecimal getLossFactor(){

        return this.lossFactor;
    }
    public void setLossFactor(BigDecimal lossFactor){

        this.lossFactor = lossFactor;
    }
    public BigDecimal getActLossFactor(){

        return this.actLossFactor;
    }
    public void setActLossFactor(BigDecimal actLossFactor){

        this.actLossFactor = actLossFactor;
    }
    public BigDecimal getMaterialCount(){

        return this.materialCount;
    }
    public void setMaterialCount(BigDecimal materialCount){

        this.materialCount = materialCount;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
        return "MdRulArticleBomDetailDo{" +
                "id=" + id +
                ", articleBomHeadId=" + articleBomHeadId +
                ", minMeasureUnit=" + minMeasureUnit +
                ", unitName='" + unitName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specName='" + specName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialId=" + materialId +
                ", lossFactor=" + lossFactor +
                ", actLossFactor=" + actLossFactor +
                ", materialCount=" + materialCount +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", isDelete=" + isDelete +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", measureUnit='" + measureUnit + '\'' +
                ", minUnitName='" + minUnitName + '\'' +
                ", materialType='" + materialType + '\'' +
                ", materialTypeShow='" + materialTypeShow + '\'' +
                ", version=" + version +
                ", state=" + state +
                ", startEffect=" + startEffect +
                ", endEffect=" + endEffect +
                ", rate=" + rate +
                ", coefficient=" + coefficient +
                '}';
    }
}

package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ArticleBomDo implements  Serializable  {
    private Long id;

    private Long articleBomHeadId;

    private String articleId;

    private String articleFamilyId;

    private BigDecimal minMeasureUnit;
    //盘点单位
    private String unitName;

    private String materialName;
    //盘点规格
    private String specName;

    private String materialCode;

    private Long materialId;

    private BigDecimal lossFactor;

    private BigDecimal actLossFactor;

    //bom原材料份数
    private BigDecimal materialCount;

    private String version;

    //articleId菜品消耗总量
    private BigDecimal articleTotalCount;

    //articleId菜品消耗餐盒数量
    private BigDecimal articleMealFeeNumber;

    private int state;

    private Date startEffect;

    private Date endEffect;

    private int maxVersion;

    //转化率
    private BigDecimal rate;

    private BigDecimal coefficient;

    //原材料消耗
    private BigDecimal materialConsume;
    //转化单位
    private String convertUnitName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(BigDecimal materialCount) {
        this.materialCount = materialCount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleFamilyId() {
        return articleFamilyId;
    }

    public void setArticleFamilyId(String articleFamilyId) {
        this.articleFamilyId = articleFamilyId;
    }

    public BigDecimal getArticleTotalCount() {
        return articleTotalCount;
    }

    public void setArticleTotalCount(BigDecimal articleTotalCount) {
        this.articleTotalCount = articleTotalCount;
    }

    public BigDecimal getArticleMealFeeNumber() {
        return articleMealFeeNumber;
    }

    public void setArticleMealFeeNumber(BigDecimal articleMealFeeNumber) {
        this.articleMealFeeNumber = articleMealFeeNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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

    public int getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(int maxVersion) {
        this.maxVersion = maxVersion;
    }

    public BigDecimal getMaterialConsume() {
        return materialConsume;
    }

    public void setMaterialConsume(BigDecimal materialConsume) {
        this.materialConsume = materialConsume;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getConvertUnitName() {
        return convertUnitName;
    }

    public void setConvertUnitName(String convertUnitName) {
        this.convertUnitName = convertUnitName;
    }


    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return "ArticleBomDo{" +
                "id=" + id +
                ", articleBomHeadId=" + articleBomHeadId +
                ", articleId='" + articleId + '\'' +
                ", articleFamilyId='" + articleFamilyId + '\'' +
                ", minMeasureUnit=" + minMeasureUnit +
                ", unitName='" + unitName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specName='" + specName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialId=" + materialId +
                ", lossFactor=" + lossFactor +
                ", actLossFactor=" + actLossFactor +
                ", materialCount=" + materialCount +
                ", version='" + version + '\'' +
                ", articleTotalCount=" + articleTotalCount +
                ", articleMealFeeNumber=" + articleMealFeeNumber +
                ", state=" + state +
                ", startEffect=" + startEffect +
                ", endEffect=" + endEffect +
                ", maxVersion=" + maxVersion +
                ", rate=" + rate +
                ", coefficient=" + coefficient +
                ", materialConsume=" + materialConsume +
                ", convertUnitName='" + convertUnitName + '\'' +
                '}';
    }
}

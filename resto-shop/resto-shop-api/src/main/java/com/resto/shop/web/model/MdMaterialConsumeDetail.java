package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdMaterialConsumeDetail  implements  Serializable  {
    private Long id;

    private Long articleBomDetailId;

    private BigDecimal minMeasureUnit;

    private String unitName;

    private String articleId;

    private BigDecimal articleTotalCount;

    private BigDecimal articleMealfeeNumber;

    private String articleFamilyId;

    private Long materialId;

    private String materialType;

    private String materialName;

    private String specName;

    private String materialCode;

    private BigDecimal lossFactor;

    private BigDecimal actLossFactor;

    private BigDecimal rate;

    private BigDecimal coefficient;

    private BigDecimal materialCount;

    private BigDecimal materialConsume;

    private String createrId;

    private String createrName;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer version;

    private Integer state;

    private String shopId;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getArticleBomDetailId(){

        return this.articleBomDetailId;
    }
    public void setArticleBomDetailId(Long articleBomDetailId){

        this.articleBomDetailId = articleBomDetailId;
    }
    public BigDecimal getMinMeasureUnit(){

        return this.minMeasureUnit;
    }
    public void setMinMeasureUnit(BigDecimal minMeasureUnit){

        this.minMeasureUnit = minMeasureUnit;
    }
    public String getUnitName(){

        return this.unitName;
    }
    public void setUnitName(String unitName){

        this.unitName = unitName;
    }
    public String getArticleId(){

        return this.articleId;
    }
    public void setArticleId(String articleId){

        this.articleId = articleId;
    }
    public BigDecimal getArticleTotalCount(){

        return this.articleTotalCount;
    }
    public void setArticleTotalCount(BigDecimal articleTotalCount){

        this.articleTotalCount = articleTotalCount;
    }
    public BigDecimal getArticleMealfeeNumber(){

        return this.articleMealfeeNumber;
    }
    public void setArticleMealfeeNumber(BigDecimal articleMealfeeNumber){

        this.articleMealfeeNumber = articleMealfeeNumber;
    }
    public String getArticleFamilyId(){

        return this.articleFamilyId;
    }
    public void setArticleFamilyId(String articleFamilyId){

        this.articleFamilyId = articleFamilyId;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public String getMaterialType(){

        return this.materialType;
    }
    public void setMaterialType(String materialType){

        this.materialType = materialType;
    }
    public String getMaterialName(){

        return this.materialName;
    }
    public void setMaterialName(String materialName){

        this.materialName = materialName;
    }
    public String getSpecName(){

        return this.specName;
    }
    public void setSpecName(String specName){

        this.specName = specName;
    }
    public String getMaterialCode(){

        return this.materialCode;
    }
    public void setMaterialCode(String materialCode){

        this.materialCode = materialCode;
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
    public BigDecimal getRate(){

        return this.rate;
    }
    public void setRate(BigDecimal rate){

        this.rate = rate;
    }
    public BigDecimal getCoefficient(){

        return this.coefficient;
    }
    public void setCoefficient(BigDecimal coefficient){

        this.coefficient = coefficient;
    }
    public BigDecimal getMaterialCount(){

        return this.materialCount;
    }
    public void setMaterialCount(BigDecimal materialCount){

        this.materialCount = materialCount;
    }
    public BigDecimal getMaterialConsume(){

        return this.materialConsume;
    }
    public void setMaterialConsume(BigDecimal materialConsume){

        this.materialConsume = materialConsume;
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
    public Integer getVersion(){

        return this.version;
    }
    public void setVersion(Integer version){

        this.version = version;
    }
    public Integer getState(){

        return this.state;
    }
    public void setState(Integer state){

        this.state = state;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}

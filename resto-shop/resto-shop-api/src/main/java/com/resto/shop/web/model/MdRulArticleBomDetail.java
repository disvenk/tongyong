package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdRulArticleBomDetail  implements  Serializable  {
    private Long id;

    private Long articleBomHeadId;

    private BigDecimal minMeasureUnit;

    private String unitName;

    private String materialType;

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

    private Integer version;

    private Integer state;

    private Date startEffect;

    private Date endEffect;

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
    public Date getStartEffect(){

        return this.startEffect;
    }
    public void setStartEffect(Date startEffect){

        this.startEffect = startEffect;
    }
    public Date getEndEffect(){

        return this.endEffect;
    }
    public void setEndEffect(Date endEffect){

        this.endEffect = endEffect;
    }
}

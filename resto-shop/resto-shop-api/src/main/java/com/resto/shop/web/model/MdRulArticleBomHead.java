package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class MdRulArticleBomHead  implements  Serializable  {
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

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public String getBomCode(){

        return this.bomCode;
    }
    public void setBomCode(String bomCode){

        this.bomCode = bomCode;
    }
    public String getArticleFamilyId(){

        return this.articleFamilyId;
    }
    public void setArticleFamilyId(String articleFamilyId){

        this.articleFamilyId = articleFamilyId;
    }
    public String getArticleId(){

        return this.articleId;
    }
    public void setArticleId(String articleId){

        this.articleId = articleId;
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
    public Integer getMaxVersion(){

        return this.maxVersion;
    }
    public void setMaxVersion(Integer maxVersion){

        this.maxVersion = maxVersion;
    }
    public String getProducer(){

        return this.producer;
    }
    public void setProducer(String producer){

        this.producer = producer;
    }
}

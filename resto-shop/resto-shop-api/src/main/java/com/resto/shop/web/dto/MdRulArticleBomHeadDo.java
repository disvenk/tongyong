package com.resto.shop.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MdRulArticleBomHeadDo implements  Serializable  {
    private Long id;

    private String bomCode;

    private  String articleFamilyId;


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

    private Integer size;

    private String articleName;

    private String familyName;

    private Integer version;

    private Integer state;

    private String startEffect;

    private String endEffect;

    private Integer maxVersion;

    private String producer;


    private List<MdRulArticleBomDetailDo> bomDetailDoList = new ArrayList<>();


    //等待删除的原料id
    private List<Long> bomDetailDeleteIds;


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


    public List<MdRulArticleBomDetailDo> getBomDetailDoList() {
        return bomDetailDoList;
    }

    public void setBomDetailDoList(List<MdRulArticleBomDetailDo> bomDetailDoList) {
        this.bomDetailDoList = bomDetailDoList;
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


    public List<Long> getBomDetailDeleteIds() {
        return bomDetailDeleteIds;
    }

    public void setBomDetailDeleteIds(List<Long> bomDetailDeleteIds) {
        this.bomDetailDeleteIds = bomDetailDeleteIds;
    }


    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public String getStartEffect() {
        return startEffect;
    }

    public void setStartEffect(String startEffect) {
        this.startEffect = startEffect;
    }

    public String getEndEffect() {
        return endEffect;
    }

    public void setEndEffect(String endEffect) {
        this.endEffect = endEffect;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(Integer maxVersion) {
        this.maxVersion = maxVersion;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public String toString() {
        return "MdRulArticleBomHeadDo{" +
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
                ", size=" + size +
                ", articleName='" + articleName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", version=" + version +
                ", state=" + state +
                ", startEffect=" + startEffect +
                ", endEffect=" + endEffect +
                ", maxVersion=" + maxVersion +
                ", producer='" + producer + '\'' +
                ", bomDetailDoList=" + bomDetailDoList +
                ", bomDetailDeleteIds=" + bomDetailDeleteIds +
                '}';
    }
}

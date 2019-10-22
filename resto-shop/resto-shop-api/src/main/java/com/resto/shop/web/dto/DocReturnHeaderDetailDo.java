package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DocReturnHeaderDetailDo implements  Serializable  {

    private static final long serialVersionUID = 1069375977017712671L;
    private Long id;

    private String shopDetailId;

    private String orderCode;

    private String orderName;

    private String orderStatus;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private Date gmtModified;

    private Date publishedTime;

    private String updaterId;

    private String updaterName;

    private String supName;

    private String shopName;

    private String note;

    private String topEmail;

    private String auditor;

    private String topContact;

    private String topMobile;

    private Integer isDelete;



    private String returnHeaderId;
    private String categoryOneName;
    private String categoryThirdName;
    private String categoryTwoName;
    private String cityName;
    private String materialName;
    private String districtName;
    private String materialCode;
    private BigDecimal measureUnit;
    private String provinceName;
    private String materialType;
    private String materialTypeShow;
    private String specName;

    private String unitName;
    private BigDecimal minMeasureUnit;
    private String minUnitName;

    private Integer returnCount;
    private Integer size;

    private List<DocReturnDetailDo> docReturnDetailDos;


    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
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
    public String getOrderCode(){

        return this.orderCode;
    }
    public void setOrderCode(String orderCode){

        this.orderCode = orderCode;
    }
    public String getOrderName(){

        return this.orderName;
    }
    public void setOrderName(String orderName){

        this.orderName = orderName;
    }
    public String getOrderStatus(){

        return this.orderStatus;
    }
    public void setOrderStatus(String orderStatus){

        this.orderStatus = orderStatus;
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
    public Date getGmtModified(){

        return this.gmtModified;
    }
    public void setGmtModified(Date gmtModified){

        this.gmtModified = gmtModified;
    }
    public Date getPublishedTime(){

        return this.publishedTime;
    }
    public void setPublishedTime(Date publishedTime){

        this.publishedTime = publishedTime;
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
    public String getNote(){

        return this.note;
    }
    public void setNote(String note){

        this.note = note;
    }
    public String getAuditor(){

        return this.auditor;
    }
    public void setAuditor(String auditor){

        this.auditor = auditor;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }



    public String getTopEmail() {
        return topEmail;
    }

    public void setTopEmail(String topEmail) {
        this.topEmail = topEmail;
    }

    public String getTopContact() {
        return topContact;
    }

    public void setTopContact(String topContact) {
        this.topContact = topContact;
    }

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getReturnHeaderId() {
        return returnHeaderId;
    }

    public void setReturnHeaderId(String returnHeaderId) {
        this.returnHeaderId = returnHeaderId;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryThirdName() {
        return categoryThirdName;
    }

    public void setCategoryThirdName(String categoryThirdName) {
        this.categoryThirdName = categoryThirdName;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public BigDecimal getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(BigDecimal measureUnit) {
        this.measureUnit = measureUnit;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getMinMeasureUnit() {
        return minMeasureUnit;
    }

    public void setMinMeasureUnit(BigDecimal minMeasureUnit) {
        this.minMeasureUnit = minMeasureUnit;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public List<DocReturnDetailDo> getDocReturnDetailDos() {
        return docReturnDetailDos;
    }

    public void setDocReturnDetailDos(List<DocReturnDetailDo> docReturnDetailDos) {
        this.docReturnDetailDos = docReturnDetailDos;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public String getMaterialTypeShow() {
        return materialTypeShow;
    }

    public void setMaterialTypeShow(String materialTypeShow) {
        this.materialTypeShow = materialTypeShow;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

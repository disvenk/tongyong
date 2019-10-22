package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocPmsPoDetailDo implements  Serializable  {
    private Long id;

    private Long pmsHeaderId;

    private String pmsHeaderCode;

    private Long supPriceDetailId;

    //采购参考价格不含税
    private BigDecimal purchaseMoney;
    //采购参考价格含税
    private BigDecimal purchaseTaxMoney;
    //采购价格不含税
    private BigDecimal purchaseRealMoney;
    //采购价格含税
    private BigDecimal purchaseRealTaxMoney;
    //收货价格不含税
    private BigDecimal receivedMoney;
    //收货价格含税
    private BigDecimal receivedTaxMoney;

    private String orderDetailStatus;

    private Long materialId;

    private BigDecimal planQty;

    private BigDecimal actQty;

    private String payStatus;

    private Long unitId;

    private String unitName;

    private String specName;

    private Long specId;

    private String note;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private Integer isDelete;

    private String topEmail;
    private String topMobile;
    private String topContact;
    private String supName;
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




    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getPmsHeaderId(){

        return this.pmsHeaderId;
    }
    public void setPmsHeaderId(Long pmsHeaderId){

        this.pmsHeaderId = pmsHeaderId;
    }
    public String getPmsHeaderCode(){

        return this.pmsHeaderCode;
    }
    public void setPmsHeaderCode(String pmsHeaderCode){

        this.pmsHeaderCode = pmsHeaderCode;
    }
    public Long getSupPriceDetailId(){

        return this.supPriceDetailId;
    }
    public void setSupPriceDetailId(Long supPriceDetailId){

        this.supPriceDetailId = supPriceDetailId;
    }
    public BigDecimal getPurchaseMoney(){

        return this.purchaseMoney;
    }
    public void setPurchaseMoney(BigDecimal purchaseMoney){

        this.purchaseMoney = purchaseMoney;
    }
    public BigDecimal getPurchaseTaxMoney(){

        return this.purchaseTaxMoney;
    }
    public void setPurchaseTaxMoney(BigDecimal purchaseTaxMoney){

        this.purchaseTaxMoney = purchaseTaxMoney;
    }
    public BigDecimal getPurchaseRealMoney(){

        return this.purchaseRealMoney;
    }
    public void setPurchaseRealMoney(BigDecimal purchaseRealMoney){

        this.purchaseRealMoney = purchaseRealMoney;
    }
    public BigDecimal getPurchaseRealTaxMoney(){

        return this.purchaseRealTaxMoney;
    }
    public void setPurchaseRealTaxMoney(BigDecimal purchaseRealTaxMoney){

        this.purchaseRealTaxMoney = purchaseRealTaxMoney;
    }
    public BigDecimal getReceivedMoney(){

        return this.receivedMoney;
    }
    public void setReceivedMoney(BigDecimal receivedMoney){

        this.receivedMoney = receivedMoney;
    }
    public BigDecimal getReceivedTaxMoney(){

        return this.receivedTaxMoney;
    }
    public void setReceivedTaxMoney(BigDecimal receivedTaxMoney){

        this.receivedTaxMoney = receivedTaxMoney;
    }
    public String getOrderDetailStatus(){

        return this.orderDetailStatus;
    }
    public void setOrderDetailStatus(String orderDetailStatus){

        this.orderDetailStatus = orderDetailStatus;
    }
    public Long getMaterialId(){

        return this.materialId;
    }
    public void setMaterialId(Long materialId){

        this.materialId = materialId;
    }
    public BigDecimal getPlanQty(){

        return this.planQty;
    }
    public void setPlanQty(BigDecimal planQty){

        this.planQty = planQty;
    }
    public BigDecimal getActQty(){

        return this.actQty;
    }
    public void setActQty(BigDecimal actQty){

        this.actQty = actQty;
    }
    public String getPayStatus(){

        return this.payStatus;
    }
    public void setPayStatus(String payStatus){

        this.payStatus = payStatus;
    }
    public Long getUnitId(){

        return this.unitId;
    }
    public void setUnitId(Long unitId){

        this.unitId = unitId;
    }
    public String getUnitName(){

        return this.unitName;
    }
    public void setUnitName(String unitName){

        this.unitName = unitName;
    }
    public String getSpecName(){

        return this.specName;
    }
    public void setSpecName(String specName){

        this.specName = specName;
    }
    public Long getSpecId(){

        return this.specId;
    }
    public void setSpecId(Long specId){

        this.specId = specId;
    }
    public String getNote(){

        return this.note;
    }
    public void setNote(String note){

        this.note = note;
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

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
    }

    public String getTopContact() {
        return topContact;
    }

    public void setTopContact(String topContact) {
        this.topContact = topContact;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
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



    @Override
    public String toString() {
        return "DocPmsPoDetailDo{" +
                "id=" + id +
                ", pmsHeaderId=" + pmsHeaderId +
                ", pmsHeaderCode='" + pmsHeaderCode + '\'' +
                ", supPriceDetailId=" + supPriceDetailId +
                ", purchaseMoney=" + purchaseMoney +
                ", purchaseTaxMoney=" + purchaseTaxMoney +
                ", purchaseRealMoney=" + purchaseRealMoney +
                ", purchaseRealTaxMoney=" + purchaseRealTaxMoney +
                ", receivedMoney=" + receivedMoney +
                ", receivedTaxMoney=" + receivedTaxMoney +
                ", orderDetailStatus='" + orderDetailStatus + '\'' +
                ", materialId=" + materialId +
                ", planQty=" + planQty +
                ", actQty=" + actQty +
                ", payStatus='" + payStatus + '\'' +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", specName='" + specName + '\'' +
                ", specId=" + specId +
                ", note='" + note + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", gmtModified=" + gmtModified +
                ", isDelete=" + isDelete +
                ", topEmail='" + topEmail + '\'' +
                ", topMobile='" + topMobile + '\'' +
                ", topContact='" + topContact + '\'' +
                ", supName='" + supName + '\'' +
                ", categoryOneName='" + categoryOneName + '\'' +
                ", categoryThirdName='" + categoryThirdName + '\'' +
                ", categoryTwoName='" + categoryTwoName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", measureUnit=" + measureUnit +
                ", provinceName='" + provinceName + '\'' +
                ", materialType='" + materialType + '\'' +
                '}';
    }
}

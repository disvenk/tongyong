package com.resto.shop.web.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocPmsPoHeaderDetailDo implements  Serializable  {
    private Long id;

    private Long supplierId;

    private Long supPriceHeadId;

    private String shopDetailId;

    private String shopName;

    private String orderName;

    private String orderCode;

    private String orderStatus;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private Date gmtModified;

    private Date auditTime;

    private String auditName;

    private String updaterId;

    private String updaterName;

    private String note;

    private Integer isDelete;

    private BigDecimal tax;

    private BigDecimal totalAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectTime;

    private String   orderStatusShow;
    private int    size;


    private String payStatus;

    //采购明细
    private Long pmsDetailId;

    private Long pmsHeaderId;

    private String pmsHeaderCode;

    private Long supPriceDetailId;

    private BigDecimal purchaseMoney;

    private BigDecimal purchaseTaxMoney;

    private BigDecimal purchaseRealMoney;

    private BigDecimal purchaseRealTaxMoney;

    private BigDecimal receivedMoney;//收货价格

    private BigDecimal receivedTaxMoney;

    private BigDecimal purchasePrice;//采购价格

    private String orderDetailStatus;

    private Long materialId;

    private BigDecimal planQty;

    private BigDecimal actQty;

    private String orderDetailPayStatus;

    private Long unitId;

    private String unitName;

    private String specName;

    private Long specId;

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
    private List<DocPmsPoDetailDo> docPmsPoDetailDos = new ArrayList<>();

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getSupplierId(){

        return this.supplierId;
    }
    public void setSupplierId(Long supplierId){

        this.supplierId = supplierId;
    }
    public Long getSupPriceHeadId(){

        return this.supPriceHeadId;
    }
    public void setSupPriceHeadId(Long supPriceHeadId){

        this.supPriceHeadId = supPriceHeadId;
    }
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getShopName(){

        return this.shopName;
    }
    public void setShopName(String shopName){

        this.shopName = shopName;
    }
    public String getOrderName(){

        return this.orderName;
    }
    public void setOrderName(String orderName){

        this.orderName = orderName;
    }
    public String getOrderCode(){

        return this.orderCode;
    }
    public void setOrderCode(String orderCode){

        this.orderCode = orderCode;
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
    public Date getAuditTime(){

        return this.auditTime;
    }
    public void setAuditTime(Date auditTime){

        this.auditTime = auditTime;
    }
    public String getAuditName(){

        return this.auditName;
    }
    public void setAuditName(String auditName){

        this.auditName = auditName;
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
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
    public BigDecimal getTax(){

        return this.tax;
    }
    public void setTax(BigDecimal tax){

        this.tax = tax;
    }
    public BigDecimal getTotalAmount(){

        return this.totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount){

        this.totalAmount = totalAmount;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public String getPayStatus(){

        return this.payStatus;
    }
    public void setPayStatus(String payStatus){

        this.payStatus = payStatus;
    }


    public Long getPmsDetailId() {
        return pmsDetailId;
    }

    public void setPmsDetailId(Long pmsDetailId) {
        this.pmsDetailId = pmsDetailId;
    }

    public Long getPmsHeaderId() {
        return pmsHeaderId;
    }

    public void setPmsHeaderId(Long pmsHeaderId) {
        this.pmsHeaderId = pmsHeaderId;
    }

    public String getPmsHeaderCode() {
        return pmsHeaderCode;
    }

    public void setPmsHeaderCode(String pmsHeaderCode) {
        this.pmsHeaderCode = pmsHeaderCode;
    }

    public Long getSupPriceDetailId() {
        return supPriceDetailId;
    }

    public void setSupPriceDetailId(Long supPriceDetailId) {
        this.supPriceDetailId = supPriceDetailId;
    }

    public BigDecimal getPurchaseRealMoney() {
        return purchaseRealMoney;
    }

    public void setPurchaseRealMoney(BigDecimal purchaseRealMoney) {
        this.purchaseRealMoney = purchaseRealMoney;
    }

    public BigDecimal getPurchaseRealTaxMoney() {
        return purchaseRealTaxMoney;
    }

    public void setPurchaseRealTaxMoney(BigDecimal purchaseRealTaxMoney) {
        this.purchaseRealTaxMoney = purchaseRealTaxMoney;
    }

    public String getOrderDetailStatus() {
        return orderDetailStatus;
    }

    public void setOrderDetailStatus(String orderDetailStatus) {
        this.orderDetailStatus = orderDetailStatus;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public BigDecimal getActQty() {
        return actQty;
    }

    public void setActQty(BigDecimal actQty) {
        this.actQty = actQty;
    }

    public String getOrderDetailPayStatus() {
        return orderDetailPayStatus;
    }

    public void setOrderDetailPayStatus(String orderDetailPayStatus) {
        this.orderDetailPayStatus = orderDetailPayStatus;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
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

    public List<DocPmsPoDetailDo> getDocPmsPoDetailDos() {
        return docPmsPoDetailDos;
    }

    public void setDocPmsPoDetailDos(List<DocPmsPoDetailDo> docPmsPoDetailDos) {
        this.docPmsPoDetailDos = docPmsPoDetailDos;
    }



    public BigDecimal getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(BigDecimal purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    public BigDecimal getPurchaseTaxMoney() {
        return purchaseTaxMoney;
    }

    public void setPurchaseTaxMoney(BigDecimal purchaseTaxMoney) {
        this.purchaseTaxMoney = purchaseTaxMoney;
    }

    public BigDecimal getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(BigDecimal receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public BigDecimal getReceivedTaxMoney() {
        return receivedTaxMoney;
    }

    public void setReceivedTaxMoney(BigDecimal receivedTaxMoney) {
        this.receivedTaxMoney = receivedTaxMoney;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getOrderStatusShow() {
        return orderStatusShow;
    }

    public void setOrderStatusShow(String orderStatusShow) {
        this.orderStatusShow = orderStatusShow;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DocPmsPoHeaderDetailDo{" +
                "id=" + id +
                ", supplierId=" + supplierId +
                ", supPriceHeadId=" + supPriceHeadId +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", auditTime=" + auditTime +
                ", auditName='" + auditName + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", note='" + note + '\'' +
                ", isDelete=" + isDelete +
                ", tax=" + tax +
                ", totalAmount=" + totalAmount +
                ", expectTime=" + expectTime +
                ", orderStatusShow='" + orderStatusShow + '\'' +
                ", size=" + size +
                ", payStatus='" + payStatus + '\'' +
                ", pmsDetailId=" + pmsDetailId +
                ", pmsHeaderId=" + pmsHeaderId +
                ", pmsHeaderCode='" + pmsHeaderCode + '\'' +
                ", supPriceDetailId=" + supPriceDetailId +
                ", purchaseMoney=" + purchaseMoney +
                ", purchaseTaxMoney=" + purchaseTaxMoney +
                ", purchaseRealMoney=" + purchaseRealMoney +
                ", purchaseRealTaxMoney=" + purchaseRealTaxMoney +
                ", receivedMoney=" + receivedMoney +
                ", receivedTaxMoney=" + receivedTaxMoney +
                ", purchasePrice=" + purchasePrice +
                ", orderDetailStatus='" + orderDetailStatus + '\'' +
                ", materialId=" + materialId +
                ", planQty=" + planQty +
                ", actQty=" + actQty +
                ", orderDetailPayStatus='" + orderDetailPayStatus + '\'' +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", specName='" + specName + '\'' +
                ", specId=" + specId +
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
                ", docPmsPoDetailDos=" + docPmsPoDetailDos +
                '}';
    }
}

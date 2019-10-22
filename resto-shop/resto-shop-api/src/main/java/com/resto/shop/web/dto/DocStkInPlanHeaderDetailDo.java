package com.resto.shop.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocStkInPlanHeaderDetailDo implements  Serializable {
    private Long id;

    private Long supplierId;

    private Long supPriceHeadId;

    private Long pmsHeaderId;

    private String shopDetailId;

    private String orderCode;

    private String orderStatus;

    private String orderStatusShow;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private Date gmtModified;

    private Date publishedTime;

    private String publishedName;

    private Date auditTime;

    private String auditName;

    private Date confirmTime;

    private String confirmName;

    private String updaterId;

    private String updaterName;

    private String note;

    private Integer isDelete;

    private int size;

    private String topEmail;
    private String topMobile;
    private String topContact;
    private String supName;
    private BigDecimal actQty;
    private BigDecimal planQty;
    private String orderDetailStatus;
    private String stkInHeaderId;
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
    private String materialTypes;
    private String specName;

    private String unitName;
    private BigDecimal minMeasureUnit;
    //入库价格
    private BigDecimal purchaseMoney;
    private String minUnitName;
    private String orderName;
    private String taxNumber;

    private Long pmsDetailId;

    private BigDecimal billAmount;
    private String billNumber;
    private String shopDetailName;
    private Long billId;



    private List<DocStkInPlanDetailDo> docStkInPlanDetailDoList = new ArrayList<>();


    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSupPriceHeadId() {
        return supPriceHeadId;
    }

    public void setSupPriceHeadId(Long supPriceHeadId) {
        this.supPriceHeadId = supPriceHeadId;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getPublishedName() {
        return publishedName;
    }

    public void setPublishedName(String publishedName) {
        this.publishedName = publishedName;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmName() {
        return confirmName;
    }

    public void setConfirmName(String confirmName) {
        this.confirmName = confirmName;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
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

    public BigDecimal getActQty() {
        return actQty;
    }

    public void setActQty(BigDecimal actQty) {
        this.actQty = actQty;
    }

    public String getOrderDetailStatus() {
        return orderDetailStatus;
    }

    public void setOrderDetailStatus(String orderDetailStatus) {
        this.orderDetailStatus = orderDetailStatus;
    }

    public String getStkInHeaderId() {
        return stkInHeaderId;
    }

    public void setStkInHeaderId(String stkInHeaderId) {
        this.stkInHeaderId = stkInHeaderId;
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

    public List<DocStkInPlanDetailDo> getDocStkInPlanDetailDoList() {
        return docStkInPlanDetailDoList;
    }

    public void setDocStkInPlanDetailDoList(List<DocStkInPlanDetailDo> docStkInPlanDetailDoList) {
        this.docStkInPlanDetailDoList = docStkInPlanDetailDoList;
    }


    public BigDecimal getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(BigDecimal measureUnit) {
        this.measureUnit = measureUnit;
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


    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Long getPmsHeaderId() {
        return pmsHeaderId;
    }

    public void setPmsHeaderId(Long pmsHeaderId) {
        this.pmsHeaderId = pmsHeaderId;
    }

    public Long getPmsDetailId() {
        return pmsDetailId;
    }

    public void setPmsDetailId(Long pmsDetailId) {
        this.pmsDetailId = pmsDetailId;
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

    public BigDecimal getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(BigDecimal purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }


    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }


    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getShopDetailName() {
        return shopDetailName;
    }

    public void setShopDetailName(String shopDetailName) {
        this.shopDetailName = shopDetailName;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }


    public String getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(String materialTypes) {
        this.materialTypes = materialTypes;
    }

    @Override
    public String toString() {
        return "DocStkInPlanHeaderDetailDo{" +
                "id=" + id +
                ", supplierId=" + supplierId +
                ", supPriceHeadId=" + supPriceHeadId +
                ", pmsHeaderId=" + pmsHeaderId +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderStatusShow='" + orderStatusShow + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", publishedTime=" + publishedTime +
                ", publishedName='" + publishedName + '\'' +
                ", auditTime=" + auditTime +
                ", auditName='" + auditName + '\'' +
                ", confirmTime=" + confirmTime +
                ", confirmName='" + confirmName + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", note='" + note + '\'' +
                ", isDelete=" + isDelete +
                ", size=" + size +
                ", topEmail='" + topEmail + '\'' +
                ", topMobile='" + topMobile + '\'' +
                ", topContact='" + topContact + '\'' +
                ", supName='" + supName + '\'' +
                ", actQty=" + actQty +
                ", planQty=" + planQty +
                ", orderDetailStatus='" + orderDetailStatus + '\'' +
                ", stkInHeaderId='" + stkInHeaderId + '\'' +
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
                ", materialTypes='" + materialTypes + '\'' +
                ", specName='" + specName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", minMeasureUnit=" + minMeasureUnit +
                ", purchaseMoney=" + purchaseMoney +
                ", minUnitName='" + minUnitName + '\'' +
                ", orderName='" + orderName + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", pmsDetailId=" + pmsDetailId +
                ", billAmount=" + billAmount +
                ", billNumber='" + billNumber + '\'' +
                ", shopDetailName='" + shopDetailName + '\'' +
                ", billId=" + billId +
                ", docStkInPlanDetailDoList=" + docStkInPlanDetailDoList +
                '}';
    }
}

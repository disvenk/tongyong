package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class DocStkInPlanHeader  implements  Serializable  {
    private Long id;

    private Long supplierId;

    private Long supPriceHeadId;
    private Long pmsHeaderId;

    private  String shopDetailId;

    private  String orderName;


    private String orderCode;

    private String orderStatus;

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

    private String taxNumber;

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
    public String getPublishedName(){

        return this.publishedName;
    }
    public void setPublishedName(String publishedName){

        this.publishedName = publishedName;
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
    public Date getConfirmTime(){

        return this.confirmTime;
    }
    public void setConfirmTime(Date confirmTime){

        this.confirmTime = confirmTime;
    }
    public String getConfirmName(){

        return this.confirmName;
    }
    public void setConfirmName(String confirmName){

        this.confirmName = confirmName;
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

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }


    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
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


    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    @Override
    public String toString() {
        return "DocStkInPlanHeader{" +
                "id=" + id +
                ", supplierId=" + supplierId +
                ", supPriceHeadId=" + supPriceHeadId +
                ", pmsHeaderId=" + pmsHeaderId +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
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
                ", taxNumber='" + taxNumber + '\'' +
                '}';
    }
}

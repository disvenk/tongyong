package com.resto.shop.web.dto;


import com.resto.shop.web.model.MdSupplierContact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MdSupplierDo implements  Serializable  {
    private Long id;

    private String supCode;

    private String supplierType;

    private String materialTypes;

    private String shopDetailId;

    private String supAliasName;

    private String supName;

    private String note;

    private String bankName;

    private String bankAccount;

    private String version;

    private Integer isDelete;

    private String createrId;

    private String createrName;

    private String updaterId;

    private String updaterName;

    private Date gmtCreate;

    private Date gmtModified;


    private String topContact;


    private String topMobile;

    private String topEmail;


    private Integer isTop; //是否在主表显示

    private String taxNumber;
    private String topPosition;
    private int state;
    private List<MdSupplierContact> supplierContacts = new ArrayList<>();



    //等待删除联系人id
    private List<Long> supContactIds  =new ArrayList<>();


    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public String getSupCode(){

        return this.supCode;
    }
    public void setSupCode(String supCode){

        this.supCode = supCode;
    }
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getSupName(){

        return this.supName;
    }
    public void setSupName(String supName){

        this.supName = supName;
    }

    public String getNote(){

        return this.note;
    }
    public void setNote(String note){

        this.note = note;
    }
    public String getBankName(){

        return this.bankName;
    }
    public void setBankName(String bankName){

        this.bankName = bankName;
    }
    public String getBankAccount(){

        return this.bankAccount;
    }
    public void setBankAccount(String bankAccount){

        this.bankAccount = bankAccount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<MdSupplierContact> getSupplierContacts() {
        return supplierContacts;
    }

    public void setSupplierContacts(List<MdSupplierContact> supplierContacts) {
        this.supplierContacts = supplierContacts;
    }


    public List<Long> getSupContactIds() {
        return supContactIds;
    }

    public void setSupContactIds(List<Long> supContactIds) {
        this.supContactIds = supContactIds;
    }

    public String getSupAliasName() {
        return supAliasName;
    }

    public void setSupAliasName(String supAliasName) {
        this.supAliasName = supAliasName;
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

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(String materialTypes) {
        this.materialTypes = materialTypes;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
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

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(String topPosition) {
        this.topPosition = topPosition;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MdSupplierDo{" +
                "id=" + id +
                ", supCode='" + supCode + '\'' +
                ", supplierType='" + supplierType + '\'' +
                ", materialTypes='" + materialTypes + '\'' +
                ", shopDetailId='" + shopDetailId + '\'' +
                ", supAliasName='" + supAliasName + '\'' +
                ", supName='" + supName + '\'' +
                ", note='" + note + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", version='" + version + '\'' +
                ", isDelete=" + isDelete +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", topContact='" + topContact + '\'' +
                ", topMobile='" + topMobile + '\'' +
                ", topEmail='" + topEmail + '\'' +
                ", isTop=" + isTop +
                ", supplierContacts=" + supplierContacts +
                ", supContactIds=" + supContactIds +
                '}';
    }
}

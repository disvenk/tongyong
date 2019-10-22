package com.resto.shop.web.dto;


import com.resto.shop.web.model.MdSupplierContact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MdSupplierAndContactDo implements  Serializable  {
    private Long id;

    private String supCode;

    private String supplierType;
    private String supplierTypeShow;

    private String supplierTypeDesc;

    private String materialTypes;

    private String materialIds;

    private String materialTypesDesc;

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

    private Long updaterId;

    private String updaterName;

    private Date gmtCreate;

    private Date gmtModified;

    private int state;

    // 联系人信息
    private Long supContactId;

    private String contact;


    private String mobile;

    private String email;


    private String topContact;


    private String topMobile;

    private String topEmail;

    private String topPosition;
    private Integer isTop; //是否在主表显示

    private String position;
    private String taxNumber;
    private List<MdSupplierContact> supplierContacts = new ArrayList<>();



    //等待删除联系人id
    private List<Long> supContactIds  =new ArrayList<>();

    public String getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(String materialIds) {
        this.materialIds = materialIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getSupplierTypeDesc() {
        return supplierTypeDesc;
    }

    public void setSupplierTypeDesc(String supplierTypeDesc) {
        this.supplierTypeDesc = supplierTypeDesc;
    }

    public String getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(String materialTypes) {
        this.materialTypes = materialTypes;
    }

    public String getMaterialTypesDesc() {
        return materialTypesDesc;
    }

    public void setMaterialTypesDesc(String materialTypesDesc) {
        this.materialTypesDesc = materialTypesDesc;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getSupAliasName() {
        return supAliasName;
    }

    public void setSupAliasName(String supAliasName) {
        this.supAliasName = supAliasName;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
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

    public Long getSupContactId() {
        return supContactId;
    }

    public void setSupContactId(Long supContactId) {
        this.supContactId = supContactId;
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

    public String getTopEmail() {
        return topEmail;
    }

    public void setTopEmail(String topEmail) {
        this.topEmail = topEmail;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
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


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupplierTypeShow() {
        return supplierTypeShow;
    }

    public void setSupplierTypeShow(String supplierTypeShow) {
        this.supplierTypeShow = supplierTypeShow;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(String topPosition) {
        this.topPosition = topPosition;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MdSupplierAndContactDo{" +
                "id=" + id +
                ", supCode='" + supCode + '\'' +
                ", supplierType='" + supplierType + '\'' +
                ", supplierTypeShow='" + supplierTypeShow + '\'' +
                ", supplierTypeDesc='" + supplierTypeDesc + '\'' +
                ", materialTypes='" + materialTypes + '\'' +
                ", materialIds='" + materialIds + '\'' +
                ", materialTypesDesc='" + materialTypesDesc + '\'' +
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
                ", updaterId=" + updaterId +
                ", updaterName='" + updaterName + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", supContactId=" + supContactId +
                ", contact='" + contact + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", topContact='" + topContact + '\'' +
                ", topMobile='" + topMobile + '\'' +
                ", topEmail='" + topEmail + '\'' +
                ", topPosition='" + topPosition + '\'' +
                ", isTop=" + isTop +
                ", position='" + position + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", supplierContacts=" + supplierContacts +
                ", supContactIds=" + supContactIds +
                '}';
    }
}

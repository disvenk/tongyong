package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class MdSupplier  implements  Serializable  {
    private Long id;

    private String supCode;

    private String supplierType;

    private String productTypes;

    private String shopDetailId;

    private String supAliasName;

    private String topMobile;

    private String topEmail;

    private String topContact;

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

    private String taxNumber;

    private String topPosition;

    private Integer state;

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
    public String getSupplierType(){

        return this.supplierType;
    }
    public void setSupplierType(String supplierType){

        this.supplierType = supplierType;
    }
    public String getProductTypes(){

        return this.productTypes;
    }
    public void setProductTypes(String productTypes){

        this.productTypes = productTypes;
    }
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getSupAliasName(){

        return this.supAliasName;
    }
    public void setSupAliasName(String supAliasName){

        this.supAliasName = supAliasName;
    }
    public String getTopMobile(){

        return this.topMobile;
    }
    public void setTopMobile(String topMobile){

        this.topMobile = topMobile;
    }
    public String getTopEmail(){

        return this.topEmail;
    }
    public void setTopEmail(String topEmail){

        this.topEmail = topEmail;
    }
    public String getTopContact(){

        return this.topContact;
    }
    public void setTopContact(String topContact){

        this.topContact = topContact;
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
    public String getVersion(){

        return this.version;
    }
    public void setVersion(String version){

        this.version = version;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
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
    public String getTaxNumber(){

        return this.taxNumber;
    }
    public void setTaxNumber(String taxNumber){

        this.taxNumber = taxNumber;
    }
    public String getTopPosition(){

        return this.topPosition;
    }
    public void setTopPosition(String topPosition){

        this.topPosition = topPosition;
    }
    public Integer getState(){

        return this.state;
    }
    public void setState(Integer state){

        this.state = state;
    }
}

package com.resto.shop.web.dto;


import com.resto.shop.web.model.MdSupplierPriceDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MdSupplierPriceHeadDo implements  Serializable  {
    private Long id;

    private Long supplierId;

    private Long contactId;

    private String shopDetailId;

    private String supStatus;

    private String priceNo;


    private String startEffect;

    private String endEffect;

    private Integer isEffect;

    private Integer version;

    private Integer isDelete;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    //联系方式信息
    private String contact;

    private  String email;


    private String remark;



    private String supCode;

    private String  supName;
    // 供应链原料类型  ---不是直接取供应链的物料类型，--统计报价单所有物料的类型
    private String materialTypes;
    //物料种类数 --统计报价单所有物料的类型
    private Integer materialSizes;

    private String materialName;

    private String priceName;

    private String mobile;
    private BigDecimal tax;
    private String position;
    private List<MdSupplierPriceDetail> mdSupplierPriceDetailDoList= new ArrayList<>();

    private List<Long> supplierPriceDetailDeleteIds= new ArrayList<>();

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
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getPriceNo(){

        return this.priceNo;
    }
    public void setPriceNo(String priceNo){

        this.priceNo = priceNo;
    }

    public String getStartEffect(){

        return this.startEffect;
    }
    public void setStartEffect(String startEffect){

        this.startEffect = startEffect;
    }
    public String getEndEffect(){

        return this.endEffect;
    }
    public void setEndEffect(String endEffect){

        this.endEffect = endEffect;
    }
    public Integer getIsEffect(){

        return this.isEffect;
    }
    public void setIsEffect(Integer isEffect){

        this.isEffect = isEffect;
    }
    public Integer getVersion(){

        return this.version;
    }
    public void setVersion(Integer version){

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

    public List<MdSupplierPriceDetail> getMdSupplierPriceDetailDoList() {
        return mdSupplierPriceDetailDoList;
    }

    public void setMdSupplierPriceDetailDoList(List<MdSupplierPriceDetail> mdSupplierPriceDetailDoList) {
        this.mdSupplierPriceDetailDoList = mdSupplierPriceDetailDoList;
    }

    public String getSupStatus() {
        return supStatus;
    }

    public void setSupStatus(String status, String supStatus) {
        this.supStatus = supStatus;
    }

    public List<Long> getSupplierPriceDetailDeleteIds() {
        return supplierPriceDetailDeleteIds;
    }

    public void setSupplierPriceDetailDeleteIds(List<Long> supplierPriceDetailDeleteIds) {
        this.supplierPriceDetailDeleteIds = supplierPriceDetailDeleteIds;
    }


    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getSupCode() {
        return supCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }



    public String getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(String materialTypes) {
        this.materialTypes = materialTypes;
    }

    public Integer getMaterialSizes() {
        return materialSizes;
    }

    public void setMaterialSizes(Integer materialSizes) {
        this.materialSizes = materialSizes;
    }


    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setSupStatus(String supStatus) {
        this.supStatus = supStatus;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

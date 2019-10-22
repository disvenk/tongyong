package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MdSupplierPriceHead  implements  Serializable  {
    private Long id;

    private Long supplierId;

    private String shopDetailId;

    private String supStatus;

    private Long contactId;

    private String priceName;

    private String priceNo;

    private String materialTypes;

    private Date startEffect;

    private Date endEffect;

    private String remark;

    private Integer isEffect;

    private Integer version;

    private Integer isDelete;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private String updaterId;

    private String updaterName;

    private Date gmtModified;

    private BigDecimal tax;

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
    public String getSupStatus(){

        return this.supStatus;
    }
    public void setSupStatus(String supStatus){

        this.supStatus = supStatus;
    }
    public Long getContactId(){

        return this.contactId;
    }
    public void setContactId(Long contactId){

        this.contactId = contactId;
    }
    public String getPriceName(){

        return this.priceName;
    }
    public void setPriceName(String priceName){

        this.priceName = priceName;
    }
    public String getPriceNo(){

        return this.priceNo;
    }
    public void setPriceNo(String priceNo){

        this.priceNo = priceNo;
    }
    public String getMaterialTypes(){

        return this.materialTypes;
    }
    public void setMaterialTypes(String materialTypes){

        this.materialTypes = materialTypes;
    }
    public Date getStartEffect(){

        return this.startEffect;
    }
    public void setStartEffect(Date startEffect){

        this.startEffect = startEffect;
    }
    public Date getEndEffect(){

        return this.endEffect;
    }
    public void setEndEffect(Date endEffect){

        this.endEffect = endEffect;
    }
    public String getRemark(){

        return this.remark;
    }
    public void setRemark(String remark){

        this.remark = remark;
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
    public BigDecimal getTax(){

        return this.tax;
    }
    public void setTax(BigDecimal tax){

        this.tax = tax;
    }
}

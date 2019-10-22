package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DocPmsPoHeader  implements  Serializable  {

    private Long id;

    private Long supplierId;

    private Long supPriceHeadId;

    private String shopDetailId;

    private String shopName;

    private String orderName;

    private String orderCode;

    private Integer orderStatus;

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

    private Date expectTime;

    private String payStatus;

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
    public Integer getOrderStatus(){

        return this.orderStatus;
    }
    public void setOrderStatus(Integer orderStatus){

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
    public Date getExpectTime(){

        return this.expectTime;
    }
    public void setExpectTime(Date expectTime){

        this.expectTime = expectTime;
    }
    public String getPayStatus(){

        return this.payStatus;
    }
    public void setPayStatus(String payStatus){

        this.payStatus = payStatus;
    }
}

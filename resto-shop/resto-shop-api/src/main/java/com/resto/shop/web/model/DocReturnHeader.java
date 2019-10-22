package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class DocReturnHeader  implements  Serializable  {
    private static final long serialVersionUID = 3772243502163462735L;
    private Long id;

    private String shopDetailId;

    private String orderCode;

    private Long supplierId;

    private String orderName;

    private String orderStatus;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private Date gmtModified;

    private Date publishedTime;

    private String updaterId;

    private String updaterName;

    private String note;

    private String auditor;

    private Integer isDelete;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public String getShopDetailId(){

        return this.shopDetailId;
    }
    public void setShopDetailId(String shopDetailId){

        this.shopDetailId = shopDetailId;
    }
    public String getOrderCode(){

        return this.orderCode;
    }
    public void setOrderCode(String orderCode){

        this.orderCode = orderCode;
    }
    public Long getSupplierId(){

        return this.supplierId;
    }
    public void setSupplierId(Long supplierId){

        this.supplierId = supplierId;
    }
    public String getOrderName(){

        return this.orderName;
    }
    public void setOrderName(String orderName){

        this.orderName = orderName;
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
    public Date getPublishedTime(){

        return this.publishedTime;
    }
    public void setPublishedTime(Date publishedTime){

        this.publishedTime = publishedTime;
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
    public String getAuditor(){

        return this.auditor;
    }
    public void setAuditor(String auditor){

        this.auditor = auditor;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
    }
}

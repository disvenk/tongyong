package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DocStockCountHeader  implements  Serializable  {
    private static final long serialVersionUID = 8661121001440490324L;
    private Long id;

    private String shopDetailId;

    private String orderCode;

    private String orderName;

    private String orderStatus;

    private String methodType;

    private String createrId;

    private String createrName;

    private Date gmtCreate;

    private Date gmtModified;

    private Date publishedTime;

    private Long updaterId;

    private String updaterName;

    private Date stockCountStartTime;

    private Date stockCountEndTime;

    private Date adjustStockTime;

    private Integer isRecheck;

    private Integer isBlind;

    private String note;

    private Integer isDelete;

    private List<DocStockCountDetail> stockCountDetailList;

    public List<DocStockCountDetail> getStockCountDetailList() {
        return stockCountDetailList;
    }

    public void setStockCountDetailList(List<DocStockCountDetail> stockCountDetailList) {
        this.stockCountDetailList = stockCountDetailList;
    }

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
    public String getMethodType(){

        return this.methodType;
    }
    public void setMethodType(String methodType){

        this.methodType = methodType;
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
    public Long getUpdaterId(){

        return this.updaterId;
    }
    public void setUpdaterId(Long updaterId){

        this.updaterId = updaterId;
    }
    public String getUpdaterName(){

        return this.updaterName;
    }
    public void setUpdaterName(String updaterName){

        this.updaterName = updaterName;
    }
    public Date getStockCountStartTime(){

        return this.stockCountStartTime;
    }
    public void setStockCountStartTime(Date stockCountStartTime){

        this.stockCountStartTime = stockCountStartTime;
    }
    public Date getStockCountEndTime(){

        return this.stockCountEndTime;
    }
    public void setStockCountEndTime(Date stockCountEndTime){

        this.stockCountEndTime = stockCountEndTime;
    }
    public Date getAdjustStockTime(){

        return this.adjustStockTime;
    }
    public void setAdjustStockTime(Date adjustStockTime){

        this.adjustStockTime = adjustStockTime;
    }
    public Integer getIsRecheck(){

        return this.isRecheck;
    }
    public void setIsRecheck(Integer isRecheck){

        this.isRecheck = isRecheck;
    }
    public Integer getIsBlind(){

        return this.isBlind;
    }
    public void setIsBlind(Integer isBlind){

        this.isBlind = isBlind;
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
}

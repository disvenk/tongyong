package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carl on 2016/12/16.
 */
public class TableQrcode implements Serializable {

    private Long id;

    private String brandId;

    private String shopDetailId;

    private Integer tableNumber;

    private Integer state;

    private Date createTime;

    private Date updateTime;

    private String brandName;

    private String shopName;

    private Long areaId;

    private Integer customerCount;

    private String areaName;
    private boolean seatState;

    private String tablePass;

    public boolean isSeatState() {
        return seatState;
    }

    public void setSeatState(boolean seatState) {
        this.seatState = seatState;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        if(state == null){
            this.state = 0;
        }else{
            this.state = state;
        }

    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTablePass() {
        return tablePass;
    }

    public void setTablePass(String tablePass) {
        this.tablePass = tablePass;
    }
}

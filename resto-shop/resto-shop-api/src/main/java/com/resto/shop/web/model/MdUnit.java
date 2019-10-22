package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class MdUnit implements Serializable {
    private Long id;

    private String unitCode;

    private String unitName;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer type;

    private  String shopDetailId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public String getUnitCode(){

        return this.unitCode;
    }
    public void setUnitCode(String unitCode){

        this.unitCode = unitCode;
    }
    public String getUnitName(){

        return this.unitName;
    }
    public void setUnitName(String unitName){

        this.unitName = unitName;
    }
    public Integer getIsDelete(){

        return this.isDelete;
    }
    public void setIsDelete(Integer isDelete){

        this.isDelete = isDelete;
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

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }
}

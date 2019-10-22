package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class MdMaterialConsumeOpt  implements  Serializable  {
    private Long id;

    private Date startOptTime;

    private Date endOptTime;

    private Date startOrderTime;

    private Date endOrderTime;

    private Integer optType;

    private String shopId;

    private String note;//执行操作说明

    private Integer  isDelete;



    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Date getStartOptTime(){

        return this.startOptTime;
    }
    public void setStartOptTime(Date startOptTime){

        this.startOptTime = startOptTime;
    }
    public Date getEndOptTime(){

        return this.endOptTime;
    }
    public void setEndOptTime(Date endOptTime){

        this.endOptTime = endOptTime;
    }
    public Date getStartOrderTime(){

        return this.startOrderTime;
    }
    public void setStartOrderTime(Date startOrderTime){

        this.startOrderTime = startOrderTime;
    }
    public Date getEndOrderTime(){

        return this.endOrderTime;
    }
    public void setEndOrderTime(Date endOrderTime){

        this.endOrderTime = endOrderTime;
    }
    public Integer getOptType(){

        return this.optType;
    }
    public void setOptType(Integer optType){

        this.optType = optType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}

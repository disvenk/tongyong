package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;

public class DoOperateLog  implements  Serializable  {
    private Long id;

    private Long doHeaderId;

    private String orderCode;

    private String operateType;

    private String operateReason;

    private String fmParam;

    private String toParam;

    private String operatorId;

    private String operatorName;

    private Integer isDelete;

    private Date gmtCreate;

    public Long getId(){

        return this.id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Long getDoHeaderId(){

        return this.doHeaderId;
    }
    public void setDoHeaderId(Long doHeaderId){

        this.doHeaderId = doHeaderId;
    }
    public String getOrderCode(){

        return this.orderCode;
    }
    public void setOrderCode(String orderCode){

        this.orderCode = orderCode;
    }
    public String getOperateType(){

        return this.operateType;
    }
    public void setOperateType(String operateType){

        this.operateType = operateType;
    }
    public String getOperateReason(){

        return this.operateReason;
    }
    public void setOperateReason(String operateReason){

        this.operateReason = operateReason;
    }
    public String getFmParam(){

        return this.fmParam;
    }
    public void setFmParam(String fmParam){

        this.fmParam = fmParam;
    }
    public String getToParam(){

        return this.toParam;
    }
    public void setToParam(String toParam){

        this.toParam = toParam;
    }


    public String getOperatorName(){

        return this.operatorName;
    }
    public void setOperatorName(String operatorName){

        this.operatorName = operatorName;
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


    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}

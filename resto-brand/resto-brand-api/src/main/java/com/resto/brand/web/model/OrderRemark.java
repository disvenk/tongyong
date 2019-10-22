package com.resto.brand.web.model;

import java.io.Serializable;
import java.util.Date;

public class OrderRemark implements Serializable {

    private String id;
    private String remarkName;
    private Integer sort;
    private Integer state;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public OrderRemark() {
    }

    public OrderRemark(String id, String remarkName, Integer sort, Integer state, Date createTime) {
        this.id = id;
        this.remarkName = remarkName;
        this.sort = sort;
        this.state = state;
        this.createTime = createTime;
    }
}

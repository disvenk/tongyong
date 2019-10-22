package com.resto.shop.web.posDto;

import com.resto.shop.web.model.CustomerAddress;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by KONATA on 2017/8/17.
 */
public class CustomerAddressDto implements Serializable {
    private static final long serialVersionUID = 4925086790550812835L;

    public CustomerAddressDto() {
        this.id = "";
        this.name = "";
        this.sex = 0;
        this.mobileNo = "";
        this.address = "";
        this.addressReality = "";
        this.label = "";
        this.state = 0;
        this.customerId = "";
        this.createTime = new Date().getTime();
        this.updateTime = new Date().getTime();
    }

    public CustomerAddressDto(CustomerAddress address) {
        this.id = address.getId() == null ? "" : address.getId();
        this.name = address.getName() == null ? "" : address.getName();
        this.sex = address.getSex() == null ? 0 : address.getSex();
        this.mobileNo = address.getMobileNo() == null ? "" : address.getMobileNo();
        this.address = address.getAddress() == null ? "" : address.getAddress();
        this.addressReality = address.getAddressReality() == null ? "" : address.getAddressReality();
        this.label = address.getLabel() == null ? "" : address.getLabel();
        this.state = address.getState() == null ? 0 : address.getState();
        this.customerId = address.getCustomerId() == null ? "" : address.getCustomerId();
        if (address.getCreateTime() != null) {
            this.createTime = address.getCreateTime().getTime();
        } else {
            this.createTime = new Date().getTime();
        }
        if (address.getUpdateTime() != null) {
            this.updateTime = address.getUpdateTime().getTime();
        } else {
            this.updateTime = new Date().getTime();
        }

    }


    private String id;

    private String name;

    private Integer sex;

    private String mobileNo;

    private String address;

    private String addressReality;

    private String label;

    private Integer state;

    private String customerId;

    private Long createTime;

    private Long updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressReality() {
        return addressReality;
    }

    public void setAddressReality(String addressReality) {
        this.addressReality = addressReality;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}

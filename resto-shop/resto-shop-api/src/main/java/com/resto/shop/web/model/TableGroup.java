package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
public class TableGroup implements Serializable {

    public static final Integer PAY = 1;

    public static final Integer NOT_PAY= 0;

    public static final Integer FINISH = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    private String tableNumber;

    private String groupId;

    private Date createTime;

    private Integer state;     //0 正常 1已付款 2已释放

    private String createCustomerId;     //创建者id

    private String orderId;     //该组的主订单

    private String shopDetailId;

    private String brandId;

    private List<CustomerGroup> customerGroups;


    public List<CustomerGroup> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateCustomerId() {
        return createCustomerId;
    }

    public void setCreateCustomerId(String createCustomerId) {
        this.createCustomerId = createCustomerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}

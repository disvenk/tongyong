package com.resto.shop.web.model;

import com.resto.brand.core.enums.PlatformKey;
import com.resto.brand.core.util.ApplicationUtils;
import eleme.openapi.sdk.api.entity.order.OOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PlatformOrder implements Serializable {
    private String id;

    private Integer type;

    private String platformOrderId;

    private String shopDetailId;

    private BigDecimal originalPrice;

    private BigDecimal totalPrice;

    private String address;

    private String phone;

    private String name;

    private Date orderCreateTime;

    private Date createTime;

    private String payType;

    private String remark;

    private String sourceText;

    private Integer productionStatus;

    private List<PlatformOrderDetail> platformOrderDetails;

    private List<Map<String, Object>> shopAppraises;

    private  String shopName;

    public List<Map<String, Object>> getShopAppraises() {
        return shopAppraises;
    }

    public void setShopAppraises(List<Map<String, Object>> shopAppraises) {
        this.shopAppraises = shopAppraises;
    }

    public List<PlatformOrderDetail> getPlatformOrderDetails() {
        return platformOrderDetails;
    }

    public void setPlatformOrderDetails(List<PlatformOrderDetail> platformOrderDetails) {
        this.platformOrderDetails = platformOrderDetails;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId == null ? null : platformOrderId.trim();
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText == null ? null : sourceText.trim();
    }

    public Integer getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(Integer productionStatus) {
        this.productionStatus = productionStatus;
    }

    public PlatformOrder(){}

    public PlatformOrder(HungerOrder order) {
        id = ApplicationUtils.randomUUID();
        type = PlatformKey.ELEME;
        platformOrderId = order.getOrderId();
        shopDetailId = order.getShopDetailId();
        originalPrice = order.getOriginalPrice();
        totalPrice = order.getTotalPrice();
        address = order.getAddress();
        phone = order.getPhoneList();
        name = order.getConsignee();
        orderCreateTime = order.getCreatedAt();
        createTime = new Date();
        payType = "已在线支付";
        remark = order.getDescription();
        sourceText = order.toString();
    }

    public PlatformOrder(OOrder order, String shopId){
        id = ApplicationUtils.randomUUID();
        type = PlatformKey.ELEME;
        platformOrderId = order.getId();
        shopDetailId = shopId;
        originalPrice = new BigDecimal(order.getOriginalPrice());
        totalPrice = new BigDecimal(order.getTotalPrice());
        address = order.getAddress();
        phone = order.getPhoneList().toString();
        name = order.getConsignee();
        orderCreateTime = order.getCreatedAt();
        createTime = new Date();
        payType = "已在线支付";
        remark = order.getDescription();
//        sourceText = order.toString();
    }
}
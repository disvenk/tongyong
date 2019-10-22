package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 *  美团外卖订单 dto
 * Created by Lmx on 2017/3/22.
 */
public class MeiTuanOrderDto implements Serializable {
    //美团订单ID
    private String orderId;
    //Resto+ 店铺ID
    private String ePoiId;
    //订单原价
    private Double originalPrice;
    //订单总价 （用户实际支付金额）
    private Double total;
    //收货人地址
    private String recipientAddress;
    //收货人姓名
    private String recipientName;
    //收货人电话
    private String recipientPhone;
    //订单备注
    private String caution;
    //美团订单创建时间
    private long ctime;
    //订单配送费
    private Double shippingFee;
    //订单菜品详情
    private String detail;
    //订单额外费用
    private String extras;
    //订单支付方式
    private Integer payType;
    //美团推送的订单全部JSON数据
    private String sourceText;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getePoiId() {
        return ePoiId;
    }

    public void setePoiId(String ePoiId) {
        this.ePoiId = ePoiId;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
}

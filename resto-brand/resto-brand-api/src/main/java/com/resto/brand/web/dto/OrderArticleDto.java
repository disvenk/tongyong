package com.resto.brand.web.dto;

import com.resto.brand.core.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/11.
 */
public class OrderArticleDto implements Serializable {

    private String shopId;//店铺Id
    private String shopName;//店铺名称
    private String orderId;//订单id
    private Date orderTime;//下单时间
    private String telephone;//手机号码
    private String articleName;//餐品名称
    private String articleNum;//餐品数量

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getOrderTime() {
    	return DateUtil.formatDate(this.orderTime, "yyyy-MM-dd HH:mm:ss");
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }
}

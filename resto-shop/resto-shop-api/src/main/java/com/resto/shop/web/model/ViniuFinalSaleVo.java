package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class ViniuFinalSaleVo implements Serializable {

    //实收金额
    private BigDecimal sale_money;

    //订单日期时间
    private String sale_time;

    //折扣金额
    private BigDecimal discount_money;

    //实收数据
    private List<SaleData> sale_data;

    //折扣数据
    private List<DiscountData> discount_data;

    public BigDecimal getSale_money() {
        return sale_money;
    }

    public void setSale_money(BigDecimal sale_money) {
        this.sale_money = sale_money;
    }

    public String getSale_time() {
        return sale_time;
    }

    public void setSale_time(String sale_time) {
        this.sale_time = sale_time;
    }

    public BigDecimal getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(BigDecimal discount_money) {
        this.discount_money = discount_money;
    }

    public List<SaleData> getSale_data() {
        return sale_data;
    }

    public void setSale_data(List<SaleData> sale_data) {
        this.sale_data = sale_data;
    }

    public List<DiscountData> getDiscount_data() {
        return discount_data;
    }

    public void setDiscount_data(List<DiscountData> discount_data) {
        this.discount_data = discount_data;
    }
}

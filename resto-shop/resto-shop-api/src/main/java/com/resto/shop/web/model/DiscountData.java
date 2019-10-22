package com.resto.shop.web.model;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;


public class DiscountData implements Serializable {

    //折扣类别   活动折扣
    @NotBlank
    private String discount_type;

    //折扣金额
    @NotBlank
    private BigDecimal discount_money;

    //折扣类型编码
    @NotBlank
    private String discount_typecode;

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public BigDecimal getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(BigDecimal discount_money) {
        this.discount_money = discount_money;
    }

    public String getDiscount_typecode() {
        return discount_typecode;
    }

    public void setDiscount_typecode(String discount_typecode) {
        this.discount_typecode = discount_typecode;
    }
}

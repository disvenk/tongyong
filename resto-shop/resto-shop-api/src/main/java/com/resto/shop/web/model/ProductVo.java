package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductVo implements Serializable {
    //货号
    private String code;
    //销售数量
    private Integer count;
    //货品单价
    private BigDecimal price;
    //总金额
    private BigDecimal total;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}

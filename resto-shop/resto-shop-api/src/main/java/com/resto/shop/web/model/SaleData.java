package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleData implements Serializable {

    //收入类别
    private String sale_type;

    //收入金额
    private BigDecimal sale_money;

    //收入类别编码
    private String sale_typecode;

    public String getSale_type() {
        return sale_type;
    }

    public void setSale_type(String sale_type) {
        this.sale_type = sale_type;
    }

    public BigDecimal getSale_money() {
        return sale_money;
    }

    public void setSale_money(BigDecimal sale_money) {
        this.sale_money = sale_money;
    }

    public String getSale_typecode() {
        return sale_typecode;
    }

    public void setSale_typecode(String sale_typecode) {
        this.sale_typecode = sale_typecode;
    }
}

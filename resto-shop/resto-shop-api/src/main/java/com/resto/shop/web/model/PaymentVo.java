package com.resto.shop.web.model;

import java.io.Serializable;

public class PaymentVo implements Serializable {
    //支付类型 1:现金方式，2：银行卡方式，3：其他方式
    private Integer type;
    //支付码
    private String code;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by carl on 2017/8/25.
 */
public class User implements Serializable {

    private String telephone;

    private BigDecimal money;

    private String remark;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

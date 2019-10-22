package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 * Created by yz on 2017-03-07.
 */
public class BackCustomerDto implements Serializable {
    private String customerId;//回头用户的id
    private  int num ;//回头用户出现的次数

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

package com.resto.shop.web.dto;

import java.io.Serializable;

/**
 * @author yanjuan
 * @date 17/10/25 下午6:51
 */
public class OrderNumDto implements Serializable{

    private String shopId;

    //交易笔数
    private Integer num;


    public OrderNumDto() {
    }

    public OrderNumDto(String shopId, Integer num) {
        this.shopId = shopId;
        this.num = num;
    }


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}




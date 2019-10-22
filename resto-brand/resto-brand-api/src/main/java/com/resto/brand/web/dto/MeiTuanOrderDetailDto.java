package com.resto.brand.web.dto;

import java.io.Serializable;

/**
 *  美团订单的菜品详情
 * Created by Lmx on 2017/3/23.
 */
public class MeiTuanOrderDetailDto implements Serializable {
    //菜品名
    private String food_name;
    //菜品份数
    private int quantity;
    //菜品原价
    private double price;
    //菜品规格
    private String spec;
    //餐盒费
    private double  box_price;
    //餐盒数量
    private Integer box_num;

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public double getBox_price() {
        return box_price;
    }

    public void setBox_price(double box_price) {
        this.box_price = box_price;
    }

    public Integer getBox_num() {
        return box_num;
    }

    public void setBox_num(Integer box_num) {
        this.box_num = box_num;
    }
}

package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class FoodData implements Serializable {

    //菜品编码
    private String food_code;

    //名称
    private String food_name;

    //菜品单价
    private BigDecimal food_unitprice;

    //菜品数量
    private Integer food_num;

    //菜品总价
    private BigDecimal food_totalmoney;

    //大类名称
    private String food_bname;

    //大类名称编码
    private String food_bnamecode;

    //小类名称
    private String food_sname;

    //小类名称编码
    private String food_snamecode;

    //单位
    private String food_unit;

    public String getFood_code() {
        return food_code;
    }

    public void setFood_code(String food_code) {
        this.food_code = food_code;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public BigDecimal getFood_unitprice() {
        return food_unitprice;
    }

    public void setFood_unitprice(BigDecimal food_unitprice) {
        this.food_unitprice = food_unitprice;
    }

    public Integer getFood_num() {
        return food_num;
    }

    public void setFood_num(Integer food_num) {
        this.food_num = food_num;
    }

    public BigDecimal getFood_totalmoney() {
        return food_totalmoney;
    }

    public void setFood_totalmoney(BigDecimal food_totalmoney) {
        this.food_totalmoney = food_totalmoney;
    }

    public String getFood_bname() {
        return food_bname;
    }

    public void setFood_bname(String food_bname) {
        this.food_bname = food_bname;
    }

    public String getFood_bnamecode() {
        return food_bnamecode;
    }

    public void setFood_bnamecode(String food_bnamecode) {
        this.food_bnamecode = food_bnamecode;
    }

    public String getFood_sname() {
        return food_sname;
    }

    public void setFood_sname(String food_sname) {
        this.food_sname = food_sname;
    }

    public String getFood_snamecode() {
        return food_snamecode;
    }

    public void setFood_snamecode(String food_snamecode) {
        this.food_snamecode = food_snamecode;
    }

    public String getFood_unit() {
        return food_unit;
    }

    public void setFood_unit(String food_unit) {
        this.food_unit = food_unit;
    }
}

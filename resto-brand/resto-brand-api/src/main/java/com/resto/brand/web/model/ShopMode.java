package com.resto.brand.web.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class ShopMode implements Serializable {
	public static final int TABLE_MODE = 1;  //坐下点餐模式
    public static final int CALL_NUMBER = 2; //电视叫号模式
    public static final int MANUAL_ORDER=3; //手动下单模式
    public static final int HOUFU_ORDER=5; //后付款模式
    public static final int BOSS_ORDER=6; //后付款模式
    public static final int MEISHI=7; //美食广场

	@NotNull(message="id不能为空")
    private Integer id;
    
    @NotBlank(message="店铺模式名称不能为空")
    private String name;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
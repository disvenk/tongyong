package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopModeDto implements Serializable {

    private static final long serialVersionUID = 1425210204730234267L;

	public static final int TABLE_MODE = 1;  //坐下点餐模式
    public static final int CALL_NUMBER = 2; //电视叫号模式
    public static final int MANUAL_ORDER=3; //手动下单模式
    public static final int HOUFU_ORDER=5; //后付款模式
    public static final int BOSS_ORDER=6; //后付款模式
    public static final int MEISHI=7; //美食广场

    private Integer id;

    private String name;

    private String remark;

}
package com.resto.service.brand.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ShopMode implements Serializable {

    private static final long serialVersionUID = 1453771923772424281L;
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

}
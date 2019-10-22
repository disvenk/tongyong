package com.resto.service.shop.entity;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeSetting implements Serializable{

    private static final long serialVersionUID = 1148492691272879676L;

    private String id;
    
    @NotNull(message="充值金额不能为空")
    @Min(message="充值金额最小为0",value=0)
    private BigDecimal chargeMoney;
    
    @NotNull(message="返还金额不能为空")
    @Min(message="返还金额最小为0",value=0)
    private BigDecimal rewardMoney;
    
    @NotNull(message="是否显示到菜单栏上不能为空")
    private Byte showIn;

    private String labelText;
    
    @NotNull(message="排序不能为空")
    private Integer sort;
    
    @NotNull(message="活动状态不能为空")
    private Byte state;

    private Date createTime;

    private String brandId;
    
    private String shopDetailId;

    @NotNull(message="赠送金额到账天数不能为空")
    @Min(message="赠送金额到账天数最小值为1",value=1)
    private Integer numberDay;

    // 1.首冲 2多冲 1,2首冲+多冲
    private  String firstCharge;

}
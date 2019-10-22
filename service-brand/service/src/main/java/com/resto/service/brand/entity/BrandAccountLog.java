package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BrandAccountLog implements Serializable {

    private static final long serialVersionUID = 6927208302474500176L;

    private Long id;

    private Date createTime;

    //主题V ｛注册+短信+充值 指的是品牌名字｝｛消费指的是店铺名字｝
    private String groupName;

    //行为V 10注册 20消费  30 短信
    private Integer behavior;

    private String behaviorName;//行为名字 用于报表显示

    //资金变动
    private BigDecimal foundChange;

    //账户余额
    private BigDecimal remain;

    //详情V
    private Integer detail;

    private String detailName;//详情名字 用于报表显示

    private Integer accountId;

    private String brandId;

    private String shopId;

    //流水号
    private String serialNumber;

    private  BigDecimal orderMoney; //如果是消费类型 则记录订单金额

	private Boolean isParent;

}
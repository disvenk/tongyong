package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BrandAccount implements Serializable {

    private static final long serialVersionUID = -9168068978237977804L;

    //账户id
    private Integer id;

    //品牌id
    private String brandId;

    //品牌设置id

    private String brandSettingId;

    //账户余额
    private BigDecimal accountBalance;

    //已经使用
    private BigDecimal amountUsed;

    //发票总额
    private BigDecimal totalInvoiceAmount;

    //已开发票金额
    private BigDecimal usedInvoiceAmount;

    //剩余可开发票金额
    private BigDecimal remainedInvoiceAmount;

    //最开始开启品牌账户的时间
    private Date createTime;

    //更新品牌账户的时间
    private Date updateTime;

    private String brandName;

}
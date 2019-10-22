package com.resto.shop.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xielc on 2018/6/9.
 */
@Data
public class AppraiseNewBrandDto implements Serializable{

    private static final long serialVersionUID = -4445977311720903173L;

    private String brandId;

    private String brandName;

    private Integer brandNum=0;

    private BigDecimal appraiseRatio = new BigDecimal(0);

    private BigDecimal allLevel = new BigDecimal(0);

    private BigDecimal service = new BigDecimal(0);//服务

    private BigDecimal conditions = new BigDecimal(0);//环境

    private BigDecimal price = new BigDecimal(0);//性价比

    private BigDecimal ambience = new BigDecimal(0);//氛围

    private BigDecimal exhibit = new BigDecimal(0);//出品

}

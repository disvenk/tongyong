package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xielc on 2017/7/11.
 * 用于第三方外卖报表统计
 */
@Data
public class PlatformReportDto implements Serializable {

    private String shopId;//店铺id

    private String shopName;//店铺名称

    private Integer allCount;//第三方外卖订单数

    private BigDecimal allPrice;//第三方外卖订单额

    private Integer elmCount;//饿了吗外卖订单数

    private BigDecimal elmPrice;//饿了吗外卖订单额

    private Integer mtCount;//美团外卖订单数

    private BigDecimal mtPrice;//美团外卖订单额

    private Integer bdCount;//百度外卖订单数

    private BigDecimal bdPrice;//百度外卖订单额

    private Map<String, Object> brandAppraise;

    private List<Map<String, Object>> shopAppraises;
    
}

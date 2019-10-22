package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xielc on 2017/8/11.
 * 订单列表统计店铺数据
 */
@Data
public class ShopOrderReportDto implements Serializable {

    private static final long serialVersionUID = -6142535838976719372L;

    private String shopDetailId;//店铺id

    private String shopName;//品牌名称

    private Integer shop_orderCount;//订单总数

    private BigDecimal shop_orderPrice;//订单总额

    private Integer shop_peopleCount;//就餐人数

    private Integer shop_tangshiCount;//堂食订单总数

    private BigDecimal shop_tangshiPrice;//堂食订单总额

    private Integer shop_waidaiCount;//外带订单总数

    private BigDecimal shop_waidaiPrice;//外带订单总额

    private Integer shop_waimaiCount;//外卖订单总数

    private BigDecimal shop_waimaiPrice;//外卖订单总额

    private BigDecimal shop_singlePrice;//单均

    private BigDecimal shop_perPersonPrice;//人均

    private Map<String, Object> brandOrderDto;

    private List<Map<String, Object>> shopOrderDtos;

}

package com.resto.shop.web.dto;

import com.resto.shop.web.model.OffLineOrder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yanjuan
 * @date 17/12/26 14:30
 * 存 线下订单 和 外卖订单的内容
 *
 */

@Data
public class UnderLineOrderDto implements Serializable{


    /**
     * 本日线下订单总数/订单总额
     */
    private int todayEnterCount;
    private BigDecimal todayEnterTotal;

    /**
     * /本日外卖订单数/订单总额
     */
    private int todayDeliverOrders;
    private BigDecimal todayOrderBooks;


    /**
     * 本旬线下订单总数/订单总额
     */
    private int xunEnterCount;
    private BigDecimal xunEnterTotal;

    /**
     * /本旬外卖订单数/订单总额
     */
    private int xunDeliverOrders;
    private BigDecimal xunOrderBooks;


    /**
     *
     本月线下订单总数/总额
     */
    private  int monthEnterCount;
    private  BigDecimal monthEnterTotal;


    /**
     * 本月外卖订单数/总额
     */
    private  int monthDeliverOrders;
    private BigDecimal monthOrderBooks;



}

package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yz on 2017-01-05.
 *
 * 每日报表数据输出
 * 短信方式 给店长
 */
@Data
public class EverydayReport implements Serializable {

    private static final long serialVersionUID = 705066281618682793L;

    private  String shopName ;//店铺名称

    Date date ;//当天的日期

    //今日-----------------------------------begin
    BigDecimal todayRestoTotalMoney ;//今日resto 订单总额

    BigDecimal todayEnterTotalMoney;//今日商家录入的金额

    Integer todayRestoCountOrder;//今日resto 订单总数(父子订单算一个订单)

    Integer todayEnterCountOrder;//今日商家录入的订单总数
    //新增用户消费单数
        //当天新用户(第一次下单)数
    Integer todayNewCustomerCount ;
    //当天自然到店用户
    Integer todayNewNormalCustomerCount;
    //当天分享用户
    Integer todayNewShareCustomerCount;
        //当天消费的用户总数
    Integer todayCustomerCount;
    //回头用户消费单数
    Integer todayBackCustomerCount;
    //二次回头用户消费单数
    Integer todayTwoBackCustomerCount;
    //多次回头用户消费单数
    Integer todayMoreTwoBackCustomerCount;
    String todaysatisfied;//本日满意度
    //今日-----------------------------------end

    //上旬begin---------------------------------------------------------
    BigDecimal firstOfMonthRestoTotalMoney ;//上旬resto 订单总额
    BigDecimal firstOfMonthEnterTotalMoney;//上旬商家录入的金额
    Integer firstOfMonthRestoCountOrder;//上旬resto 订单总数(父子订单算一个订单)
    Integer firstOfMonthEnterCountOrder;//上旬商家录入的订单总数
    //新增用户消费单数
    //上旬新用户(第一次下单)数
    Integer firstOfMonthNewCustomerCount ;
    //上旬消费的用户总数
    Integer firstOfMonthCustomerCount;
    //上旬回头用户消费单数
    Integer firstOfMonthBackCustomerCount;
    //二次回头用户消费单数
    Integer firstOfMonthTwoBackCustomerCount;
    //多次回头用户消费单数
    String firstOfMonthsatisfied;//上旬满意度
    //上旬end---------------------------------------------------------

    //中旬begin--------------------------------------------------
    BigDecimal middleOfMonthRestoTotalMoney ;//中旬resto 订单总额
    BigDecimal middleOfMonthEnterTotalMoney;//中旬商家录入的金额
    Integer middleOfMonthRestoCountOrder;//中旬resto 订单总数(父子订单算一个订单)
    Integer middleOfMonthEnterCountOrder;//中旬商家录入的订单总数
    //新增用户消费单数
    //中旬新用户(第一次下单)数
    Integer middleOfMonthNewCustomerCount ;
    //中旬消费的用户总数
    Integer middleOfMonthCustomerCount;
    //中旬回头用户消费单数
    Integer middleOfMonthBackCustomerCount;
    //二次回头用户消费单数
    Integer middleOfMonthTwoBackCustomerCount;
    //多次回头用户消费单数
    String middleOfMonthsatisfied;//中旬满意度
    //中旬end-------------------------------------------------------------

    //下旬begin--------------------------------------------------------
    BigDecimal lastOfMonthRestoTotalMoney ;//下旬resto 订单总额
    BigDecimal lastOfMonthEnterTotalMoney;//下旬商家录入的金额
    Integer lastOfMonthRestoCountOrder;//下旬resto 订单总数(父子订单算一个订单)
    Integer lastOfMonthEnterCountOrder;//下旬商家录入的订单总数
    //新增用户消费单数
    //下旬新用户(第一次下单)数
    Integer lastOfMonthNewCustomerCount ;
    //下旬消费的用户总数
    Integer lastOfMonthCustomerCount;
    //下旬回头用户消费单数
    Integer lastOfMonthBackCustomerCount;
    //二次回头用户消费单数
    Integer lastOfMonthTwoBackCustomerCount;
    //多次回头用户消费单数
    String lastOfMonthsatisfied;//下旬满意度
    //下旬end--------------------------------------------------------

    //本月begin----------------------------------------------------
    BigDecimal monthRestoTotalMoney ;//本月resto 订单总额
    BigDecimal monthEnterTotalMoney;//本月商家录入的金额
    Integer monthRestoCountOrder;//本月resto 订单总数(父子订单算一个订单)
    Integer monthEnterCountOrder;//本月商家录入的订单总数
    //新增用户消费单数
    //本月新用户(第一次下单)数
    Integer monthNewCustomerCount ;
    //本月消费的用户总数
    Integer monthCustomerCount;
    //本月回头用户消费单数
    Integer monthBackCustomerCount;
    //二次回头用户消费单数
    Integer monthTwoBackCustomerCount;
    //多次回头用户消费单数
    String monthsatisfied;//本月满意度
    //本月end---------------------------------------------------------
}

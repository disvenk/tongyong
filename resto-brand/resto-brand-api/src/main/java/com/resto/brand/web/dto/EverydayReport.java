package com.resto.brand.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yz on 2017-01-05.
 *
 * 每日报表数据输出
 * 短信方式 给店长
 */
public class EverydayReport implements Serializable {

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


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTodayRestoTotalMoney() {
        return todayRestoTotalMoney;
    }

    public void setTodayRestoTotalMoney(BigDecimal todayRestoTotalMoney) {
        this.todayRestoTotalMoney = todayRestoTotalMoney;
    }

    public BigDecimal getTodayEnterTotalMoney() {
        return todayEnterTotalMoney;
    }

    public void setTodayEnterTotalMoney(BigDecimal todayEnterTotalMoney) {
        this.todayEnterTotalMoney = todayEnterTotalMoney;
    }

    public Integer getTodayRestoCountOrder() {
        return todayRestoCountOrder;
    }

    public void setTodayRestoCountOrder(Integer todayRestoCountOrder) {
        this.todayRestoCountOrder = todayRestoCountOrder;
    }

    public Integer getTodayEnterCountOrder() {
        return todayEnterCountOrder;
    }

    public void setTodayEnterCountOrder(Integer todayEnterCountOrder) {
        this.todayEnterCountOrder = todayEnterCountOrder;
    }

    public Integer getTodayNewCustomerCount() {
        return todayNewCustomerCount;
    }

    public void setTodayNewCustomerCount(Integer todayNewCustomerCount) {
        this.todayNewCustomerCount = todayNewCustomerCount;
    }

    public Integer getTodayNewNormalCustomerCount() {
        return todayNewNormalCustomerCount;
    }

    public void setTodayNewNormalCustomerCount(Integer todayNewNormalCustomerCount) {
        this.todayNewNormalCustomerCount = todayNewNormalCustomerCount;
    }

    public Integer getTodayNewShareCustomerCount() {
        return todayNewShareCustomerCount;
    }

    public void setTodayNewShareCustomerCount(Integer todayNewShareCustomerCount) {
        this.todayNewShareCustomerCount = todayNewShareCustomerCount;
    }

    public Integer getTodayCustomerCount() {
        return todayCustomerCount;
    }

    public void setTodayCustomerCount(Integer todayCustomerCount) {
        this.todayCustomerCount = todayCustomerCount;
    }

    public Integer getTodayBackCustomerCount() {
        return todayBackCustomerCount;
    }

    public void setTodayBackCustomerCount(Integer todayBackCustomerCount) {
        this.todayBackCustomerCount = todayBackCustomerCount;
    }

    public Integer getTodayTwoBackCustomerCount() {
        return todayTwoBackCustomerCount;
    }

    public void setTodayTwoBackCustomerCount(Integer todayTwoBackCustomerCount) {
        this.todayTwoBackCustomerCount = todayTwoBackCustomerCount;
    }

    public Integer getTodayMoreTwoBackCustomerCount() {
        return todayMoreTwoBackCustomerCount;
    }

    public void setTodayMoreTwoBackCustomerCount(Integer todayMoreTwoBackCustomerCount) {
        this.todayMoreTwoBackCustomerCount = todayMoreTwoBackCustomerCount;
    }

    public String getTodaysatisfied() {
        return todaysatisfied;
    }

    public void setTodaysatisfied(String todaysatisfied) {
        this.todaysatisfied = todaysatisfied;
    }

    public BigDecimal getFirstOfMonthRestoTotalMoney() {
        return firstOfMonthRestoTotalMoney;
    }

    public void setFirstOfMonthRestoTotalMoney(BigDecimal firstOfMonthRestoTotalMoney) {
        this.firstOfMonthRestoTotalMoney = firstOfMonthRestoTotalMoney;
    }

    public BigDecimal getFirstOfMonthEnterTotalMoney() {
        return firstOfMonthEnterTotalMoney;
    }

    public void setFirstOfMonthEnterTotalMoney(BigDecimal firstOfMonthEnterTotalMoney) {
        this.firstOfMonthEnterTotalMoney = firstOfMonthEnterTotalMoney;
    }

    public Integer getFirstOfMonthRestoCountOrder() {
        return firstOfMonthRestoCountOrder;
    }

    public void setFirstOfMonthRestoCountOrder(Integer firstOfMonthRestoCountOrder) {
        this.firstOfMonthRestoCountOrder = firstOfMonthRestoCountOrder;
    }

    public Integer getFirstOfMonthEnterCountOrder() {
        return firstOfMonthEnterCountOrder;
    }

    public void setFirstOfMonthEnterCountOrder(Integer firstOfMonthEnterCountOrder) {
        this.firstOfMonthEnterCountOrder = firstOfMonthEnterCountOrder;
    }

    public Integer getFirstOfMonthNewCustomerCount() {
        return firstOfMonthNewCustomerCount;
    }

    public void setFirstOfMonthNewCustomerCount(Integer firstOfMonthNewCustomerCount) {
        this.firstOfMonthNewCustomerCount = firstOfMonthNewCustomerCount;
    }

    public Integer getFirstOfMonthCustomerCount() {
        return firstOfMonthCustomerCount;
    }

    public void setFirstOfMonthCustomerCount(Integer firstOfMonthCustomerCount) {
        this.firstOfMonthCustomerCount = firstOfMonthCustomerCount;
    }

    public Integer getFirstOfMonthBackCustomerCount() {
        return firstOfMonthBackCustomerCount;
    }

    public void setFirstOfMonthBackCustomerCount(Integer firstOfMonthBackCustomerCount) {
        this.firstOfMonthBackCustomerCount = firstOfMonthBackCustomerCount;
    }

    public Integer getFirstOfMonthTwoBackCustomerCount() {
        return firstOfMonthTwoBackCustomerCount;
    }

    public void setFirstOfMonthTwoBackCustomerCount(Integer firstOfMonthTwoBackCustomerCount) {
        this.firstOfMonthTwoBackCustomerCount = firstOfMonthTwoBackCustomerCount;
    }

    public String getFirstOfMonthsatisfied() {
        return firstOfMonthsatisfied;
    }

    public void setFirstOfMonthsatisfied(String firstOfMonthsatisfied) {
        this.firstOfMonthsatisfied = firstOfMonthsatisfied;
    }

    public BigDecimal getMiddleOfMonthRestoTotalMoney() {
        return middleOfMonthRestoTotalMoney;
    }

    public void setMiddleOfMonthRestoTotalMoney(BigDecimal middleOfMonthRestoTotalMoney) {
        this.middleOfMonthRestoTotalMoney = middleOfMonthRestoTotalMoney;
    }

    public BigDecimal getMiddleOfMonthEnterTotalMoney() {
        return middleOfMonthEnterTotalMoney;
    }

    public void setMiddleOfMonthEnterTotalMoney(BigDecimal middleOfMonthEnterTotalMoney) {
        this.middleOfMonthEnterTotalMoney = middleOfMonthEnterTotalMoney;
    }

    public Integer getMiddleOfMonthRestoCountOrder() {
        return middleOfMonthRestoCountOrder;
    }

    public void setMiddleOfMonthRestoCountOrder(Integer middleOfMonthRestoCountOrder) {
        this.middleOfMonthRestoCountOrder = middleOfMonthRestoCountOrder;
    }

    public Integer getMiddleOfMonthEnterCountOrder() {
        return middleOfMonthEnterCountOrder;
    }

    public void setMiddleOfMonthEnterCountOrder(Integer middleOfMonthEnterCountOrder) {
        this.middleOfMonthEnterCountOrder = middleOfMonthEnterCountOrder;
    }

    public Integer getMiddleOfMonthNewCustomerCount() {
        return middleOfMonthNewCustomerCount;
    }

    public void setMiddleOfMonthNewCustomerCount(Integer middleOfMonthNewCustomerCount) {
        this.middleOfMonthNewCustomerCount = middleOfMonthNewCustomerCount;
    }

    public Integer getMiddleOfMonthCustomerCount() {
        return middleOfMonthCustomerCount;
    }

    public void setMiddleOfMonthCustomerCount(Integer middleOfMonthCustomerCount) {
        this.middleOfMonthCustomerCount = middleOfMonthCustomerCount;
    }

    public Integer getMiddleOfMonthBackCustomerCount() {
        return middleOfMonthBackCustomerCount;
    }

    public void setMiddleOfMonthBackCustomerCount(Integer middleOfMonthBackCustomerCount) {
        this.middleOfMonthBackCustomerCount = middleOfMonthBackCustomerCount;
    }

    public Integer getMiddleOfMonthTwoBackCustomerCount() {
        return middleOfMonthTwoBackCustomerCount;
    }

    public void setMiddleOfMonthTwoBackCustomerCount(Integer middleOfMonthTwoBackCustomerCount) {
        this.middleOfMonthTwoBackCustomerCount = middleOfMonthTwoBackCustomerCount;
    }

    public String getMiddleOfMonthsatisfied() {
        return middleOfMonthsatisfied;
    }

    public void setMiddleOfMonthsatisfied(String middleOfMonthsatisfied) {
        this.middleOfMonthsatisfied = middleOfMonthsatisfied;
    }

    public BigDecimal getLastOfMonthRestoTotalMoney() {
        return lastOfMonthRestoTotalMoney;
    }

    public void setLastOfMonthRestoTotalMoney(BigDecimal lastOfMonthRestoTotalMoney) {
        this.lastOfMonthRestoTotalMoney = lastOfMonthRestoTotalMoney;
    }

    public BigDecimal getLastOfMonthEnterTotalMoney() {
        return lastOfMonthEnterTotalMoney;
    }

    public void setLastOfMonthEnterTotalMoney(BigDecimal lastOfMonthEnterTotalMoney) {
        this.lastOfMonthEnterTotalMoney = lastOfMonthEnterTotalMoney;
    }

    public Integer getLastOfMonthRestoCountOrder() {
        return lastOfMonthRestoCountOrder;
    }

    public void setLastOfMonthRestoCountOrder(Integer lastOfMonthRestoCountOrder) {
        this.lastOfMonthRestoCountOrder = lastOfMonthRestoCountOrder;
    }

    public Integer getLastOfMonthEnterCountOrder() {
        return lastOfMonthEnterCountOrder;
    }

    public void setLastOfMonthEnterCountOrder(Integer lastOfMonthEnterCountOrder) {
        this.lastOfMonthEnterCountOrder = lastOfMonthEnterCountOrder;
    }

    public Integer getLastOfMonthNewCustomerCount() {
        return lastOfMonthNewCustomerCount;
    }

    public void setLastOfMonthNewCustomerCount(Integer lastOfMonthNewCustomerCount) {
        this.lastOfMonthNewCustomerCount = lastOfMonthNewCustomerCount;
    }

    public Integer getLastOfMonthCustomerCount() {
        return lastOfMonthCustomerCount;
    }

    public void setLastOfMonthCustomerCount(Integer lastOfMonthCustomerCount) {
        this.lastOfMonthCustomerCount = lastOfMonthCustomerCount;
    }

    public Integer getLastOfMonthBackCustomerCount() {
        return lastOfMonthBackCustomerCount;
    }

    public void setLastOfMonthBackCustomerCount(Integer lastOfMonthBackCustomerCount) {
        this.lastOfMonthBackCustomerCount = lastOfMonthBackCustomerCount;
    }

    public Integer getLastOfMonthTwoBackCustomerCount() {
        return lastOfMonthTwoBackCustomerCount;
    }

    public void setLastOfMonthTwoBackCustomerCount(Integer lastOfMonthTwoBackCustomerCount) {
        this.lastOfMonthTwoBackCustomerCount = lastOfMonthTwoBackCustomerCount;
    }

    public String getLastOfMonthsatisfied() {
        return lastOfMonthsatisfied;
    }

    public void setLastOfMonthsatisfied(String lastOfMonthsatisfied) {
        this.lastOfMonthsatisfied = lastOfMonthsatisfied;
    }

    public BigDecimal getMonthRestoTotalMoney() {
        return monthRestoTotalMoney;
    }

    public void setMonthRestoTotalMoney(BigDecimal monthRestoTotalMoney) {
        this.monthRestoTotalMoney = monthRestoTotalMoney;
    }

    public BigDecimal getMonthEnterTotalMoney() {
        return monthEnterTotalMoney;
    }

    public void setMonthEnterTotalMoney(BigDecimal monthEnterTotalMoney) {
        this.monthEnterTotalMoney = monthEnterTotalMoney;
    }

    public Integer getMonthRestoCountOrder() {
        return monthRestoCountOrder;
    }

    public void setMonthRestoCountOrder(Integer monthRestoCountOrder) {
        this.monthRestoCountOrder = monthRestoCountOrder;
    }

    public Integer getMonthEnterCountOrder() {
        return monthEnterCountOrder;
    }

    public void setMonthEnterCountOrder(Integer monthEnterCountOrder) {
        this.monthEnterCountOrder = monthEnterCountOrder;
    }

    public Integer getMonthNewCustomerCount() {
        return monthNewCustomerCount;
    }

    public void setMonthNewCustomerCount(Integer monthNewCustomerCount) {
        this.monthNewCustomerCount = monthNewCustomerCount;
    }

    public Integer getMonthCustomerCount() {
        return monthCustomerCount;
    }

    public void setMonthCustomerCount(Integer monthCustomerCount) {
        this.monthCustomerCount = monthCustomerCount;
    }

    public Integer getMonthBackCustomerCount() {
        return monthBackCustomerCount;
    }

    public void setMonthBackCustomerCount(Integer monthBackCustomerCount) {
        this.monthBackCustomerCount = monthBackCustomerCount;
    }

    public Integer getMonthTwoBackCustomerCount() {
        return monthTwoBackCustomerCount;
    }

    public void setMonthTwoBackCustomerCount(Integer monthTwoBackCustomerCount) {
        this.monthTwoBackCustomerCount = monthTwoBackCustomerCount;
    }

    public String getMonthsatisfied() {
        return monthsatisfied;
    }

    public void setMonthsatisfied(String monthsatisfied) {
        this.monthsatisfied = monthsatisfied;
    }
}

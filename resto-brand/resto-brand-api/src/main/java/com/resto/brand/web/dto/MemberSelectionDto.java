package com.resto.brand.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 会员信息报表--》会员筛选
 */
public class MemberSelectionDto implements Serializable {
    private String customerType;
    private String isValue;
    private String nickname;
    private String sex;
    private String telephone;
    private String birthday;
    private String orderCount;
    private String orderMoney;
    private String avgOrderMoney;

    private List<Map<String, String>> memberSelectionDtos;

    public List<Map<String, String>> getMemberSelectionDtos() {
        return memberSelectionDtos;
    }

    public void setMemberSelectionDtos(List<Map<String, String>> memberSelectionDtos) {
        this.memberSelectionDtos = memberSelectionDtos;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIsValue() {
        return isValue;
    }

    public void setIsValue(String isValue) {
        this.isValue = isValue;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getAvgOrderMoney() {
        return avgOrderMoney;
    }

    public void setAvgOrderMoney(String avgOrderMoney) {
        this.avgOrderMoney = avgOrderMoney;
    }

    public MemberSelectionDto() {
    }

    public MemberSelectionDto(String customerType, String isValue, String nickname, String sex, String telephone, String birthday, String orderCount, String orderMoney, String avgOrderMoney) {
        this.customerType = customerType;
        this.isValue = isValue;
        this.nickname = nickname;
        this.sex = sex;
        this.telephone = telephone;
        this.birthday = birthday;
        this.orderCount = orderCount;
        this.orderMoney = orderMoney;
        this.avgOrderMoney = avgOrderMoney;
    }
}

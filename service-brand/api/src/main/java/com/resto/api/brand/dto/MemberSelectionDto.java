package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 会员信息报表--》会员筛选
 */
@Data
public class MemberSelectionDto implements Serializable {

    private static final long serialVersionUID = -9202312168525340157L;
    
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

package com.resto.brand.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户充值、品牌充值汇总信息
 */
@Data
public class ChargeTotalDto implements Serializable{

    //用户昵称
    private String nickname;

    //电话号码
    private String telephone;

    //历史充值数量
    private Integer historyChargeCount;

    //历史充值金额
    private BigDecimal historyChargeMoney;

    //历史充值赠送金额
    private BigDecimal historyRewardMoney;

    //账户余额
    private BigDecimal remain;

    //历史充值剩余金额
    private BigDecimal historyChargeBalance;

    //历史充值赠送剩余金额
    private BigDecimal historyRewardBalance;

    //红包余额
    private BigDecimal redBalance;
}

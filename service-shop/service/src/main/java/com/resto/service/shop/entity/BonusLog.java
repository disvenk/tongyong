package com.resto.service.shop.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BonusLog implements Serializable{

    private static final long serialVersionUID = 4663694810790827782L;

    private String id;

    private String chargeOrderId;

    private String bonusSettingId;

    private Integer bonusAmount;

    private Integer state;

    private Integer shopownerBonusAmount;

    private Integer employeeBonusAmount;

    private String shopownerId;

    private String employeeId;

    private Date createTime;

    private String wishing;

    private Integer shopownerIssuingState;

    private Integer employeeIssuingState;

    private Integer amountDisbursed;

    private Integer employeeAmountDisbursed;

    private Integer shopownerAmountDisbursed;

}
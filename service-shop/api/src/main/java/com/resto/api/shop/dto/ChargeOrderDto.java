package com.resto.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeOrderDto implements Serializable {

    private static final long serialVersionUID = -7187711849081587121L;

    private String id;

    private BigDecimal chargeMoney;

    private BigDecimal rewardMoney;

    private Byte orderState;

    private Date createTime;

    private Date finishTime;

    private String customerId;

    private String shopDetailId;

    private String brandId;

    private BigDecimal chargeBalance;

    private BigDecimal rewardBalance;

    private BigDecimal totalBalance;

    private Integer numberDayNow;

    private BigDecimal arrivalAmount;

    private BigDecimal endAmount;

    private Integer type;
     //充值详细
    private ChargeLogDto chargelog;

    private String telephone;

    private String chargeSettingId;

    public ChargeOrderDto(String id, BigDecimal chargeMoney, BigDecimal rewardMoney, Byte orderState, Date createTime,
                          String customerId, String shopDetailId, String brandId) {
		super();
		this.id = id;
		this.chargeMoney = chargeMoney;
		this.rewardMoney = rewardMoney;
		this.orderState = orderState;
		this.createTime = createTime;
		this.customerId = customerId;
		this.shopDetailId = shopDetailId;
		this.brandId = brandId;
	}

	public ChargeOrderDto() {
		super();
	}

    public void setType(Integer type) {
        if(type==null){
            type=1;
        }
        this.type = type;
    }

}
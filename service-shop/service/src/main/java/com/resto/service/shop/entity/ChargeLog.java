package com.resto.service.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeLog implements Serializable {

	private static final long serialVersionUID = 3829561885181713313L;

	private String id;

    private BigDecimal chargeMoney;
    
    private String operationPhone;
    
    private String customerPhone;
    
    private String shopDetailId;
    
    private String shopName;
    
    private Date createTime;

	private String chargeOrderId;

}

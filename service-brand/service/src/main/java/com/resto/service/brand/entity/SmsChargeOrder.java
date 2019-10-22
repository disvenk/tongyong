package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SmsChargeOrder implements Serializable {

	private static final long serialVersionUID = 1491977372531284387L;
	private String id;
	private String brandId;
	private Date createTime;
	private Date pushOrderTime;//确认时间
	private BigDecimal chargeMoney;//充值金额
	private BigDecimal smsUnitPrice;//短信单价
	private Integer number;//购买条数
	private String ticketId;//发票id
	private Integer payType;//支付方式
	private String tradeNo;//第三方交易号
	private Integer orderStatus;//订单状态，0未支付/1已支付
	private Integer status;//状态，判断是否被删除，0删除，1存在
	private String remark;//订单备注
	private String brandName;//品牌名称
	
}

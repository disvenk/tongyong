package com.resto.service.brand.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票信息
 * @author Administrator
 *
 */
@Data
public class SmsTicket implements Serializable {

	private static final long serialVersionUID = -6035198043878815999L;
	private String id;
	private Integer ticketStatus;//申请状态
	private String header;//发票抬头
	private Date createTime;//发票申请时间
	private Date pushTime;//申请完成时间
	private String proposer;//申请人
	private String content;//发票内容：明细
	private BigDecimal money;//发票金额

	
	private Integer type;//申请发票的类型
	private String taxpayerCode;//纳税人识别码
	private String registeredAddress;//注册公司的地址
	private String registeredPhone;//注册公司的电话
	private String bankName;//开户行
	private String bankAccount;//开户账号
	
	private String consigneceId;//关联收货信息
	private String expersage;//物流单号
	private String name;//收货人姓名
	private String phone;//收货人电话
	private String address;//收货人地址
	private String remark;//发票备注 
	private Integer status;//订单状态（0删除，1存在）
	
	private String brandId;

}

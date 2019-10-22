package com.resto.brand.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票信息
 * @author Administrator
 *
 */
public class SmsTicket implements Serializable {
	
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
	
	
	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getExpersage() {
		return expersage;
	}
	public void setExpersage(String expersage) {
		this.expersage = expersage;
	}
	public Integer getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(Integer ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getTaxpayerCode() {
		return taxpayerCode;
	}
	public void setTaxpayerCode(String taxpayerCode) {
		this.taxpayerCode = taxpayerCode;
	}
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	
	public String getRegisteredPhone() {
		return registeredPhone;
	}
	public void setRegisteredPhone(String registeredPhone) {
		this.registeredPhone = registeredPhone;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getConsigneceId() {
		return consigneceId;
	}
	public void setConsigneceId(String consigneceId) {
		this.consigneceId = consigneceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPushTime() {
		return pushTime;
	}
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
}

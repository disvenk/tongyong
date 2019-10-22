package com.resto.brand.web.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Contract implements Serializable {
    private Long id;

    //合同编号
    private String constractNum;

    //品牌名称
    private String brandName;

    //店铺数量
    private Integer shopNum;

    //签约时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    //签约金额
    private BigDecimal signMoney;

    //甲方公司
    private String aCompanyName;

    //甲方签约人
    private String aSigntory;

    //甲方签约人
    private String aTelephone;

    //甲方 邮箱
    @Email
    private String aEmail;

    //乙方公司名称
    private String bCompanyName;

    //乙方签约人
    private String bSigntory;

    //乙方手机号
    private String bTelephone;

    //乙方邮箱
    @Email @NotEmpty
    private String bEmail;

    //计费方式 1时间 2效果
    private Integer chargeMode;

    //已收款
    private BigDecimal receiveMoney;

    //未收款
    private BigDecimal unreceiveMoney;

    //每年收
    private BigDecimal yearMoney;

    //上线时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    //到期时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationTime;

    //有效期
    private Integer validity;

    //短信条数
    private Integer smsNum;

    //状态
    private Integer status;

    //合同附件
    private String picUrl;

    //账户余额
    private BigDecimal accountBalance;

    //已使用余额
    private BigDecimal usedBalance;

    //是否开启新用户注册
    private Byte openNewCustomerRegister;

    //每个新用户扣费
    private BigDecimal newCustomerValue;

    //是否开启短信付费
    private Byte openSendSms;

    //每条短信价格
    private BigDecimal sendSmsValue;

    //所有订单 0不开启 1.订单总额 2实付金额
    private Byte openAllOrder;

    //所有订单占比
    private Double allOrderValue;

	//回头用户订单 0不开启 1.回头用户订单总额 2实付金额
    private Byte openBackCustomerOrder;

	//回头用户订单占比
    private BigDecimal backCustomerOrderValue;

    private Byte openOutFoodOrder;

    private BigDecimal outFoodOrderValue;

    private Byte openThirdFoodOrder;

    private Double thirdFoodOrderValue;

    //创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    private Date updateTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")

    //更新人
    private String updateUser;

    //店铺名称 多个店铺用","隔开
    private String shopNames;

    //一个合同可以有多个收入
    List<Income> incomeList ;

    //剩余可开发票
    private BigDecimal invoiceMoney;


    //已开发票
    private BigDecimal hasInvoiceMoney;

    public BigDecimal getInvoiceMoney() {
        return invoiceMoney;
    }

    public void setInvoiceMoney(BigDecimal invoiceMoney) {
        this.invoiceMoney = invoiceMoney;
    }

    public BigDecimal getHasInvoiceMoney() {
        return hasInvoiceMoney;
    }

    public void setHasInvoiceMoney(BigDecimal hasInvoiceMoney) {
        this.hasInvoiceMoney = hasInvoiceMoney;
    }

    public List<Income> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<Income> incomeList) {
        this.incomeList = incomeList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConstractNum() {
        return constractNum;
    }

    public void setConstractNum(String constractNum) {
        this.constractNum = constractNum == null ? null : constractNum.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public Integer getShopNum() {
        return shopNum;
    }

    public void setShopNum(Integer shopNum) {
        this.shopNum = shopNum;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public BigDecimal getSignMoney() {
        return signMoney;
    }

    public void setSignMoney(BigDecimal signMoney) {
        this.signMoney = signMoney;
    }

    public String getaCompanyName() {
        return aCompanyName;
    }

    public void setaCompanyName(String aCompanyName) {
        this.aCompanyName = aCompanyName == null ? null : aCompanyName.trim();
    }

    public String getaSigntory() {
        return aSigntory;
    }

    public void setaSigntory(String aSigntory) {
        this.aSigntory = aSigntory == null ? null : aSigntory.trim();
    }

    public String getaTelephone() {
        return aTelephone;
    }

    public void setaTelephone(String aTelephone) {
        this.aTelephone = aTelephone == null ? null : aTelephone.trim();
    }

    public String getaEmail() {
        return aEmail;
    }

    public void setaEmail(String aEmail) {
        this.aEmail = aEmail == null ? null : aEmail.trim();
    }

    public String getbCompanyName() {
        return bCompanyName;
    }

    public void setbCompanyName(String bCompanyName) {
        this.bCompanyName = bCompanyName == null ? null : bCompanyName.trim();
    }

    public String getbSigntory() {
        return bSigntory;
    }

    public void setbSigntory(String bSigntory) {
        this.bSigntory = bSigntory == null ? null : bSigntory.trim();
    }

    public String getbTelephone() {
        return bTelephone;
    }

    public void setbTelephone(String bTelephone) {
        this.bTelephone = bTelephone == null ? null : bTelephone.trim();
    }

    public String getbEmail() {
        return bEmail;
    }

    public void setbEmail(String bEmail) {
        this.bEmail = bEmail == null ? null : bEmail.trim();
    }

    public Integer getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(Integer chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getReceiveMoney() {
        return receiveMoney;
    }

    public void setReceiveMoney(BigDecimal receiveMoney) {
        this.receiveMoney = receiveMoney;
    }

    public BigDecimal getUnreceiveMoney() {
        return unreceiveMoney;
    }

    public void setUnreceiveMoney(BigDecimal unreceiveMoney) {
        this.unreceiveMoney = unreceiveMoney;
    }

    public BigDecimal getYearMoney() {
        return yearMoney;
    }

    public void setYearMoney(BigDecimal yearMoney) {
        this.yearMoney = yearMoney;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(Integer smsNum) {
        this.smsNum = smsNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getUsedBalance() {
        return usedBalance;
    }

    public void setUsedBalance(BigDecimal usedBalance) {
        this.usedBalance = usedBalance;
    }

    public Byte getOpenNewCustomerRegister() {
        return openNewCustomerRegister;
    }

    public void setOpenNewCustomerRegister(Byte openNewCustomerRegister) {
        this.openNewCustomerRegister = openNewCustomerRegister;
    }

    public BigDecimal getNewCustomerValue() {
        return newCustomerValue;
    }

    public void setNewCustomerValue(BigDecimal newCustomerValue) {
        this.newCustomerValue = newCustomerValue;
    }

    public Byte getOpenSendSms() {
        return openSendSms;
    }

    public void setOpenSendSms(Byte openSendSms) {
        this.openSendSms = openSendSms;
    }

    public BigDecimal getSendSmsValue() {
        return sendSmsValue;
    }

    public void setSendSmsValue(BigDecimal sendSmsValue) {
        this.sendSmsValue = sendSmsValue;
    }

    public Byte getOpenAllOrder() {
        return openAllOrder;
    }

    public void setOpenAllOrder(Byte openAllOrder) {
        this.openAllOrder = openAllOrder;
    }

    public Double getAllOrderValue() {
        return allOrderValue;
    }

    public void setAllOrderValue(Double allOrderValue) {
        this.allOrderValue = allOrderValue;
    }

    public Byte getOpenBackCustomerOrder() {
        return openBackCustomerOrder;
    }

    public void setOpenBackCustomerOrder(Byte openBackCustomerOrder) {
        this.openBackCustomerOrder = openBackCustomerOrder;
    }

    public BigDecimal getBackCustomerOrderValue() {
        return backCustomerOrderValue;
    }

    public void setBackCustomerOrderValue(BigDecimal backCustomerOrderValue) {
        this.backCustomerOrderValue = backCustomerOrderValue;
    }

    public Byte getOpenOutFoodOrder() {
        return openOutFoodOrder;
    }

    public void setOpenOutFoodOrder(Byte openOutFoodOrder) {
        this.openOutFoodOrder = openOutFoodOrder;
    }

    public BigDecimal getOutFoodOrderValue() {
        return outFoodOrderValue;
    }

    public void setOutFoodOrderValue(BigDecimal outFoodOrderValue) {
        this.outFoodOrderValue = outFoodOrderValue;
    }

    public Byte getOpenThirdFoodOrder() {
        return openThirdFoodOrder;
    }

    public void setOpenThirdFoodOrder(Byte openThirdFoodOrder) {
        this.openThirdFoodOrder = openThirdFoodOrder;
    }

    public Double getThirdFoodOrderValue() {
        return thirdFoodOrderValue;
    }

    public void setThirdFoodOrderValue(Double thirdFoodOrderValue) {
        this.thirdFoodOrderValue = thirdFoodOrderValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public String getShopNames() {
        return shopNames;
    }

    public void setShopNames(String shopNames) {
        this.shopNames = shopNames == null ? null : shopNames.trim();
    }
}
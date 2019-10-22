package com.resto.shop.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by carl on 2016/10/14.
 */
public class GetNumber implements Serializable {

    private String id;

    private String shopDetailId;

    private String brandId;

    private Integer state;

    private Date createTime;

    private Date eatTime;

    private Date passNumberTime;

    private Integer personNumber;

    private String phone;

    private Integer waitNumber;

    private String tableType;

    private Integer callNumber;

    private Date callNumberTime;

    private BigDecimal flowMoney;

    private Integer countByTableTpye;

    private String imgUrl;

    private String shopName;

    private String customerId;

    private BigDecimal finalMoney;

    private BigDecimal highMoney;
    
    //当前取的号码
    private String codeValue;

    private String codeId;

    public BigDecimal getHighMoney() {
        return highMoney;
    }

    public void setHighMoney(BigDecimal highMoney) {
        this.highMoney = highMoney;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getFinalMoney() {
        return finalMoney;
    }

    public void setFinalMoney(BigDecimal finalMoney) {
        this.finalMoney = finalMoney;
    }

    public Date getCallNumberTime() {
        return callNumberTime;
    }

    public void setCallNumberTime(Date callNumberTime) {
        this.callNumberTime = callNumberTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getCountByTableTpye() {
        return countByTableTpye;
    }

    public void setCountByTableTpye(Integer countByTableTpye) {
        this.countByTableTpye = countByTableTpye;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEatTime() {
        return eatTime;
    }

    public void setEatTime(Date eatTime) {
        this.eatTime = eatTime;
    }

    public Date getPassNumberTime() {
        return passNumberTime;
    }

    public void setPassNumberTime(Date passNumberTime) {
        this.passNumberTime = passNumberTime;
    }

    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getWaitNumber() {
        return waitNumber;
    }

    public void setWaitNumber(Integer waitNumber) {
        this.waitNumber = waitNumber;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Integer getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(Integer callNumber) {
        this.callNumber = callNumber;
    }

    public BigDecimal getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(BigDecimal flowMoney) {
        this.flowMoney = flowMoney;
    }

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }
}

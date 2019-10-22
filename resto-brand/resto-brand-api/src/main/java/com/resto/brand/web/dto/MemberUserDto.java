package com.resto.brand.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MemberUserDto implements Serializable {
	private String customerId;
	private String accountId;
	private String nickname;
	private String photo;
	private String birthday;
	private String telephone;
	private String province;
	private String city;
	private String sex;
	private String remain;
	private String chargeRemain;
	private String presentRemain;
	private String redRemain;
	private String money;
	private String amount;
	private String sumMoney;
	private String isBindPhone;
	private String isCharge;
    private Map<String, Object> brandCustomerCount;
    private List<Map<String, Object>> memberUserDtos;

    public Map<String, Object> getBrandCustomerCount() {
        return brandCustomerCount;
    }

    public void setBrandCustomerCount(Map<String, Object> brandCustomerCount) {
        this.brandCustomerCount = brandCustomerCount;
    }

    public List<Map<String, Object>> getMemberUserDtos() {
        return memberUserDtos;
    }

    public void setMemberUserDtos(List<Map<String, Object>> memberUserDtos) {
        this.memberUserDtos = memberUserDtos;
    }

    public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRemain() {
		return remain;
	}
	public void setRemain(String remain) {
		this.remain = remain;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getIsBindPhone() {
		return isBindPhone;
	}
	public void setIsBindPhone(String isBindPhone) {
		this.isBindPhone = isBindPhone;
	}
	
	public String getChargeRemain() {
		return chargeRemain;
	}
	public void setChargeRemain(String chargeRemain) {
		this.chargeRemain = chargeRemain;
	}
	public String getPresentRemain() {
		return presentRemain;
	}
	public void setPresentRemain(String presentRemain) {
		this.presentRemain = presentRemain;
	}
	public String getRedRemain() {
		return redRemain;
	}
	public void setRedRemain(String redRemain) {
		this.redRemain = redRemain;
	}
	public MemberUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MemberUserDto(String customerId, String accountId, String nickname, String photo, String birthday,
			String telephone, String province, String city, String sex, String remain, String chargeRemain,
			String presentRemain, String redRemain, String money, String amount, String sumMoney, String isBindPhone,
			String isCharge) {
		super();
		this.customerId = customerId;
		this.accountId = accountId;
		this.nickname = nickname;
		this.photo = photo;
		this.birthday = birthday;
		this.telephone = telephone;
		this.province = province;
		this.city = city;
		this.sex = sex;
		this.remain = remain;
		this.chargeRemain = chargeRemain;
		this.presentRemain = presentRemain;
		this.redRemain = redRemain;
		this.money = money;
		this.amount = amount;
		this.sumMoney = sumMoney;
		this.isBindPhone = isBindPhone;
		this.isCharge = isCharge;
	}
	public String getIsCharge() {
		return isCharge;
	}
	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}
	
	
}

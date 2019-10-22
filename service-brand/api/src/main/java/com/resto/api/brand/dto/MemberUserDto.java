package com.resto.api.brand.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MemberUserDto implements Serializable {

	private static final long serialVersionUID = -7056504841113310355L;

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
	
}

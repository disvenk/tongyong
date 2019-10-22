package com.resto.wechat.web.dto;

import com.resto.shop.web.model.Order;

public class OrderDto extends Order{
	private Boolean useAccount;
	private String useCoupon;
	public Boolean getUseAccount() {
		return useAccount;
	}
	public String getUseCoupon() {
		return useCoupon;
	}
	public void setUseAccount(Boolean useAccount) {
		this.useAccount = useAccount;
	}
	public void setUseCoupon(String useCoupon) {
		this.useCoupon = useCoupon;
	}
}

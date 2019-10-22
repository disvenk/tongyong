package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandAccount;

import java.math.BigDecimal;

public interface BrandAccountService extends GenericService<BrandAccount, Integer> {

    /**
     * 根据品牌id 查询品牌账户相关信息
     * @param brandId
     * @return
     */
    BrandAccount selectByBrandId(String brandId);

    BrandAccount selectByBrandSettingId(String brandSettingId);

	/**
	 * 品牌账户充值
	 * @param brandId
	 * @param chargeMoney
	 * @return
	 */
    int chargeAccount(String brandId, BigDecimal chargeMoney);

	/**
	 * 品牌账户扣费
	 * @param jifeiMoney 可能是短信扣费 注册扣费 订单扣费 ...
	 * @param id
	 * @return
	 */
	int	updateBlance(BigDecimal jifeiMoney,Integer id);

	/**
	 * 在bo端 手动给品牌充值 品牌账户余额
	 * @param b
	 * @param addAccount
	 * @param totalAccount
	 * @param userName
	 * @param brandId
	 */
	void updateBrandAccountByManual(BrandAccount b,BigDecimal addAccount,BigDecimal totalAccount,String userName,String brandId);
}

package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.SmsAcount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface SmsAcountMapper extends BaseDao<SmsAcount,String> {
	
	/**
	 * 根据品牌ID 查询该账户的短信基本信息
	 * @param brandId
	 * @return
	 */
	SmsAcount selectByBrandId(@Param("brandId") String brandId);
	
	/**
	 * 给指定品牌充值短信数量
	 * @param brandId
	 * @param money
	 * @return
	 */
	int chargeSms(@Param("brandId") String brandId, @Param("number") int number, @Param("money") BigDecimal money);
	
	
	/**
	 * 更新短信账户短信条数
	 * @param brandId
	 */
	void updateByBrandId(@Param("brandId") String brandId);
	
	
	/**
	 * 根据品牌ID查询短信单价
	 * @param brandId
	 * @return
	 */
	BigDecimal selectSmsUnitPriceByBrandId(@Param("brandId") String brandId);
	
	/**
	 * 根据品牌ID查询此品牌剩余可开的发票金额
	 * @param brandId
	 * @return
	 */
	BigDecimal selectInvoiceMoney(@Param("brandId") String brandId);
	
	/**
	 * 根据品牌ID更新此品牌剩余可开的发票金额
	 * @param brandId
	 * @return
	 */
	int updateRemainerAmcount(@Param("brandId") String brandId, @Param("money") BigDecimal money);

}

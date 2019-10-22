package com.resto.brand.web.dao;

import com.resto.brand.web.model.BrandAccount;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

public interface BrandAccountMapper extends GenericDao<BrandAccount,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(BrandAccount record);

    int insertSelective(BrandAccount record);

    BrandAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BrandAccount record);

    int updateByPrimaryKey(BrandAccount record);

    /**
     *
     * 根据品牌id 查询品牌账户
     * @param brandId
     * @return
     */
    BrandAccount selectByBrandId(String brandId);

    BrandAccount selectByBrandSettingId(String brandSettingId);

    /**
     * 充值账户余额
     * @param brandId
     * @param chargeMoney
     * @return
     */
    int chargeAccount(@Param("brandId") String brandId, @Param("chargeMoney") BigDecimal chargeMoney,@Param("Date") Date date);

	/**
	 * 扣费
	 * @param id
	 * @param payMoney
	 * @param date
	 * @return
	 */
	int updateBlance(@Param("id") Integer id, @Param("payMoney") BigDecimal payMoney,@Param("Date") Date date);
}

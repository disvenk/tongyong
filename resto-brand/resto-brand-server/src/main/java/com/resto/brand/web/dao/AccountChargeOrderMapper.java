package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.AccountChargeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AccountChargeOrderMapper  extends GenericDao<AccountChargeOrder,String> {
    int deleteByPrimaryKey(String id);

    int insert(AccountChargeOrder record);

    int insertSelective(AccountChargeOrder record);

    AccountChargeOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AccountChargeOrder record);

    int updateByPrimaryKey(AccountChargeOrder record);

    /**
     * 查询已支付的订单
     * @return
     */
    List<AccountChargeOrder> selectHasPayList();

	/**
	 *
	 * @param beginDate
	 * @param endDate
	 * @param brandId
	 * @return
	 */
	List<AccountChargeOrder> selectListByBrandIdAndTime(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate,@Param("brandId") String brandId);

	List<AccountChargeOrder> selectHasPayListByBrandId(@Param("brandId") String brandId);
}

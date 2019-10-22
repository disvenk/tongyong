package com.resto.brand.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.SmsChargeOrder;

public interface SmsChargeOrderMapper extends GenericDao<SmsChargeOrder, String>{
    
    int insertSelective(SmsChargeOrder record);

    SmsChargeOrder selectByPrimaryKey(@Param("id") String id);

    int updateByPrimaryKeySelective(SmsChargeOrder record);
    
    /**
	 * 根据品牌ID 查询短信充值订单
	 * @param brandId
	 * @return
	 */
	List<SmsChargeOrder> selectByBrandId(@Param("brandId") String brandId);

	List<SmsChargeOrder> selectOtherPay();
	
	List<SmsChargeOrder> selectListData();
	
}
package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.SmsChargeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsChargeOrderMapper extends BaseDao<SmsChargeOrder, String> {
    
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
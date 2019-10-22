package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.SmsTicket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsTicketMapper  extends BaseDao<SmsTicket,String> {
	
	List<SmsTicket> selectByBrandId(@Param("brandId") String brandId);
	
}

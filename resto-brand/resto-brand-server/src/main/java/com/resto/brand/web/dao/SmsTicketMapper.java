package com.resto.brand.web.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.SmsTicket;

public interface SmsTicketMapper  extends GenericDao<SmsTicket,String> {
	
	List<SmsTicket> selectByBrandId(@Param("brandId")String brandId);
	
}

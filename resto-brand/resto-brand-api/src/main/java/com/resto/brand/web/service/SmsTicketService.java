package com.resto.brand.web.service;

import java.util.List;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.SmsTicket;

public interface SmsTicketService extends GenericService<SmsTicket, String> {

	List<SmsTicket> selectTicketlist();
	
	List<SmsTicket> selectByBrandId(String brandId);
	
	/**
	 * 创建申请发票订单
	 * @param smsTicket
	 * @return
	 */
	Result createInvoice(SmsTicket smsTicket);
}

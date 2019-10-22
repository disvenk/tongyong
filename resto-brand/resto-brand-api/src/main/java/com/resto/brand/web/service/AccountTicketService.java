package com.resto.brand.web.service;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.AccountTicket;

import java.util.List;

public interface AccountTicketService extends GenericService<AccountTicket, Long> {

	/**
	 * 创建申请发票订单
 	 * @param accountTicket
	 * @return
	 */
   Result createAccountTicket(AccountTicket accountTicket);

	/**
	 *
	 * @param brandId
	 * @return
	 */
	List<AccountTicket> selectListByBrandId(String brandId);
    
}

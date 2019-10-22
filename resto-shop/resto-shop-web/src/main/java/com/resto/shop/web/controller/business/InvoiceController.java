package com.resto.shop.web.controller.business;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.SmsTicket;
import com.resto.brand.web.service.SmsTicketService;
import com.resto.shop.web.controller.GenericController;

/**
 * 发票信息 Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("invoice")
public class InvoiceController extends GenericController {

	@Resource
	SmsTicketService smsTicketService;

	@RequestMapping("/list")
	public void list() {
	}

	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData() {
		List<SmsTicket> list = smsTicketService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(list);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(SmsTicket smsTicket){
		smsTicket.setId(ApplicationUtils.randomUUID());
		smsTicket.setProposer(getCurrentUserId());
		smsTicket.setBrandId(getCurrentBrandId());
		smsTicket.setCreateTime(new Date());
		return smsTicketService.createInvoice(smsTicket);
	}
}

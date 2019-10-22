package com.resto.brand.web.controller.business;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.SmsTicket;
import com.resto.brand.web.service.SmsTicketService;

@Controller
@RequestMapping("smsticket")
public class SmsTicketController extends GenericController{

	@Resource
	SmsTicketService smsTicketService;
	
	
	@RequestMapping("/list")
	public void list(){
	}

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SmsTicket> listData(){
		return smsTicketService.selectTicketlist();
	}
	
	
	@RequestMapping("modify")
	@ResponseBody
	public Result update(SmsTicket smsTicket){
		smsTicket.setPushTime(new Date());
		smsTicket.setTicketStatus(1);
		smsTicketService.update(smsTicket);
		return getSuccessResult(null);
	}
	
}

package com.resto.brand.web.controller.business;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.AccountTicket;
import com.resto.brand.web.service.AccountTicketService;

@Controller
@RequestMapping("accountticket")
public class AccountTicketController extends GenericController{

	@Resource
	AccountTicketService accountticketService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<AccountTicket> listData(){
		return accountticketService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		AccountTicket accountticket = accountticketService.selectById(id);
		return getSuccessResult(accountticket);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid AccountTicket brand){
		accountticketService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid AccountTicket accountTicket){

		accountTicket.setPushTime(new Date());
		accountTicket.setTicketStatus((byte)1);
		accountticketService.update(accountTicket);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		accountticketService.delete(id);
		return Result.getSuccess();
	}





}

 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.AccountTicket;
 import com.resto.brand.web.service.AccountTicketService;
 import com.resto.brand.web.service.BrandAccountService;
 import com.resto.shop.web.controller.GenericController;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.Date;
 import java.util.List;

 @Controller
 @RequestMapping("accountticket")
 public class AccountTicketController extends GenericController {

	 @Resource
	 AccountTicketService accountticketService;


	 @Resource
	 BrandAccountService brandAccountService;

	 @RequestMapping("/list")
	 public void list(){
	 }

	 @RequestMapping("/list_all")
	 @ResponseBody
	 public Result listData(){
		 List<AccountTicket> list = accountticketService.selectListByBrandId(getCurrentBrandId());
		 return getSuccessResult(list);
	 }

	 @RequestMapping("list_one")
	 @ResponseBody
	 public Result list_one(Long id){
		 AccountTicket accountticket = accountticketService.selectById(id);
		 return getSuccessResult(accountticket);
	 }

	 @RequestMapping("create")
	 @ResponseBody
	 public Result create(@Valid AccountTicket accountTicket){
		 accountTicket.setProposer(getCurrentUserId());
		 accountTicket.setBrandId(getCurrentBrandId());
		 accountTicket.setCreateTime(new Date());
		 accountticketService.createAccountTicket(accountTicket) ;
		 return Result.getSuccess();
	 }

	 @RequestMapping("modify")
	 @ResponseBody
	 public Result modify(@Valid AccountTicket brand){
		 accountticketService.update(brand);
		 return Result.getSuccess();
	 }

	 @RequestMapping("delete")
	 @ResponseBody
	 public Result delete(Long id){
		 accountticketService.delete(id);
		 return Result.getSuccess();
	 }

	 @RequestMapping("selectInvoiceMoney")
	 @ResponseBody
	 public  Result getInvoiceMoney(){

	 	return getSuccessResult(brandAccountService.selectByBrandId(getCurrentBrandId()));

	 }

 }

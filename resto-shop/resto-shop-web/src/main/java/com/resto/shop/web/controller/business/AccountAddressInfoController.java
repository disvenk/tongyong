 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.core.util.ApplicationUtils;
 import com.resto.brand.web.model.AccountAddressInfo;
 import com.resto.brand.web.service.AccountAddressInfoService;
 import com.resto.shop.web.controller.GenericController;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

 @Controller
 @RequestMapping("accountAddressInfo")
 public class AccountAddressInfoController extends GenericController {

	 @Resource
	 AccountAddressInfoService accountaddressinfoService;

	 @RequestMapping("/list")
	 public void list(){
	 }

	 @RequestMapping("/list_all")
	 @ResponseBody
	 public Result listData(){
		 List<AccountAddressInfo> list = accountaddressinfoService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(list);
	 }

	 @RequestMapping("list_one")
	 @ResponseBody
	 public Result list_one(String id){
		 AccountAddressInfo accountaddressinfo = accountaddressinfoService.selectById(id);
		 return getSuccessResult(accountaddressinfo);
	 }

	 @RequestMapping("create")
	 @ResponseBody
	 public Result create(@Valid AccountAddressInfo brand){
	 	 brand.setBrandId(getCurrentBrandId());
	 	 brand.setId(ApplicationUtils.randomUUID());
		 accountaddressinfoService.insert(brand);
		 return Result.getSuccess();
	 }

	 @RequestMapping("modify")
	 @ResponseBody
	 public Result modify(@Valid AccountAddressInfo brand){
		 accountaddressinfoService.update(brand);
		 return Result.getSuccess();
	 }

	 @RequestMapping("delete")
	 @ResponseBody
	 public Result delete(String id){
		 accountaddressinfoService.delete(id);
		 return Result.getSuccess();
	 }
 }

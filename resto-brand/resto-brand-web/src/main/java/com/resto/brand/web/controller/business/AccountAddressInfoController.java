package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.AccountAddressInfo;
import com.resto.brand.web.service.AccountAddressInfoService;

@Controller
@RequestMapping("accountaddressinfo")
public class AccountAddressInfoController extends GenericController{

	@Resource
	AccountAddressInfoService accountaddressinfoService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<AccountAddressInfo> listData(){
		return accountaddressinfoService.selectList();
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

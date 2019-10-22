package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.AccountNotice;
import com.resto.brand.web.service.AccountNoticeService;

@Controller
@RequestMapping("accountnotice")
public class AccountNoticeController extends GenericController{

	@Resource
	AccountNoticeService accountnoticeService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<AccountNotice> listData(){
		return accountnoticeService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		AccountNotice accountnotice = accountnoticeService.selectById(id);
		return getSuccessResult(accountnotice);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid AccountNotice brand){
		accountnoticeService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid AccountNotice brand){
		accountnoticeService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		accountnoticeService.delete(id);
		return Result.getSuccess();
	}
}

 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.AccountLog;
import com.resto.shop.web.service.AccountLogService;

@Controller
@RequestMapping("accountlog")
public class AccountLogController extends GenericController{

	@Resource
	AccountLogService accountlogService;

	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<AccountLog> listData(){
		return accountlogService.selectList();
	}

	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		AccountLog accountlog = accountlogService.selectById(id);
		return getSuccessResult(accountlog);
	}

	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid AccountLog brand){
		accountlogService.insert(brand);
		return Result.getSuccess();
	}

	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid AccountLog brand){
		accountlogService.update(brand);
		return Result.getSuccess();
	}

	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		accountlogService.delete(id);
		return Result.getSuccess();
	}
}

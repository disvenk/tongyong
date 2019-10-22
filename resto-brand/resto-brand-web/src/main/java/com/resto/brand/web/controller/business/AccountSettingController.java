package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.AccountSetting;
import com.resto.brand.web.service.AccountSettingService;

@Controller
@RequestMapping("accountsetting")
public class AccountSettingController extends GenericController{

	@Resource
	AccountSettingService accountsettingService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<AccountSetting> listData(){
		return accountsettingService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		AccountSetting accountsetting = accountsettingService.selectById(id);
		return getSuccessResult(accountsetting);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid AccountSetting brand){
		accountsettingService.insertAccountSetting(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid AccountSetting brand){

		accountsettingService.updateAccountSetting(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		accountsettingService.delete(id);
		return Result.getSuccess();
	}


    @RequestMapping("getOneBySettingId")
    @ResponseBody
    public Result getOneBySettingId(String brandSettingId){
        AccountSetting accountsetting = accountsettingService.selectByBrandSettingId(brandSettingId);

        return getSuccessResult(accountsetting);
    }

}

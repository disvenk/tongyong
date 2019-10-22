package com.resto.brand.web.controller.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.BrandAccount;
import com.resto.brand.web.service.BrandAccountService;

@Controller
@RequestMapping("brandaccount")
public class BrandAccountController extends GenericController{

	@Resource
    BrandAccountService accountService;


	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<BrandAccount> listData(){
		return accountService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		BrandAccount account = accountService.selectById(id);
		return getSuccessResult(account);
	}

    @RequestMapping("getOneBySettingId")
    @ResponseBody
    public Result getAccount(String brandSettingId){
        BrandAccount account = accountService.selectByBrandSettingId(brandSettingId);
        return getSuccessResult(account);
    }

	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid BrandAccount brand){
		accountService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid BrandAccount brand){
		accountService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		accountService.delete(id);
		return Result.getSuccess();
	}

	@RequestMapping("updateAccount")
	@ResponseBody
	public Result updateAccount(Integer id , BigDecimal addAccount, String password,String userName){
		Result result = new Result();

		if(!"Vino.2017".equals(password)){
			result.setMessage("密码错误");
			result.setSuccess(false);
			return result ;
		}
		BrandAccount brandAccount = accountService.selectById(id);
		BigDecimal totalAccount = brandAccount.getAccountBalance();//充值时开始余额
		BrandAccount b = new BrandAccount();
		b.setId(id);
		b.setUpdateTime(new Date());
		b.setAccountBalance(totalAccount.add(addAccount));
		accountService.updateBrandAccountByManual(b,addAccount,totalAccount,userName,brandAccount.getBrandId());
		return Result.getSuccess();
	}
}

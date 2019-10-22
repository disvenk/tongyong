package com.resto.brand.web.controller.business;

import java.util.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.dto.brandContractNums;
import com.resto.brand.web.model.BrandUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Contract;
import com.resto.brand.web.service.ContractService;

@Controller
@RequestMapping("contract")
public class ContractController extends GenericController{

	@Resource
	ContractService contractService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Contract> listData(){
		return contractService.selectList();
	}

	@RequestMapping("list_brands")
	@ResponseBody
	public List<brandContractNums>  list_brands (){
		return contractService.selectGroupByBrandName();
	}



	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		Contract contract = contractService.selectById(id);
		return getSuccessResult(contract);
	}

	@RequestMapping("listByConstractNum")
	@ResponseBody
	public Result listByConstractNum(String constractNum){
		Contract contract = contractService.selectByConstractNum(constractNum);
		return getSuccessResult(contract);
	}



	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Contract contract){
		contract.setCreateTime(new Date());
		contract.setUpdateTime(contract.getCreateTime());
		contract.setUpdateUser("测试--");
		contract.setUnreceiveMoney(contract.getSignMoney());
		contractService.insert(contract);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify( @Valid Contract brand){
		//获取当前的用户
		BrandUser brandUser = getCurrentBrandUser();
		System.out.println(brandUser);
		contractService.update(brand);

		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		contractService.delete(id);
		return Result.getSuccess();
	}
}

package com.resto.brand.web.controller.business;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Income;
import com.resto.brand.web.service.IncomeService;

@Controller
@RequestMapping("income")
public class IncomeController extends GenericController{

	@Resource
	private IncomeService incomeService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Income> listData(){

    	return incomeService.selectIncomeAndContractList();
	}

	@RequestMapping("/listByConstractNum")
	@ResponseBody
	public BigDecimal listByConstractNums(String constractNum){

		return incomeService.selectByConstractNum(constractNum);
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		Income income = incomeService.selectById(id);
		return getSuccessResult(income);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Income income){
		incomeService.insertIncome(income);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Income brand){
		incomeService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		incomeService.delete(id);
		return Result.getSuccess();
	}
}

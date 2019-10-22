package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.shop.web.service.PosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.MealTemp;
import com.resto.shop.web.service.MealTempService;

@Controller
@RequestMapping("mealtemp")
public class MealTempController extends GenericController{

	@Resource
	MealTempService mealtempService;

	@Autowired
	PosService posService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<MealTemp> listData(){
		return mealtempService.selectList(getCurrentBrandId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		MealTemp mealtemp = mealtempService.selectById(id);
		return getSuccessResult(mealtemp);
	}
	@RequestMapping("list_one_full")
	@ResponseBody
	public Result listonefull(Integer id){
		MealTemp temp = mealtempService.selectFull(id);
		return getSuccessResult(temp);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid @RequestBody MealTemp mealTemp){
		mealTemp.setBrandId(getCurrentBrandId());
		mealtempService.create(mealTemp);
		//posService.shopMsgChange(getCurrentShopId());
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid @RequestBody MealTemp mealTemp){
		mealTemp.setBrandId(getCurrentBrandId());
		mealtempService.update(mealTemp);
		//posService.shopMsgChange(getCurrentShopId());
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		mealtempService.delete(id);
		//posService.shopMsgChange(getCurrentShopId());
		return Result.getSuccess();
	}
}

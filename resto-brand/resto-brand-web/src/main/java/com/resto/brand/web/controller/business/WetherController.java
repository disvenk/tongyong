package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Wether;
import com.resto.brand.web.service.WetherService;

@Controller
@RequestMapping("wether")
public class WetherController extends GenericController{

	@Resource
	WetherService wetherService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Wether> listData(){
		return wetherService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		Wether wether = wetherService.selectById(id);
		return getSuccessResult(wether);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Wether brand){
		wetherService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Wether brand){
		wetherService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		wetherService.delete(id);
		return Result.getSuccess();
	}
}

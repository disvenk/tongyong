package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ModuleList;
import com.resto.brand.web.service.ModuleListService;

@Controller
@RequestMapping("modulelist")
public class ModuleListController extends GenericController{

	@Resource
	ModuleListService modulelistService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ModuleList> listData(){
		return modulelistService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		ModuleList modulelist = modulelistService.selectById(id);
		return getSuccessResult(modulelist);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ModuleList brand){
		modulelistService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ModuleList brand){
		modulelistService.update(brand);
		return Result.getSuccess();
	}
	
}

package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.OtherConfig;
import com.resto.brand.web.service.OtherConfigService;

@Controller
@RequestMapping("otherconfig")
public class OtherConfigController extends GenericController{

	@Resource
	OtherConfigService otherconfigService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<OtherConfig> listData(){
		return otherconfigService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		OtherConfig otherconfig = otherconfigService.selectById(id);
		return getSuccessResult(otherconfig);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid OtherConfig otherConfig){
		otherconfigService.create(otherConfig);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid OtherConfig otherConfig){
		otherconfigService.update(otherConfig);
		return Result.getSuccess();
	}
	
	@RequestMapping("checkNameExits")
	@ResponseBody
	public Result checkOtherConfigNameExits(String otherConfigName){
		OtherConfig otherConfig = otherconfigService.selectOtherConfigName(otherConfigName);
		return getSuccessResult(otherConfig);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		otherconfigService.delete(id);
		return Result.getSuccess();
	}
}

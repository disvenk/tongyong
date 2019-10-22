package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.OtherConfigDetailed;
import com.resto.brand.web.service.OtherConfigDetailedService;

@Controller
@RequestMapping("otherconfigdetailed")
public class OtherConfigDetailedController extends GenericController{

	@Resource
	OtherConfigDetailedService otherconfigdetailedService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<OtherConfigDetailed> listData(){
		return otherconfigdetailedService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		OtherConfigDetailed otherconfigdetailed = otherconfigdetailedService.selectById(id);
		return getSuccessResult(otherconfigdetailed);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid OtherConfigDetailed brand){
		otherconfigdetailedService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid OtherConfigDetailed brand){
		otherconfigdetailedService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		otherconfigdetailedService.delete(id);
		return Result.getSuccess();
	}
}

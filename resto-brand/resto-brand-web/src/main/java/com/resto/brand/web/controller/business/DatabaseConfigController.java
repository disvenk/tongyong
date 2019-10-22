package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.service.DatabaseConfigService;

@Controller
@RequestMapping("databaseconfig")
public class DatabaseConfigController extends GenericController{

	@Resource
	DatabaseConfigService databaseconfigService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<DatabaseConfig> listData(){
		List<DatabaseConfig> databaseConfigs = databaseconfigService.selectList();
		for(int i = 0; i < databaseConfigs.size(); i++){
			DatabaseConfig databaseconfig = databaseConfigs.get(i);
			databaseconfig.setUsername("就不告诉你");
			databaseconfig.setPassword("就不告诉你");
		}
		return databaseConfigs;
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		DatabaseConfig databaseconfig = databaseconfigService.selectById(id);
		databaseconfig.setUsername("就不告诉你");
		databaseconfig.setPassword("就不告诉你");
		return getSuccessResult(databaseconfig);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid DatabaseConfig brand){
		databaseconfigService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid DatabaseConfig brand){
		databaseconfigService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		databaseconfigService.delete(id);
		return Result.getSuccess();
	}
}

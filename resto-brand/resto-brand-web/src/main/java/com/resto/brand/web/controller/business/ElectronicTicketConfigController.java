 package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.core.entity.JSONResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ElectronicTicketConfig;
import com.resto.brand.web.service.ElectronicTicketConfigService;

@Controller
@RequestMapping("electronicticketconfig")
public class ElectronicTicketConfigController extends GenericController{

	@Resource
	ElectronicTicketConfigService electronicticketconfigService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ElectronicTicketConfig> listData(){
		return electronicticketconfigService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		ElectronicTicketConfig electronicticketconfig = electronicticketconfigService.selectById(id);
		return getSuccessResult(electronicticketconfig);
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ElectronicTicketConfig brand){
		if(brand.getId()==null){
			electronicticketconfigService.insert(brand);
		}else {
			electronicticketconfigService.update(brand);
		}

		return Result.getSuccess();
	}

	@RequestMapping("selectByBrandId")
	@ResponseBody
	public Result select(String brandId){
		ElectronicTicketConfig electronicTicketConfig = electronicticketconfigService.selectByBrandId(brandId);
		return new JSONResult(electronicTicketConfig,"成功",true);
	}
}

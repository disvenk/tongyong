package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.DistributionMode;
import com.resto.brand.web.service.DistributionModeService;

@Controller
@RequestMapping("distributionmode")
public class DistributionModeController extends GenericController{

	@Resource
	DistributionModeService distributionmodeService;
	
	@RequestMapping("/list")
        public void list(){
        }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<DistributionMode> listData(){
		return distributionmodeService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		DistributionMode distributionmode = distributionmodeService.selectById(id);
		return getSuccessResult(distributionmode);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid DistributionMode brand){
		distributionmodeService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid DistributionMode brand){
		distributionmodeService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		distributionmodeService.delete(id);
		return Result.getSuccess();
	}
}

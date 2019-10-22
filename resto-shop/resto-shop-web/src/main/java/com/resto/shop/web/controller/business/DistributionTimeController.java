package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.DistributionTime;
import com.resto.shop.web.service.DistributionTimeService;

@Controller
@RequestMapping("distributiontime")
public class DistributionTimeController extends GenericController{

	@Resource
	DistributionTimeService distributiontimeService;
	
	@RequestMapping("/list")
        public void list(){
        }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<DistributionTime> listData(){
		return distributiontimeService.selectListByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		DistributionTime distributiontime = distributiontimeService.selectById(id);
		return getSuccessResult(distributiontime);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid DistributionTime brand){
	        brand.setShopDetailId(getCurrentShopId());
		distributiontimeService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid DistributionTime brand){
		distributiontimeService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		distributiontimeService.delete(id);
		return Result.getSuccess();
	}
}

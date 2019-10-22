package com.resto.shop.web.controller.business;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ModuleList;
import com.resto.brand.web.model.RewardSetting;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.service.ModuleListService;
import com.resto.brand.web.service.RewardSettingService;
import com.resto.brand.web.service.ShareSettingService;
import com.resto.shop.web.controller.GenericController;

@Controller
@RequestMapping("modulelist")
public class ModuleListController extends GenericController{

	@Resource
	ModuleListService modulelistService;
	
	@Resource
	ShareSettingService shareSettingService;
	
	@Resource
	RewardSettingService rewardSettingService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
    /**
     * 2016-11-3
     */
	public List<ModuleList> listData(){
		List<ModuleList> list =  modulelistService.selectListWithHas(getCurrentBrandId());
        //隐藏没有开发完的功能
        List<ModuleList> list2 = new ArrayList<>();
        for(ModuleList moduleList : list){
            if(moduleList.getIsOpen()){
                list2.add(moduleList);
            }
        }

        return list2;
	}
	
	@RequestMapping("edit/{sign}")
	public String editSign(@PathVariable("sign")String sign){
		if(modulelistService.hasModule(sign,getCurrentBrandId())){
			return "modulelist/"+sign;
		}else{
			return "modulelist/module_not_open";
		}
	}
	
	
	@RequestMapping("data_share")
	@ResponseBody
	public Result datashare(){
		ShareSetting setting = shareSettingService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(setting);
	}
	
	@RequestMapping("edit_share")
	@ResponseBody
	public Result editshare(ShareSetting setting){
		setting.setBrandId(getCurrentBrandId());
		shareSettingService.updateByBrandId(setting);
		return getSuccessResult();
	}
	
	@RequestMapping("data_reward")
	@ResponseBody
	public Result data_reward(){
		RewardSetting setting = rewardSettingService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(setting);
	}
	
	@RequestMapping("edit_reward")
	@ResponseBody
	public Result data_reward(RewardSetting setting){
		setting.setBrandId(getCurrentBrandId());
		rewardSettingService.updateSetting(setting);
		return getSuccessResult();
	}
}

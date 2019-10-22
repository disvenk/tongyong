package com.resto.shop.web.controller.business;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.SupportTime;
import com.resto.shop.web.service.SupportTimeService;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("supporttime")
public class SupportTimeController extends GenericController{


	@Resource
	SupportTimeService supporttimeService;

	@Autowired
	PosService posService;

	@Autowired
	private BrandSettingService brandSettingService;

	@RequestMapping("/list")
           public void list(){
	}

	@RequestMapping("/brand_list")
	public ModelAndView brand_list(){
		BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
		if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){
			return new ModelAndView("supporttime/brand_list");
		}else{
			return new ModelAndView("supporttime/none");
		}
	}


    @RequestMapping("/shopList")
    public void shopList(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SupportTime> listData(){
	        String shopDetailId=getCurrentShopId();
		return supporttimeService.selectList(shopDetailId);
	}

    @RequestMapping("/shop/list_all")
    @ResponseBody
    public List<SupportTime> shopListData(){
        List<SupportTime> supportTimeList = supporttimeService.getSupportTimePackage();
        return supportTimeList;
    }

	@RequestMapping("/brandList_all")
	@ResponseBody
	public List<SupportTime> brandListData(){
		return supporttimeService.selectBrandList(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		SupportTime supporttime = supporttimeService.selectById(id);
		return getSuccessResult(supporttime);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid SupportTime brand){
		if(brand.getOpenArticleLibrary()!=null&&brand.getOpenArticleLibrary()==true){
			brand.setShopDetailId("-1");
		}else {
			brand.setShopDetailId(getCurrentShopId());
		}
		int count=0;
		for(int i=0;i<brand.getBeginTime().length();i++){
			if(brand.getBeginTime().charAt(i)==':'){
				count++;
			}
		}
		if(count == 1){
			brand.setBeginTime(brand.getBeginTime()+":00");
		}
		int number=0;
		for(int j=0;j<brand.getEndTime().length();j++){
			if(brand.getEndTime().charAt(j)==':'){
				number++;
			}
		}
		if(number == 1){
			brand.setEndTime(brand.getEndTime()+":59");
		}
		Integer id = supporttimeService.insertSupportTime(brand);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBSUPPORTTIME);
		shopMsgChangeDto.setType("add");
		shopMsgChangeDto.setId(id.toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid SupportTime brand){
		int count=0;
		for(int i=0;i<brand.getBeginTime().length();i++){
			if(brand.getBeginTime().charAt(i)==':'){
				count++;
			}
		}
		if(count == 1){
			brand.setBeginTime(brand.getBeginTime()+":00");
		}
		int number=0;
		for(int j=0;j<brand.getEndTime().length();j++){
			if(brand.getEndTime().charAt(j)==':'){
				number++;
			}
		}
		if(number == 1){
			brand.setEndTime(brand.getEndTime()+":59");
		}
		supporttimeService.update(brand);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBSUPPORTTIME);
		shopMsgChangeDto.setType("modify");
		shopMsgChangeDto.setId(brand.getId().toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		supporttimeService.delete(id);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBSUPPORTTIME);
		shopMsgChangeDto.setType("delete");
		shopMsgChangeDto.setId(id.toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Result.getSuccess();
	}
}

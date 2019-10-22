 package com.resto.shop.web.controller.business;


import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.RedisService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.BrandSettingService;

@Controller
@RequestMapping("brandSetting")
public class BrandSettingController extends GenericController{

	@Resource
	BrandSettingService brandSettingService;
	
	@Resource
	BrandService brandService;

	@Resource
	ShopDetailService shopDetailService;

	@Autowired
	private PosService posService;
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(){
		Brand brand = brandService.selectById(getCurrentBrandId());
		String brandSettingId = brand.getBrandSettingId();
		BrandSetting brandSetting = brandSettingService.selectById(brandSettingId);
		return getSuccessResult(brandSetting);
	}


	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid BrandSetting brandSetting){
		if(brandSetting.getSmsPushGiftCoupons() == null){
			brandSetting.setSmsPushGiftCoupons(0);
		}
		if(brandSetting.getWechatPushGiftCoupons() == null){
			brandSetting.setWechatPushGiftCoupons(0);
		}
		brandSettingService.update(brandSetting);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.BRANDSETTING);
		shopMsgChangeDto.setType("modify");
		shopMsgChangeDto.setId(brandSetting.getId());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		/*if(redisService.get(getCurrentBrandId()+"setting") != null){
			redisService.remove(getCurrentBrandId()+"setting");
		}*/
		Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
		redisService.clean(brand.getId(), brand.getBrandSign());
		ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
		LogTemplateUtils.brandSettingEdit(brand.getBrandName(), shopDetail.getName(), getUserName());

		return Result.getSuccess();
	}
	
}

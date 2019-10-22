package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.RedisService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("shopDetail")
public class ShopDetailController extends GenericController{

	@Resource
	ShopDetailService shopDetailService;

	@Resource
	BrandService brandService;

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
		ShopDetail  shopDetail = shopDetailService.selectById(getCurrentShopId());
		return getSuccessResult(shopDetail);
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(ShopDetail shopDetail){
	    shopDetail.setId(getCurrentShopId());
	    log.info(shopDetail.getIsOpenTablewareFee() + "22222");
	    if(shopDetail.getIsOpenSauceFee() == null){
	    	shopDetail.setIsOpenSauceFee(0);
		}
		if(shopDetail.getIsOpenTablewareFee() == null){
	    	shopDetail.setIsOpenTablewareFee(0);
		}
		if(shopDetail.getIsOpenTowelFee() == null){
			shopDetail.setIsOpenTowelFee(0);
		}
	    shopDetailService.update(shopDetail);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.SHOPDETAIL);
		shopMsgChangeDto.setType("modify");
		shopMsgChangeDto.setId(shopDetail.getId());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
//	    if(RedisUtil.get(shopDetail.getId()+"info") != null){
//			RedisUtil.remove(shopDetail.getId()+"info");
//		}
	    if(redisService.get(shopDetail.getId()+"info") != null){
			redisService.remove(shopDetail.getId()+"info");
		}
		Brand brand = brandService.selectByPrimaryKey(getCurrentBrandId());
		shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
		LogTemplateUtils.shopDeatilEdit(brand.getBrandName(), shopDetail.getName(), getUserName());

	    return Result.getSuccess();
	}
	
	@RequestMapping("list_all")
	@ResponseBody
	public Result listAll(){
	    List<ShopDetail> lists = shopDetailService.selectByBrandId(getCurrentBrandId());
	    return getSuccessResult(lists);
	}


	@RequestMapping("list_without_self")
	@ResponseBody
	public Result listWithoutSelf(){
		List<ShopDetail> lists = shopDetailService.selectByBrandId(getCurrentBrandId());
		for(int i = 0;i < lists.size();i++){
			if(lists.get(i).getId().equals(getCurrentShopId())){
				lists.remove(i);
			}
		}
		return getSuccessResult(lists);
	}
	
}

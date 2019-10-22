package com.resto.shop.web.controller.business;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;

@Controller
@RequestMapping("shopDetailAlipay")
public class ShopDetailAlipayController extends GenericController{

	@Resource
    ShopDetailService shopDetailService;
	
	@RequestMapping("/list")
    public void list(){
    }
	
	@RequestMapping("list_one")
    @ResponseBody
    public Result list_one(){
        ShopDetail shopDetail = shopDetailService.selectById(getCurrentShopId());
        return getSuccessResult(shopDetail);
    }
	
	@RequestMapping("modify")
    @ResponseBody
    public Result modify(ShopDetail shopDetail){
        shopDetail.setId(getCurrentShopId());
        shopDetailService.update(shopDetail);
        return Result.getSuccess();
    }
}

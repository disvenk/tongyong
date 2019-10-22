 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.Coupon;
import com.resto.shop.web.service.CouponService;

@Controller
@RequestMapping("coupon")
public class CouponController extends GenericController{

	@Resource
	CouponService couponService;


	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Coupon> listData(){
		return couponService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		Coupon coupon = couponService.selectById(id);
		return getSuccessResult(coupon);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Coupon brand){
		couponService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Coupon brand){
		couponService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		couponService.delete(id);
		return Result.getSuccess();
	}


}

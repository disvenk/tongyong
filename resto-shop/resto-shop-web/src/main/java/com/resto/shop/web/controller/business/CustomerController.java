 package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.service.CustomerService;

@Controller
@RequestMapping("customer")
public class CustomerController extends GenericController{

	@Resource
	CustomerService customerService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Customer> listData(){
		return customerService.selectListByBrandId(getCurrentBrandId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		Customer customer = customerService.selectById(id);
		return getSuccessResult(customer);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Customer brand){
		customerService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Customer brand){
		customerService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		customerService.delete(id);
		return Result.getSuccess();
	}

//    //测试获取优惠券
//    @RequestMapping("test/coupon")
//    public void testCoupon(String phone,String customerId,Integer couponType) throws AppException {
//        customerService.bindPhone(phone, customerId, couponType,getCurrentShopId());
//    }


}

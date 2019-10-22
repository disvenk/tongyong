package com.resto.shop.web.controller.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.NewEmployee;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.NewEmployeeService;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;

@Controller
@RequestMapping("newEmployee")
public class NewEmployeeController extends GenericController{

	@Resource
    NewEmployeeService newEmployeeService;

    @Resource
    CustomerService customerService;

    @Resource
    BrandSettingService brandSettingService;

	@RequestMapping("/list")
    public String list(){
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if (brandSetting.getOpenBonus().equals(Common.YES)){
            return "newEmployee/list";
        }else {
            return "notopen";
        }
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
	    try{
            Map<String, Object> map = new HashMap<>();
            List<NewEmployee> newEmployees = newEmployeeService.selectByBrandId(getCurrentBrandId());
            List<ShopDetail> shopDetails = getCurrentShopDetails();
            for (NewEmployee newEmployee : newEmployees){
                for (ShopDetail shopDetail : shopDetails){
                    if (newEmployee.getShopDetailId().equalsIgnoreCase(shopDetail.getId())){
                        newEmployee.setShopName(shopDetail.getName());
                        break;
                    }
                }
                if (newEmployee.getRoleType().equals(1)){
                    newEmployee.setRoleValue("员工");
                }else{
                    newEmployee.setRoleValue("店长");
                }
                if (newEmployee.getSex().equals(1)){
                    newEmployee.setSexValue("男");
                }else{
                    newEmployee.setSexValue("女");
                }
                if (newEmployee.getState().equals(1)){
                    newEmployee.setStateValue("启用");
                }else {
                    newEmployee.setStateValue("未启用");
                }
            }
            map.put("newEmployees",newEmployees);
            map.put("shopDetails",shopDetails);
            return getSuccessResult(map);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查看所有员工出错！");
            return new Result(false);
        }
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid NewEmployee newemployee){
	    try{
            Customer customer = customerService.selectByTelePhone(newemployee.getTelephone());
	        if (customer == null){
	            return new Result("该用户未在系统中注册，请确定该手机号用户注册后添加",false);
            }
	        newemployee.setId(ApplicationUtils.randomUUID());
            newemployee.setBrandId(getCurrentBrandId());
            newemployee.setCreateTime(new Date());
            newemployee.setNickName(customer.getNickname());
            newemployee.setWechatAvatar(customer.getHeadPhoto());
            newEmployeeService.insert(newemployee);
            return getSuccessResult();
        }catch (Exception e){
            e.printStackTrace();
            log.error("添加员工出错！");
            return new Result(false);
        }
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid NewEmployee newemployee){
	    try{
            newEmployeeService.update(newemployee);
            return getSuccessResult();
        }catch (Exception e){
            e.printStackTrace();
            log.error("编辑员工出错！");
            return new Result(false);
        }
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
	    try{
            newEmployeeService.delete(id);
            return getSuccessResult();
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除员工出错！");
            return new Result(false);
        }
	}
}

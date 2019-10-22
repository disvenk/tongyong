package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.ChargePayment;
import com.resto.shop.web.service.ChargePaymentService;

@Controller
@RequestMapping("wechatCharge")
public class WechatChargeContoller extends GenericController{

	@Resource
	ChargePaymentService chargepaymentService;
	
	@Resource
	BrandService brandService;
	
	@Resource
	ShopDetailService shopDetailService;
	
	
	@RequestMapping("/list")
    public String list(){
		return "wechatCharge/list";
    }

//	@RequestMapping("/list_all")
//	@ResponseBody
//	public List<ChargePayment> listDataByTime(@RequestParam("begin")String begin,@RequestParam("end")String end){
//		List<ChargePayment> list = chargepaymentService.selectPayListByTime(begin,end);
//		
//		return null;
//	}
	
	@RequestMapping("/list_all")
	@ResponseBody
	public List<ChargePayment> listDataByTime(@RequestParam("beginDate")String beginDate,@RequestParam("endDate")String endDate){
	    //后台判断传过来的数据是否是正常的
	    if(beginDate==null||"".equals(beginDate)){
	        log.info("开始时间不能为空");
	        return null ;
	    }
	    if(endDate==null||"".equals(endDate)){
	        log.info("结束时间不能为空");
	        return null ;
	    }
	    
		List<ChargePayment> list = chargepaymentService.selectPayList(beginDate,endDate);
		//查询品牌的名称
		Brand brand = brandService.selectById(getCurrentBrandId());
		//查询所有店铺的名字
		List<ShopDetail> lists = shopDetailService.selectByBrandId(getCurrentBrandId());
		
		for (ChargePayment chargePayment : list) {
			chargePayment.setBrandName(brand.getBrandName());
			//根据订单中存的店铺的id查询店铺的名字
			for(ShopDetail shop : lists){
			    if(chargePayment.getShopDetailId().equals(shop.getId())){
			        chargePayment.setShopDetailName(shop.getName());
			        break;
			    }
			}
		}
		
		//返回json格式数据
		return list;
	}
	
}

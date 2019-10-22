package com.resto.brand.web.controller.business;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.SmsChargeOrder;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.SmsChargeOrderService;

@Controller
@RequestMapping("smscharge")
public class SmsChargeController extends GenericController{

	@Resource
	SmsChargeOrderService smsChargeOrderService;
	
	@Resource
	BrandService brandService;
	
	@RequestMapping("/list")
	public void list(){
	}

	@RequestMapping("/list_otherpay")
	@ResponseBody
	//查询订单类型为银行卡支付的类型的订单
	public List<SmsChargeOrder> listData(){
		return  smsChargeOrderService.selectOtherPay();
	}
	
	@RequestMapping("/list_all")
	@ResponseBody
	//查询所有存在的订单
	public List<SmsChargeOrder> listAll(){
		return  smsChargeOrderService.selectListData();
	}
	
	/**
	 * 银行卡支付订单确认,更新订单状态,短信账户,和可以开发票金额等...
	 * @return
	 */
	@RequestMapping("/pay_success")
	@ResponseBody
	public Result paySuccessByBrandCard(@RequestParam("id")String id,@RequestParam("chargeMoney")BigDecimal chargeMoney){
		Result r = new Result(false);
		
		//判断钱为正数
		if(chargeMoney.compareTo(BigDecimal.ZERO)==-1){
			r.setMessage("充值的钱数目小于0");
			return r;
		}
		//查询当时生成订单时的短信单价
		SmsChargeOrder smsChargeOrder = smsChargeOrderService.selectById(id);
		//设置充值金额
		smsChargeOrder.setChargeMoney(chargeMoney);
		//根据充值金额和生成订单的单价算出短信数量    ---   四舍五入---> 计算应该充值的短信数量
		smsChargeOrder.setNumber(chargeMoney.divide(smsChargeOrder.getSmsUnitPrice(), 0, BigDecimal.ROUND_HALF_UP).intValue());
		
		smsChargeOrderService.paySuccess(smsChargeOrder,getCurrentBrandId());
		return Result.getSuccess();
	}
	
}

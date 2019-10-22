 package com.resto.shop.web.controller.business;

 import com.alibaba.fastjson.JSONObject;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.core.enums.DetailType;
 import com.resto.brand.web.model.*;
 import com.resto.brand.web.service.*;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.BrandAccountManager;
 import com.resto.shop.web.service.CustomerService;
 import com.resto.shop.web.service.SmsLogService;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import java.math.BigDecimal;
 import java.util.List;

 @Controller
 @RequestMapping("brandaccount")
 public class BrandAccountController extends GenericController {

	 @Resource
	 BrandAccountService brandAccountService;

	 @Resource
	 BrandAccountLogService brandAccountLogService;

	 @Resource
	 AccountChargeOrderService accountChargeOrderService;

	 @Resource
	 AccountSettingService accountSettingService;

	 @Resource
	 BrandSettingService brandSettingService;

	 @RequestMapping("/list")
	 public void list(){
	 }


	 @RequestMapping("initData")
	 @ResponseBody
	 public Result initData(String beginDate,String endDate){
		 BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
		 AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
		 System.err.println(JSONObject.toJSONString(accountSetting));
		 BrandAccountManager b = new BrandAccountManager();
		 b.setBrandId(getCurrentBrandId());
		 b.setShopId(getCurrentShopId());
		 BrandAccount brandAccount = brandAccountService.selectByBrandId(getCurrentBrandId());
		 b.setAccountBalance(brandAccount.getAccountBalance());//账户余额
		 b.setBrandAccountId(brandAccount.getId());
		 List<BrandAccountLog> brandAccountLogList = brandAccountLogService.selectListByBrandIdAndTime(beginDate,endDate,getCurrentBrandId());
		 int registerCustomerNum = 0; //注册用户个数
		 BigDecimal registerCustomerMoney = BigDecimal.ZERO;//注册用户支出

		 int smsNum = 0;
		 BigDecimal smsMoney = BigDecimal.ZERO;

		 int orderNum = 0 ; //订单数
		 BigDecimal orderPayMoney = BigDecimal.ZERO;//订单支出
		 BigDecimal orderMoney = BigDecimal.ZERO;//订单总额


		 if(!brandAccountLogList.isEmpty()){
		 	for(BrandAccountLog blog: brandAccountLogList){
				if(blog.getDetail()== DetailType.NEW_CUSTOMER_REGISTER){//新用户注册
					registerCustomerNum++;
					registerCustomerMoney = registerCustomerMoney.add(blog.getFoundChange());
				}

				if(blog.getDetail() == DetailType.SMS_CODE||blog.getDetail()==DetailType.SMS_DAY_MESSAGE){//发短信
					smsNum++;
					smsMoney = smsMoney.add(blog.getFoundChange());
				}

				orderMoney = orderMoney.add(blog.getOrderMoney()==null?BigDecimal.ZERO:blog.getOrderMoney());

				//订单
				if(blog.getDetail() == DetailType.ORDER_SELL || blog.getDetail() == DetailType.ORDER_REAL_SELL||
				   blog.getDetail() == DetailType.BACK_CUSTOMER_ORDER_SELL || blog.getDetail() == DetailType.BACK_CUSTOMER_ORDER_REAL_SELL) {
					orderPayMoney = orderPayMoney.add(blog.getFoundChange());
					//订单数
					if (blog.getIsParent()) {
						orderNum++;
					}
				}
			}
		 }

		 //账户充值
		 BigDecimal chargeMoney = BigDecimal.ZERO;
		 List<AccountChargeOrder> accountChargeOrders = accountChargeOrderService.selectListByBrandIdAndTime(beginDate,endDate,getCurrentBrandId());
		 if(!accountChargeOrders.isEmpty())	{
		 	for(AccountChargeOrder o:accountChargeOrders){
				chargeMoney = chargeMoney.add(o.getChargeMoney());
			}
		 }

		 b.setBrandAccountCharge(chargeMoney);

		 b.setRegisterCustoemrNum(registerCustomerNum);
		 b.setRegisterCustomerMoney(registerCustomerMoney);

		 b.setSmsNum(smsNum);
		 b.setSmsMoney(smsMoney);

		 b.setOrderNum(orderNum);
		 b.setOrderMoney(orderMoney);
		 b.setOrderOutMoney(orderPayMoney);
	 	return getSuccessResult(b);
	 }

	 @RequestMapping("getAccountData")
	 @ResponseBody
	 public Result getAccountData(String beginDate,String endDate){
		 BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
		 AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
		 System.err.println(JSONObject.toJSONString(accountSetting));
		 BrandAccountManager b = new BrandAccountManager();
		 BrandAccount brandAccount = brandAccountService.selectByBrandId(getCurrentBrandId());
		 b.setAccountBalance(brandAccount.getAccountBalance());//账户余额
		 b.setBrandAccountId(brandAccount.getId());
		 List<BrandAccountLog> brandAccountLogList = brandAccountLogService.selectListByBrandIdAndTime(beginDate,endDate,getCurrentBrandId());
		 BigDecimal registerCustomerMoney = BigDecimal.ZERO;//注册用户支出
		 BigDecimal smsMoney = BigDecimal.ZERO;//短信支出
		 BigDecimal orderPayMoney = BigDecimal.ZERO;//订单支出

		 if(!brandAccountLogList.isEmpty()){
			 for(BrandAccountLog blog: brandAccountLogList){
				 if(blog.getDetail()== DetailType.NEW_CUSTOMER_REGISTER){//新用户注册
					 registerCustomerMoney = registerCustomerMoney.add(blog.getFoundChange());
				 }
				 if(blog.getDetail() == DetailType.SMS_CODE||blog.getDetail()==DetailType.SMS_DAY_MESSAGE){//发短信
					 smsMoney = smsMoney.add(blog.getFoundChange());
				 }

				 //订单
				 if(blog.getDetail() == DetailType.ORDER_SELL || blog.getDetail() == DetailType.ORDER_REAL_SELL||
						 blog.getDetail() == DetailType.BACK_CUSTOMER_ORDER_SELL || blog.getDetail() == DetailType.BACK_CUSTOMER_ORDER_REAL_SELL) {
					 orderPayMoney = orderPayMoney.add(blog.getFoundChange());
				 }
			 }
		 }

		 //账户充值
		 BigDecimal chargeMoney = BigDecimal.ZERO;
		 List<AccountChargeOrder> accountChargeOrders = accountChargeOrderService.selectListByBrandIdAndTime(beginDate,endDate,getCurrentBrandId());
		 if(!accountChargeOrders.isEmpty())	{
			 for(AccountChargeOrder o:accountChargeOrders){
				 chargeMoney = chargeMoney.add(o.getChargeMoney());
			 }
		 }
		 b.setBrandAccountCharge(chargeMoney);
		 b.setRegisterCustomerMoney(registerCustomerMoney);
		 b.setSmsMoney(smsMoney);
		 b.setOrderOutMoney(orderPayMoney);
		 return getSuccessResult(b);
	 }

 }

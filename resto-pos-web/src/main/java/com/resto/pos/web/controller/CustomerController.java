package com.resto.pos.web.controller;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.WeChatService;
import com.resto.shop.web.constant.SmsLogType;
import com.resto.shop.web.model.ChargeSetting;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.service.AccountService;
import com.resto.shop.web.service.ChargeSettingService;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.SmsLogService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("customer")
public class CustomerController extends GenericController{

	@Resource
	private CustomerService customerService;
	
	@Resource
	private AccountService accountService;
	
	@Resource
	private SmsLogService smsLogService;
	
	@Resource
	private ChargeSettingService chargeSettingService;

	@Resource
	private BrandService brandService;

	@Autowired
	WeChatService weChatService;
	
	@Value("#{configProperties['orderMsg']}")
	public static String orderMsg;

	@RequestMapping("/queryCustomer")
	@ResponseBody
	public Result selectCustomerAccount(String telephone){
		Customer customer = new Customer();
		try{
			customer = customerService.selectCustomerAccount(telephone);
			if(customer == null){
				log.debug("账户信息为空!");
				return new Result("该手机号没有注册!", false);
			}
		}catch (Exception e) {
			log.debug("查询账户余额出错!");
			return new Result("查询出错!",false);
		}
//		UserActionUtils.writeToFtp(LogType.POS_LOG,getCurrentBrand().getBrandName()
//				,getCurrentShop().getName(),"查询用户:"+telephone);
		return getSuccessResult(customer);
	}
	
	@RequestMapping("/sendCodeMsg")
	@ResponseBody
	public Result sendCodeMsg(String opertionPhone){
		String code = new String();
		try{
			if(StringUtils.isBlank(opertionPhone)){
				return new Result("操作手机号不能为空!", false);
			}
	        code = RandomStringUtils.randomNumeric(4);

	       // com.alibaba.fastjson.JSONObject result = smsLogService.sendCode(opertionPhone, code, getCurrentBrandId(), getCurrentShopId(), SmsLogType.AUTO_CODE,null,openBrandAccount(),getAccountSetting());
			com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
			jsonObject.put("code",code);
			jsonObject.put("product",getCurrentBrand().getBrandName());
			com.alibaba.fastjson.JSONObject result = smsLogService.sendMessage(getCurrentBrandId(),getCurrentShopId(),SmsLogType.AUTO_CODE, SMSUtils.SIGN,SMSUtils.CODE_SMS_TEMP,opertionPhone,jsonObject);
			  if(!result.getBoolean("success")){
				  return new Result("发送验证码失败!",false);
			  }
		}catch (Exception e) {
			log.debug("发送验证码出错!");
			return new Result("发送验证码出错!", false);
		}
//		UserActionUtils.writeToFtp(LogType.POS_LOG,getCurrentBrand().getBrandName()
//				,getCurrentShop().getName(),"获取验证码:"+opertionPhone);
		return getSuccessResult(code);
	}
//

//	public static void main(String[] args){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		StringBuilder content = new StringBuilder();
//		content.append("堂吃支付金额:10000").append(",")
//				.append("商户录取堂吃消费笔数:64").append(",")
//				.append("用户支付消费:62/9500").append(",")
//				.append("----------------").append(",")
//				.append("新增用户消费:12/2600").append(",")
//				.append("其中：").append("")
//				.append("自然用户消费:9/1700").append(",")
//				.append("分享用户消费:3/900").append(",")
//				.append("-----------------").append(",")
//				.append("回头用户消费:50/6900").append(",")
//				.append("其中").append(",")
//				.append("二次回头用户:20/3000").append(",")
//				.append("多次回头用户:30/3900").append(",")
//				.append("-----------------").append(",")
//				.append("用户消费占比:96.85%").append(",")
//				.append("（用户交易笔数/堂吃交易笔数）").append(",")
//				.append("新增用户比率:85.76%").append(",")
//				.append("新增消费用户/(堂吃交易笔数-回头用户交易笔数)").append(",");
//		Map param = new HashMap();
//		param.put("shopName","简厨凌空SOHO\n");
//		param.put("dateTime",sdf.format(new Date())+"\n");
//		param.put("content",content.toString());
//		System.out.println(SMSUtils.sendMessage("18621943805", new JSONObject(param).toString(), "餐加", orderMsg));
//	}


	@RequestMapping("/customerCharge")
	@ResponseBody
	public Result updateCustomerAccount(String operationPhone,String customerPhone,String chargeSettingId,String customerId,String accountId){
		ChargeSetting chargeSetting = null;
		try {
			chargeSetting = chargeSettingService.selectById(chargeSettingId);
			accountService.updateCustomerAccount(operationPhone,customerPhone,chargeSetting,customerId,accountId,getCurrentBrand(),getCurrentShop());
//			UserActionUtils.writeToFtp(LogType.POS_LOG,getCurrentBrand().getBrandName()
//					,getCurrentShop().getName(),"充值操作:操作人:"+operationPhone+",充值号码:"+customerPhone
//			+".充值"+chargeSetting.getChargeMoney()+"返"+chargeSetting.getRewardMoney());
		} catch (Exception e) {
			log.error("账户充值出错!");
			return new Result("账户充值出错!", false);
		}
		return getSuccessResult(chargeSetting);
	}

	@RequestMapping("/checkCustomerSubscribe")
	@ResponseBody
	public Result checkCustomerSubscribe(String customerId) {
		//重新刷新用户是否关注
		Customer customer = customerService.selectById(customerId);
		Brand brand = brandService.selectById(customer.getBrandId());
		String accessToken = weChatService.getAccessToken(brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
		String customInfoJson = weChatService.getUserInfoSubscribe(accessToken, customer.getWechatId());
		JSONObject cusInfo = new JSONObject(customInfoJson);
		if(cusInfo.getInt("subscribe") == 1){
			return getSuccessResult(1);
		}else{
			return getSuccessResult(0);
		}
	}
}

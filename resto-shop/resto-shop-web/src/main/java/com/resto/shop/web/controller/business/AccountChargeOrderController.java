 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.alipay.util.AlipaySubmit;
 import com.resto.brand.core.entity.Result;
 import com.resto.brand.core.enums.ChargePayType;
 import com.resto.brand.core.payUtil.PayConfigUtil;
 import com.resto.brand.core.util.WeChatPayUtils;
 import com.resto.brand.web.model.AccountChargeOrder;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.AccountChargeOrderService;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.controller.GenericController;
 import org.apache.commons.lang3.StringUtils;
 import org.dom4j.DocumentException;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.validation.Valid;
 import java.net.InetAddress;
 import java.net.UnknownHostException;
 import java.util.List;
 import java.util.Map;

 import static com.resto.brand.core.payUtil.PayConfigUtil.getWxPayHtml;

 @Controller
 @RequestMapping("accountchargeorder")
 public class AccountChargeOrderController extends GenericController {

     @Resource
     AccountChargeOrderService accountchargeorderService;

     @Resource
     ShopDetailService shopDetailService;

     @RequestMapping("/list")
     public void list(){
     }

     @RequestMapping("/list_all")
     @ResponseBody
     public List<AccountChargeOrder> listData(){
         return accountchargeorderService.selectHasPayListByBrandId(getCurrentBrandId());
     }

     @RequestMapping("list_one")
     @ResponseBody
     public Result list_one(String id){
         AccountChargeOrder accountchargeorder = accountchargeorderService.selectById(id);
         return getSuccessResult(accountchargeorder);
     }

     @RequestMapping("create")
     @ResponseBody
     public Result create(@Valid AccountChargeOrder brand){
         accountchargeorderService.insert(brand);
         return Result.getSuccess();
     }

     @RequestMapping("modify")
     @ResponseBody
     public Result modify(@Valid AccountChargeOrder brand){
         accountchargeorderService.update(brand);
         return Result.getSuccess();
     }

     @RequestMapping("delete")
     @ResponseBody
     public Result delete(String id){
         accountchargeorderService.delete(id);
         return Result.getSuccess();
     }

     @RequestMapping("charge")
     @ResponseBody
     public void accountCharge(String chargeMoney, String payType, HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, DocumentException {
         StringBuilder sb = new StringBuilder();
         sb.append(getCurrentShopId());
         sb.append(",");
         ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
         sb.append(shopDetail.getName());
         sb.append(",");
         sb.append(getCurrentBrandId());
         sb.append(",");
         sb.append(getBrandName());
         String returnHtml = PayConfigUtil.RETURNHTML;

		if(StringUtils.isNotEmpty(chargeMoney)&& StringUtils.isNotEmpty(payType)){
			AccountChargeOrder accountChargeOrder =accountchargeorderService.saveChargeOrder(getCurrentBrandId(),chargeMoney);//创建充值订单
			String out_trade_no = accountChargeOrder.getId();
			if((ChargePayType.ALI_PAY+"").equals(payType)){//如果是支付宝支付
				String show_url = "";///商品展示页面
				String notify_url = getBaseUrl()+PayConfigUtil.ACCOUNT_ALIPAY_NOTIFY_URL;
				String return_url = getBaseUrl()+PayConfigUtil.ACCOUNT_ALIPAY_RETURN_URL;
				String subject = PayConfigUtil.ACCOUNT_SUBJECT;
				Map<String, String> formParame = AlipaySubmit.createFormParame(out_trade_no, subject, chargeMoney, show_url, notify_url, return_url,sb.toString());
				returnHtml = AlipaySubmit.buildRequest(formParame, "post", "确认");
			}else if((ChargePayType.WECHAT_PAY+"").equals(payType)){
				//如果是微信支付
				String spbill_create_ip  = InetAddress.getLocalHost().getHostAddress();
				String notify_url = getBaseUrl()+PayConfigUtil.ACCOUNT_WECHAT_NOTIFY_URL;
				log.info("账户充值微信的通知路径为："+notify_url);
				String body = PayConfigUtil.BODY;
				Map<String,String> apiReqeust = WeChatPayUtils.createWxPay(out_trade_no, chargeMoney, spbill_create_ip, notify_url,body,sb.toString());//通过工具类调用微信支付请求
				if("true".equals(apiReqeust.get("success"))){
					request.getSession().setAttribute("wxPayCode", apiReqeust.get("url"));
					returnHtml = getWxPayHtml();
				}
			}
		}
        PayConfigUtil.outprint(returnHtml, response);
     }

	 /**
	  * 生成微信支付的 二维码
	  * @param response
	  * @param request
	  */
	 @RequestMapping("/createWxPayCode")
	 @ResponseBody
	 public void createWxPayCode(HttpServletResponse response,HttpServletRequest request){
		 PayConfigUtil.createWxPayCode(response,request);
	 }




 }

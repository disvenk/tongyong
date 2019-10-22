package com.resto.shop.web.controller.business;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.resto.brand.core.enums.ChargePayType;
import com.resto.brand.core.payUtil.PayConfigUtil;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.resto.brand.core.alipay.util.AlipaySubmit;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.ChargePayType;
import com.resto.brand.core.payUtil.PayConfigUtil;
import com.resto.brand.core.util.WeChatPayUtils;
import com.resto.brand.web.model.SmsChargeOrder;
import com.resto.brand.web.service.SmsAcountService;
import com.resto.brand.web.service.SmsChargeOrderService;
import com.resto.shop.web.controller.GenericController;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.payUtil.PayConfigUtil.getWxPayHtml;

@Controller
@RequestMapping("smschargeorder")
public class SmsChargeOrderController extends GenericController {
	
	@Resource
	private SmsChargeOrderService smsChargeOrderService;

	@Resource
	private SmsAcountService smsAcountService;
	

	@RequestMapping("/list")
	public void list(){
	}
	
	@RequestMapping("/list_all")
	@ResponseBody
	public Result list_all(){
		List<SmsChargeOrder> list = smsChargeOrderService.selectByBrandId(getCurrentBrandId());
		return getSuccessResult(list);
	}
	
	@RequestMapping("/smsCharge")
	public void smsCharge(String chargeMoney,String paytype,HttpServletRequest request,HttpServletResponse response) throws IOException, WriterException, DocumentException{
		String returnHtml = PayConfigUtil.RETURNHTML;
//		String errorHtml="<h1>充值金额小于100无法充值<h1>";
		if(StringUtils.isNotEmpty(chargeMoney) && StringUtils.isNotEmpty(paytype)){
			//判断下单的金额是否大于100
//			if(new BigDecimal(chargeMoney).compareTo(new BigDecimal("100"))==-1){
//				log.info("充值金额不足100，无法充值");
//				outprint(errorHtml, response);
//			}
//			else{
				SmsChargeOrder smsChargeOrder = smsChargeOrderService.saveSmsOrder(getCurrentBrandId(), chargeMoney);//创建充值订单
				String out_trade_no = smsChargeOrder.getId();
				if(paytype.equals(ChargePayType.ALI_PAY+"")){//支付宝支付
					String show_url = "";///商品展示页面
					String notify_url = getBaseUrl()+PayConfigUtil.SMS_ALIPAY_NOTIFY_URL;
					String return_url = getBaseUrl()+PayConfigUtil.SMS_ALIPAY_RETURN_URL;
					String subject = PayConfigUtil.SMS_SUBJECT;
					Map<String, String> formParame = AlipaySubmit.createFormParame(out_trade_no, subject, chargeMoney, show_url, notify_url, return_url, null);
					returnHtml = AlipaySubmit.buildRequest(formParame, "post", "确认");
				}else if(paytype.equals(ChargePayType.WECHAT_PAY+"")){//微信支付
					String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();
					String notify_url =  getBaseUrl()+"paynotify/wxpay_notify";
					log.info("微信的通知路径为："+notify_url);
					String body = "【餐加】短信充值";
					Map<String,String> apiReqeust = WeChatPayUtils.createWxPay(out_trade_no, chargeMoney, spbill_create_ip, notify_url,body);
					if("true".equals(apiReqeust.get("success"))){
						request.getSession().setAttribute("wxPayCode", apiReqeust.get("url"));
						returnHtml = getWxPayHtml();
					}
				}
			}
			PayConfigUtil.outprint(returnHtml, response);
		//}
	}
	
	@RequestMapping("/smsChargeByBank")
	@ResponseBody
	public Result smsChargeByBank(String serialNumber){
		Result result = new Result(false);
		if(StringUtils.isNotEmpty(serialNumber)){
			boolean flag = smsChargeOrderService.saveSmsOrderByBank(getCurrentBrandId(), serialNumber);
			result.setSuccess(flag);
		}else{
			result.setMessage("流水号不能为空");
		}
		return result;
	}
	
	/**
	 * 完成未支付的订单
	 * @param chargeOrderId
	 * @param request
	 * @param response
	 * @throws UnknownHostException 
	 * @throws DocumentException 
	 */
	@RequestMapping("/payAgain")
	public void payAgain(String chargeOrderId,String paytype,HttpServletRequest request,HttpServletResponse response) throws UnknownHostException, DocumentException{
		String returnHtml = PayConfigUtil.RETURNHTML;
		if(StringUtils.isNotEmpty(chargeOrderId) && StringUtils.isNotEmpty(paytype)){
			SmsChargeOrder smsChargeOrder = smsChargeOrderService.selectById(chargeOrderId);
			String chargeMoney = smsChargeOrder.getChargeMoney().toString();
			if(smsChargeOrder!=null){
				String out_trade_no = smsChargeOrder.getId();
				if(paytype.equals(ChargePayType.ALI_PAY+"")){//支付宝支付
					String show_url = "";//商品展示页面
					String notify_url = getBaseUrl()+PayConfigUtil.SMS_ALIPAY_NOTIFY_URL;
					String return_url = getBaseUrl()+PayConfigUtil.SMS_ALIPAY_RETURN_URL;
					String subject = PayConfigUtil.SMS_SUBJECT;
					Map<String, String> formParame = AlipaySubmit.createFormParame(out_trade_no, subject, chargeMoney, show_url, notify_url, return_url, null);
					returnHtml = AlipaySubmit.buildRequest(formParame, "post", "确认");
				}else if(paytype.equals(ChargePayType.WECHAT_PAY+"")){//微信支付
					String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();
					String notify_url =  getBaseUrl()+"paynotify/wxpay_notify";
					String body = "【餐加】短信充值";
					Map<String,String> apiReqeust = WeChatPayUtils.createWxPay(out_trade_no, chargeMoney, spbill_create_ip, notify_url,body);
					if("true".equals(apiReqeust.get("success"))){
						request.getSession().setAttribute("wxPayCode", apiReqeust.get("url"));
						returnHtml = getWxPayHtml();
					}
				}
			}
		}
		PayConfigUtil.outprint(returnHtml, response);
	}
	
	@RequestMapping("/selectSmsUnitPrice")
	@ResponseBody
	public Result selectSmsUnitPrice(){
		BigDecimal smsUnitPrice = smsAcountService.selectSmsUnitPriceByBrandId(getCurrentBrandId());
		return getSuccessResult(smsUnitPrice);
	}
	
	@RequestMapping("/selectInvoiceMoney")
	@ResponseBody
	public Result selectInvoiceMoney(){
		BigDecimal invoiceMoney = smsAcountService.selectInvoiceMoney(getCurrentBrandId());
		return getSuccessResult(invoiceMoney);
	}
	
	
	/**
	 * 删除短信充值订单
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteOrder")
	@ResponseBody
	public boolean deleteOrder(String id){
		int row = smsChargeOrderService.delete(id);
		return row>0?true:false;
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

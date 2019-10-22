package com.resto.shop.web.controller.business;

import com.resto.brand.core.alipay.util.AlipayNotify;
import com.resto.brand.core.util.WeChatPayUtils;
import com.resto.brand.web.service.AccountChargeOrderService;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户pay回调
 *
 */
@Controller
@RequestMapping("account_paynotify")
public class AccountPayNotifyController{
	
	@Resource
	AccountChargeOrderService accountChargeOrderService;
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("alipay_notify")
	public void alipayNotify(HttpServletRequest request,HttpServletResponse response){
		log.info("支付宝账户充值---->  异步    发来贺电");
		//获取支付宝返回的所有参数
		Map<String, String> resultMap = AlipayNotify.getNotifyParams(request, response);
		//返回值
		String returnHtml = "fail";
		if(AlipayNotify.verify(resultMap)){//验证成功
			String trade_status = resultMap.get("trade_status");//交易状态
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				log.info("支付宝充值成功返回的参数为:"+resultMap);
				boolean flag = accountChargeOrderService.checkAccountChargeOrder_AliPay(resultMap);
				returnHtml = flag?"success":"fail";	//请不要修改或删除
			}else{
				//accountChargeOrderService.saveResultParam(resultMap, "支付宝");//保存参数
			}
		}else{//验证失败
			//accountChargeOrderService.saveResultParam(resultMap, "支付宝");//保存参数
			returnHtml = "fail";
		}
		//返回
		try {
			log.info("给支付宝支付异步请求的返回的信息为："+returnHtml);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(returnHtml);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/alipay_return")
	public String alipayReturn(HttpServletRequest request,HttpServletResponse response){//支付宝返回路径
		log.info("支付宝---->  充值完成   页面跳转");
		Map<String, String> params = AlipayNotify.getNotifyParams(request, response);
		if(AlipayNotify.verify(params)){//验证成功
			String trade_status = params.get("trade_status");//交易状态
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				log.info("支付宝充值成功：orderID："+params.get("out_trade_no"));
				params.put("restoResut", "true");
			}
		}else{//验证失败
			params.put("restoResut", "false");
		}
		request.setAttribute("returnParams", params);
		return "smschargeorder/alipayReturn";
	}


	@RequestMapping("wxpay_notify")
	@ResponseBody
	public String wx_notify(HttpServletRequest request) throws IOException, DocumentException{
		//微信支付异步通知 参数 详情：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7
		log.info("(账户充值)微信---->  异步    发来贺电");
		Map<String,String> resultMap = getResultMap(request);
		Map<String,String> wxResult = new HashMap<>();
		wxResult.put("return_code", "FAIL");
		if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
			if(WeChatPayUtils.validSign(resultMap,WeChatPayUtils.RESTO_MCHKEY)){
				try{
					log.info("微信充值成功返回的参数为:"+resultMap);
					accountChargeOrderService.checAccountChargeOrder_WxPay(resultMap);
					wxResult.put("return_code", "SUCCESS");
				}catch(Exception e){
					log.info("接受微信支付请求失败:"+e.getMessage());
					e.printStackTrace();
					wxResult.put("return_msg", e.toString());
				}
			}else{
				accountChargeOrderService.saveResultParam(resultMap, "微信支付");//保存参数
				wxResult.put("return_msg", "签名失败");
			}
		}
		String wxResultXml = WeChatPayUtils.mapToXml(wxResult);
		log.info("微信支付返回成功信息 id:"+resultMap.get("transaction_id")+" 返回信息："+wxResultXml);
		return wxResultXml;
	}


	
	private Map<String, String> getResultMap(HttpServletRequest request) throws IOException, DocumentException {
		InputStream input = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = "";
		StringBuffer xmlData = new StringBuffer();
		while((line=reader.readLine())!=null){
			xmlData.append(line);
		}
		log.info("receive weixin pay nofity :"+xmlData);
		return WeChatPayUtils.xmlToMap(xmlData.toString());
	}


}

package com.resto.brand.core.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeChatPayUtils {
	public static final String WEIXIN_REFUND_URL= "https://api.mch.weixin.qq.com/secapi/pay/refund";
	public static final String WEIXIN_PAY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder"; //统一下单接口
	public static final String WEIXIN_CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	public static final String WEIXIN_SEND_RED_PACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	private static final String WEIXIN_CARD_PAY = "https://api.mch.weixin.qq.com/pay/micropay"; //微信刷卡支付接口
	private static final String WEIXIN_SELECT_ORDER = "https://api.mch.weixin.qq.com/pay/orderquery"; //微信查询订单接口
	private static final String WEIXIN_REVERSE_ORDER = "https://api.mch.weixin.qq.com/secapi/pay/reverse"; //微信刷卡支付撤销订单接口


	public static final String RESTO_MCH_ID = "1350659001";				//餐加服务商
	public static final String RESTO_SUB_MCH_ID = "1350659001";		//即收账户
	public static final String RESTO_MCHKEY = "E62ADDBD1A096932FA77DBAA75AF1731";

	public static final String RESTO_APPID = "wx36bd5b9b7d264a8c";
	public static final String RESTO_APPSECRET = "807530431fe6e19e3f2c4a7d1a149465";

	static Logger log = LoggerFactory.getLogger(WeChatPayUtils.class);

	public static  String generateSign(Map<String,String> map,String mchKey){
		List<String> keySortList = new ArrayList<String>(map.keySet());
		Collections.sort(keySortList);
		StringBuffer params =new StringBuffer();
		for(int i=0;i<keySortList.size();i++){
			String k = keySortList.get(i);
			String v = map.get(k);
			if(StringUtils.isBlank(v)||"sign".equals(k))
				continue;
			params.append(k);
			params.append("=");
			params.append(v);
			params.append("&");
		}
		params.append("key="+mchKey);
		String sign = ApplicationUtils.md5Hex(params.toString()).toUpperCase();
		return sign;
	}

	public static boolean validSign(Map<String,String> result,String mchKey){
		String resultSign = result.get("sign");
		if(resultSign==null){
			return false;
		}else{
			return resultSign.equals(generateSign(result,mchKey));
		}
	}


	/**
	 * 调用微信统一下单接口
	 * 参数列表
	 * @throws
	 * @throws DocumentException
	 *
	 */
	public static Map<String,String> createJSAPIPayRequest(String body, String out_trade_no,
														   int total_fee, String spbill_create_ip, String openid,String notify_url,
														   String appid,String mchId,String mchKey,String attach) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mchId);
		if(mchId.length()==8){
			params.put("sub_mch_id",mchId);
		}
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("body", body);
		params.put("out_trade_no",out_trade_no);
		params.put("total_fee",total_fee+"");
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", "JSAPI");
		params.put("openid", openid);
		params.put("attach", attach);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		log.info("发送构建微信支付请求参数:"+xmlData);
		String resultXml = HttpRequest.post(WEIXIN_PAY_ORDER_URL).send(xmlData).body();
		log.info("发送构建微信支付请求结果:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> jspApiParams = new HashMap<String, String>();
		if("SUCCESS".equals(resultMap.get("return_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,mchKey)){
				log.info("签名验证成功");
				jspApiParams.put("appId", resultMap.get("appid"));
				String timeStamp = System.currentTimeMillis()+"";
				timeStamp = timeStamp.length()>32?timeStamp.substring(0,32):timeStamp;
				jspApiParams.put("timeStamp", timeStamp);
				jspApiParams.put("nonceStr", RandomStringUtils.randomAlphabetic(32));
				jspApiParams.put("package", "prepay_id="+resultMap.get("prepay_id"));
				jspApiParams.put("signType", "MD5");
				String paySign = generateSign(jspApiParams,mchKey);
				jspApiParams.put("paySign", paySign);
				return jspApiParams;
			}else{
				jspApiParams.put("ERROR", "微信服务验证失败！");
			}
		}else{
			jspApiParams.put("ERROR", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));
		}
		return jspApiParams;
	}

	public static Map<String, String> xmlToMap(String resultXml) throws DocumentException {
		Map<String,String> map =new HashMap<String, String>();
		Document doc = DocumentHelper.parseText(resultXml);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> children = root.elements();
		for (Element element : children) {
			map.put(element.getName(), element.getTextTrim());
		}
		return map;
	}

	public static String mapToXml(Map<String,String> map){
		Element root = DocumentHelper.createElement("xml");
		for(String key:map.keySet()){
			Element ele = root.addElement(key);
			ele.addCDATA(map.get(key));
		}
		return root.asXML();
	}

	public static Map<String, String> refund(String out_refund_no, String transaction_id,
											 int total_fee, int refund_fee,String appid,String mchId,String mchKey,String cretPath) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mchId);
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("transaction_id", transaction_id);
		params.put("out_refund_no", out_refund_no);
		params.put("total_fee", total_fee+"");
		params.put("refund_fee", refund_fee+"");
		params.put("op_user_id", mchId);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);

		String data = mapToXml(params);
		String resultXml="";
		Map<String,String> errorResult=new HashMap<>();
		try {
			resultXml = SSLHttpUtils.sendPost(WEIXIN_REFUND_URL, data,cretPath,mchId);
			Map<String,String> resultMap = xmlToMap(resultXml);
			log.info("refund result:"+resultMap);
			if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
				if(validSign(resultMap,mchKey)){
					return resultMap;
				}else{
					resultMap.put("ERROR", "wx refund sign error");
                    resultMap.put("ERROR_MSG", "验签出错");
				}
			}else{
				String error=resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des");
				resultMap.put("ERROR",error);
				String errorMsg = StringUtils.isNotBlank(resultMap.get("err_code_des")) ? resultMap.get("err_code_des") : getRefundErrorCodeMsg(resultMap.get("err_code"));
			    resultMap.put("ERROR_MSG", errorMsg);
			}
			return resultMap;
		} catch(Exception e){
			e.printStackTrace();
			errorResult.put("ERROR", e.getMessage());
			log.error("退款请求发送失败！");
		}
		return errorResult;
	}


	private static String getRefundErrorCodeMsg(String errorCode){
	    String errorMsg = "未知错误";
	    if (StringUtils.isBlank(errorCode)){
	        return "未返回错误码，未知错误";
        }
        switch (errorCode) {
            case "SYSTEMERROR":
                errorMsg = "接口返回错误";
                break;
            case "BIZERR_NEED_RETRY":
                errorMsg = "退款业务流程错误，需要商户触发重试来解决";
                break;
            case "TRADE_OVERDUE":
                errorMsg = "订单已经超过退款期限";
                break;
            case "ERROR":
                errorMsg = "业务错误";
                break;
            case "USER_ACCOUNT_ABNORMAL":
                errorMsg = "退款请求失败";
                break;
            case "INVALID_REQ_TOO_MUCH":
                errorMsg = "无效请求过多";
                break;
            case "NOTENOUGH":
                errorMsg = "余额不足";
                break;
            case "INVALID_TRANSACTIONID":
                errorMsg = "无效transaction_id";
                break;
            case "PARAM_ERROR":
                errorMsg = "参数错误";
                break;
            case "APPID_NOT_EXIST":
                errorMsg = "APPID不存在";
                break;
            case "MCHID_NOT_EXIST":
                errorMsg = "MCHID不存在";
                break;
            case "ORDERNOTEXIST":
                errorMsg = "订单号不存在";
                break;
            case "REQUIRE_POST_METHOD":
                errorMsg = "请使用post方法";
                break;
            case "SIGNERROR":
                errorMsg = "签名错误";
                break;
            case "XML_FORMAT_ERROR":
                errorMsg = "XML格式错误";
                break;
            case "FREQUENCY_LIMITED":
                errorMsg = "频率限制";
                break;
        }
        return errorMsg;
    }

	public static Map<String, String> refundNew(String out_refund_no, String transaction_id,
												int total_fee, int refund_fee,String appid,String mchId,String sub_mch_id ,String mchKey,String cretPath) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mchId);
		params.put("sub_mch_id", sub_mch_id );
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("transaction_id", transaction_id);
//		params.put("out_trade_no",transaction_id);
		params.put("out_refund_no", out_refund_no);

		params.put("total_fee", total_fee+"");
		params.put("refund_fee", refund_fee+"");
		params.put("op_user_id", mchId);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);

		String data = mapToXml(params);
		String resultXml="";
		Map<String,String> errorResult=new HashMap<>();
		try {
			resultXml = SSLHttpUtils.sendPost(WEIXIN_REFUND_URL, data,cretPath,mchId);
			Map<String,String> resultMap = xmlToMap(resultXml);
			log.info("refund result:"+resultMap);
			if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
				if(validSign(resultMap,mchKey)){
					return resultMap;
				}else{
					resultMap.put("ERROR", "wx refund sign error");
                    resultMap.put("ERROR_MSG", "验签出错");
				}
			}else{
				String error=resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des");
				resultMap.put("ERROR",error);
                String errorMsg = StringUtils.isNotBlank(resultMap.get("err_code_des")) ? resultMap.get("err_code_des") : getRefundErrorCodeMsg(resultMap.get("err_code"));
                resultMap.put("ERROR_MSG", errorMsg);
			}
			return resultMap;
		} catch(Exception e){
			e.printStackTrace();
			errorResult.put("ERROR", e.getMessage());
			log.error("退款请求发送失败！");
		}
		return errorResult;
	}



	/**
	 * 用于调用微信统一下单接口
	 * 返回 微信用于  扫码支付   的链接
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param notify_url
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String,String> createWxPay(String out_trade_no,String total_fee,String spbill_create_ip,String notify_url,String body) throws DocumentException{
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", RESTO_APPID);
		params.put("mch_id", RESTO_MCH_ID);
		//params.put("sub_mch_id",RESTO_SUB_MCH_ID);
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("body", body);
		params.put("out_trade_no",out_trade_no);
		//微信的金额单位是   分   ,所以需要 乘以 100
		BigDecimal chargeMoney = new BigDecimal(total_fee).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_DOWN);
		params.put("total_fee",chargeMoney.toString());
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", "NATIVE");
		params.put("attach", "test");
		String sign = generateSign(params,RESTO_MCHKEY);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		String resultXml = HttpRequest.post(WEIXIN_PAY_ORDER_URL).send(xmlData).body();
		log.info("发送构建微信支付请求:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> returnParamMap = new HashMap<String, String>();
		returnParamMap.put("success", "false");
		if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,RESTO_MCHKEY)){
				log.info("签名验证成功");
				returnParamMap.put("url", resultMap.get("code_url"));
				returnParamMap.put("success", "true");
			}else{
				returnParamMap.put("msg", "微信服务验证失败！");
			}
		}else{
			returnParamMap.put("msg", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));
		}
		return returnParamMap;
	}

	/**
	 * 用于调用微信统一下单接口
	 * 返回 微信用于  扫码支付   的链接
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param notify_url
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String,String> createWxPay(String out_trade_no,String total_fee,String spbill_create_ip,String notify_url,String body,String attach) throws DocumentException{
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", RESTO_APPID);
		params.put("mch_id", RESTO_MCH_ID);
		//params.put("sub_mch_id",RESTO_SUB_MCH_ID);
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("body", body);
		params.put("out_trade_no",out_trade_no);
		//微信的金额单位是   分   ,所以需要 乘以 100
		BigDecimal chargeMoney = new BigDecimal(total_fee).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_DOWN);
		params.put("total_fee",chargeMoney.toString());
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", "NATIVE");
		params.put("attach", attach);
		String sign = generateSign(params,RESTO_MCHKEY);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		String resultXml = HttpRequest.post(WEIXIN_PAY_ORDER_URL).send(xmlData).body();
		log.info("发送构建微信支付请求:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> returnParamMap = new HashMap<String, String>();
		returnParamMap.put("success", "false");
		if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,RESTO_MCHKEY)){
				log.info("签名验证成功");
				returnParamMap.put("url", resultMap.get("code_url"));
				returnParamMap.put("success", "true");
			}else{
				returnParamMap.put("msg", "微信服务验证失败！");
			}
		}else{
			returnParamMap.put("msg", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));
		}
		return returnParamMap;
	}


	/**
	 * 调用微信关闭订单接口
	 * 参数列表
	 * @throws
	 * @throws DocumentException
	 *
	 */
	public static Map<String,String> closeJSAPIPayRequest(String out_trade_no,
														  String appid,String mchId,String mchKey) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mchId);
		params.put("out_trade_no",out_trade_no);
		params.put("nonce_str", out_trade_no);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		log.info("发送关闭订单请求参数:"+xmlData);
		String resultXml = HttpRequest.post(WEIXIN_CLOSE_ORDER_URL).send(xmlData).body();
		log.info("发送关闭订单请求结果:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> jspApiParams = new HashMap<String, String>();
		if("SUCCESS".equals(resultMap.get("return_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,mchKey)){
				log.info("签名验证成功");
				jspApiParams.put("appId", resultMap.get("appid"));
				String timeStamp = System.currentTimeMillis()+"";
				timeStamp = timeStamp.length()>32?timeStamp.substring(0,32):timeStamp;
				jspApiParams.put("timeStamp", timeStamp);
				jspApiParams.put("nonceStr", RandomStringUtils.randomAlphabetic(32));
				jspApiParams.put("package", "prepay_id="+resultMap.get("prepay_id"));
				jspApiParams.put("signType", "MD5");
				String paySign = generateSign(jspApiParams,mchKey);
				jspApiParams.put("paySign", paySign);
				return jspApiParams;
			}else{
				jspApiParams.put("ERROR", "微信服务验证失败！");
			}
		}else{
			jspApiParams.put("ERROR", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));
		}
		return jspApiParams;
	}




	/**
	 * 调用微信统一下单接口
	 * 参数列表
	 * @throws
	 * @throws DocumentException
	 *
	 */
	public static Map<String,String> createJSAPIPayRequestNew(String sub_appId, String body, String out_trade_no,
															  int total_fee, String spbill_create_ip, String openid,String notify_url,
															  String appid,String mchId,String mchKey,String attach,String sub_mch_id,Integer is_sub) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mchId);
//		if(mchId.length()==8){
//			params.put("sub_mch_id",mchId);
//		}

		if(is_sub == 0){ //如果服务商的公众号 不是 下单的公众号
			params.put("sub_appid",sub_appId);
			params.put("sub_openid",openid);
		}else{
			params.put("openid", openid);
		}

		params.put("sub_mch_id",sub_mch_id);
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("body", body);
		params.put("out_trade_no",out_trade_no);
		params.put("total_fee",total_fee+"");
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", "JSAPI");


		params.put("attach", attach);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		log.info("发送构建微信支付请求参数:"+xmlData);
		String resultXml = HttpRequest.post(WEIXIN_PAY_ORDER_URL).send(xmlData).body();
		log.info("发送构建微信支付请求结果:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> jspApiParams = new HashMap<String, String>();
		if("SUCCESS".equals(resultMap.get("return_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,mchKey)){
				log.info("签名验证成功");
				jspApiParams.put("appId", resultMap.get("appid"));
				String timeStamp = System.currentTimeMillis()+"";
				timeStamp = timeStamp.length()>32?timeStamp.substring(0,32):timeStamp;
				jspApiParams.put("timeStamp", timeStamp);
				jspApiParams.put("nonceStr", RandomStringUtils.randomAlphabetic(32));
				jspApiParams.put("package", "prepay_id="+resultMap.get("prepay_id"));
				jspApiParams.put("signType", "MD5");
				String paySign = generateSign(jspApiParams,mchKey);
				jspApiParams.put("paySign", paySign);
				return jspApiParams;
			}else{
				jspApiParams.put("ERROR", "微信服务验证失败！");
			}
		}else{
			jspApiParams.put("ERROR", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));
		}
		return jspApiParams;
	}

	/**
	 * 发送现金红包
	 * @param object
	 * @return
	 * @throws UnknownHostException
	 */
	public static Map<String, String> sendredpack(JSONObject object) throws UnknownHostException {
		Map<String, String> params = new HashMap<>();
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("mch_billno", object.get("mch_billno").toString());
		params.put("mch_id", object.get("mch_id").toString());
		params.put("wxappid", object.get("wxappid").toString());
		params.put("send_name", object.get("send_name").toString());
		params.put("re_openid", object.get("re_openid").toString());
		params.put("total_amount", object.get("total_amount").toString());
		params.put("total_num", "1");
		params.put("wishing", object.get("wishing").toString());
		params.put("client_ip", InetAddress.getLocalHost().getHostAddress());
		params.put("act_name", "充值分红");
		params.put("remark", "充值分红");
		Integer totalAmount = Integer.valueOf(object.get("total_amount").toString()) / 100;
		if (totalAmount.compareTo(200) > 0){
			params.put("scene_id","PRODUCT_5");
		}
		if (object.get("consume_mch_id") != null){
			params.put("consume_mch_id",object.get("consume_mch_id").toString());
		}
		if (object.get("sub_mch_id") != null){
			params.put("sub_mch_id",object.get("sub_mch_id").toString());
		}
		if (object.get("msgappid") != null){
			params.put("msgappid",object.get("msgappid").toString());
		}
		String sign = generateSign(params,object.get("mch_key").toString());
		params.put("sign", sign);
		String data = mapToXml(params);
		String resultXml;
		Map<String,String> errorResult=new HashMap<>();
		try {
			resultXml = SSLHttpUtils.sendPost(WEIXIN_SEND_RED_PACK, data,object.get("cert_path").toString(),object.get("mch_id").toString());
			Map<String,String> resultMap = xmlToMap(resultXml);
			if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
				return resultMap;
			}else{
				String error=resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des");
				resultMap.put("ERROR",error);
			}
			return resultMap;
		} catch(Exception e){
			e.printStackTrace();
			errorResult.put("ERROR", e.getMessage());
		}
		return errorResult;
	}

	/**
	 * 微信刷卡支付
	 * @return
	 */
	public static Map<String,String> crashPay(String appid,String mch_id,String sub_mch_id,String out_trade_no,int total_fee
			,String auth_code,String articleName,String spbill_create_ip,String mchKey) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		if(!StringUtils.isEmpty(sub_mch_id)){
			params.put("sub_mch_id", sub_mch_id);
		}
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("body", articleName);
		params.put("out_trade_no",out_trade_no);
		params.put("total_fee",String.valueOf(total_fee));
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("auth_code",auth_code);
		params.put("attach", "");
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		String resultXml = HttpRequest.post(WEIXIN_CARD_PAY).send(xmlData).body();
		log.info("发送构建微信支付请求:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> returnParamMap = new HashMap<String, String>();
		returnParamMap.put("success", "false");
		if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,mchKey)){
				log.info("签名验证成功");
				returnParamMap.put("success", "true");
				returnParamMap.put("transactionId", resultMap.get("transaction_id"));
				returnParamMap.put("data", JSON.toJSONString(resultMap));
			}else{
				returnParamMap.put("msg", "微信服务验证失败");
			}
		}else{
			returnParamMap.put("msg", resultMap.get("err_code_des"));
			returnParamMap.put("errCode", resultMap.get("err_code"));
		}
		return returnParamMap;
	}

	/**
	 * 查询支付进度
	 * @param appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param out_trade_no
	 * @param mchKey
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String,String> queryPay(String appid,String mch_id,String sub_mch_id,String out_trade_no,
											  String mchKey) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		if(!StringUtils.isEmpty(sub_mch_id)){
			params.put("sub_mch_id", sub_mch_id);
		}
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("out_trade_no",out_trade_no);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		String resultXml = HttpRequest.post(WEIXIN_SELECT_ORDER).send(xmlData).body();
		log.info("发送查询微信支付订单进度请求:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> returnParamMap = new HashMap<String, String>();
		returnParamMap.put("success", "false");
		if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,mchKey)){
				//
				switch (resultMap.get("trade_state")){
					case "SUCCESS":
						returnParamMap.put("success","true");
						returnParamMap.put("data",JSONObject.toJSONString(resultMap));
						break;
					case "REFUND":
						returnParamMap.put("success","false");
						break;
					case "NOTPAY":
						returnParamMap.put("success","false");
						break;
					case "CLOSED":
						returnParamMap.put("success","false");
						break;
					case "REVOKED":
						returnParamMap.put("success","false");
						break;
					case "USERPAYING":
						break;
					case "PAYERROR":
						returnParamMap.put("success","false");
						break;
				}
				returnParamMap.put("trade_state",resultMap.get("trade_state"));
				returnParamMap.put("msg",resultMap.get("trade_state_desc"));
			}else{
				returnParamMap.put("msg","微信验证签名错误");
			}
		}else{
			returnParamMap.put("msg", resultMap.get("err_code_des"));
			returnParamMap.put("errCode", resultMap.get("err_code"));
		}
		return returnParamMap;
	}

	/**
	 * 刷卡支付撤销订单接口
	 * @param appId
	 * @param mchId
	 * @param subMchId
	 * @param outTradeNo
	 * @return
	 */
	public static Map<String, String> reverseOrder(String appId, String mchId, String subMchId, String mchKey, String outTradeNo, String certPath){
		//返回的参数
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put("success", "false");
		//调用接口所需参数
		Map<String,String> paramData = new HashMap<>();
		paramData.put("appid", appId);
		paramData.put("mch_id", mchId);
		if (StringUtils.isNotBlank(subMchId)){
			//如果是服务商模式
			paramData.put("sub_mch_id", subMchId);
		}
		paramData.put("out_trade_no", outTradeNo);
		paramData.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		String sign = generateSign(paramData, mchKey);
		paramData.put("sign", sign);
		String data = mapToXml(paramData);
		log.info("发起微信撤销订单请求：" + data);
		try{
			String result = SSLHttpUtils.sendPost(WEIXIN_REVERSE_ORDER, data,certPath,mchId);
			log.info("撤销订单结果：" + result);
			Map<String, String> resultMap = xmlToMap(result);
			if ("SUCCESS".equalsIgnoreCase(resultMap.get("return_code")) && "SUCCESS".equalsIgnoreCase(resultMap.get("result_code"))){
				returnMap.put("success", "true");
				returnMap.put("data", JSON.toJSONString(resultMap));
			}else{
				returnMap.put("msg", resultMap.get("err_code_des"));
			}
		}catch (Exception e){
			e.printStackTrace();
			returnMap.put("msg", "撤销订支付单出错，请线下处理");
		}
		return returnMap;
	}

	public static void main(String[] args) throws Exception{
		System.out.println(reverseOrder("wx36bd5b9b7d264a8c","1350659001", "", "0EDD5A4CE0652F1BE748D5433E627E93","ada8174879744ff88b83f0d3436d55f1","F:/API/ce6eb2e1-b9c4-4f9f-b5ae-95ba9ec3800f.p12"));
	}
}

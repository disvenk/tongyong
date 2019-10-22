package com.resto.api.brand.util;

import com.alibaba.fastjson.JSONObject;
import com.resto.conf.util.ApplicationUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class WeChatPayUtils {
	public static final String WEIXIN_REFUND_URL= "https://api.mch.weixin.qq.com/secapi/pay/refund";
	public static final String WEIXIN_PAY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder"; //统一下单接口
	public static final String WEIXIN_CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    public static final String WEIXIN_SEND_RED_PACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

	
	public static final String RESTO_MCH_ID = "1391463202";				//餐加服务商
	public static final String RESTO_SUB_MCH_ID = "1420007502";		//即收账户
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
				}
			}else{
				String error=resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des");
				resultMap.put("ERROR",error);
			}
			return resultMap;
		} catch(Exception e){
			e.printStackTrace();
			errorResult.put("ERROR", e.getMessage());
			log.error("退款请求发送失败！");
		}
		return errorResult; 
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
				}
			}else{
				String error=resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des");
				resultMap.put("ERROR",error);
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
		params.put("sub_mch_id",RESTO_SUB_MCH_ID);
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
		params.put("sub_mch_id",RESTO_SUB_MCH_ID);
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
			,String auth_code,String articleName,String spbill_create_ip,String attach,String mchKey) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		if(!StringUtils.isEmpty(sub_mch_id)){
			params.put("sub_mch_id", sub_mch_id);
		}
//		params.put("sub_mch_id",RESTO_SUB_MCH_ID);
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("body", articleName);
		params.put("out_trade_no",out_trade_no);
		//微信的金额单位是   分   ,所以需要 乘以 100
		params.put("total_fee",String.valueOf(total_fee));
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("auth_code",auth_code);
		params.put("attach", attach);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		String resultXml = HttpRequest.post("https://api.mch.weixin.qq.com/pay/micropay").send(xmlData).body();
		log.info("发送构建微信支付请求:"+resultXml);
		Map<String,String> resultMap = xmlToMap(resultXml);
		Map<String,String> returnParamMap = new HashMap<String, String>();
		returnParamMap.put("success", "false");
		if("SUCCESS".equals(resultMap.get("return_code"))&&"SUCCESS".equals(resultMap.get("result_code"))){
			log.info("返回结果成功");
			if(validSign(resultMap,mchKey)){
				log.info("签名验证成功");
				returnParamMap.put("url", resultMap.get("code_url"));
				returnParamMap.put("success", "true");
				returnParamMap.put("msg",resultMap.get("out_trade_no"));
			}else{
				returnParamMap.put("msg", "微信服务验证失败！");
			}
		}else{
			returnParamMap.put("msg", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));

		}
		return returnParamMap;
	}

	public static Map<String,String> queryPay(String appid,String mch_id,String sub_mch_id,String out_trade_no,
											  String mchKey) throws DocumentException {
		Map<String,String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		if(!StringUtils.isEmpty(sub_mch_id)){
			params.put("sub_mch_id", sub_mch_id);
		}
//		params.put("sub_mch_id",RESTO_SUB_MCH_ID);
		params.put("nonce_str", RandomStringUtils.randomAlphabetic(32));
		params.put("out_trade_no",out_trade_no);
		String sign = generateSign(params,mchKey);
		params.put("sign", sign);
		String xmlData = mapToXml(params);
		String resultXml = HttpRequest.post("https://api.mch.weixin.qq.com/pay/orderquery").send(xmlData).body();
		log.info("发送构建微信支付请求:"+resultXml);
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
						returnParamMap.put("msg", JSONObject.toJSON(resultMap).toString());
						break;
					case "REFUND":
						returnParamMap.put("success","false");
						returnParamMap.put("msg","REFUND");
						break;
					case "NOTPAY":
						returnParamMap.put("success","false");
						returnParamMap.put("msg","NOTPAY");
						break;
					case "CLOSED":
						returnParamMap.put("success","false");
						returnParamMap.put("msg","CLOSED");
						break;
					case "REVOKED":
						returnParamMap.put("success","false");
						returnParamMap.put("msg","REVOKED");
						break;
					case "USERPAYING":
						returnParamMap.put("success","false");
						returnParamMap.put("msg","USERPAYING");
						break;
					case "PAYERROR":
						returnParamMap.put("success","false");
						returnParamMap.put("msg","PAYERROR");
						break;
				}
			}else{
				returnParamMap.put("success","false");
				returnParamMap.put("msg","微信验证签名错误");
			}
		}else{
			returnParamMap.put("success","false");
			returnParamMap.put("msg", resultMap.get("return_msg")+resultMap.get("err_code")+":"+resultMap.get("err_code_des"));

		}
		return returnParamMap;
	}



	public static void main(String[] args) throws UnknownHostException, DocumentException {
		String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();  //终端IP String(16)
		Map result = queryPay("wx36bd5b9b7d264a8c","1391463202","1420007502","ffd42ad8fc3a484383fb7bd749d351d4","E62ADDBD1A096932FA77DBAA75AF1731");
		System.out.println(result.get("msg").toString());
		JSONObject jsonObject = JSONObject.parseObject(result.get("msg").toString());
		System.out.println(jsonObject.toString());
//	 queryPay("wx36bd5b9b7d264a8c","1350659001","","d7d1fc864be8453395bd30e19670d079","0EDD5A4CE0652F1BE748D5433E627E93");
	}

}



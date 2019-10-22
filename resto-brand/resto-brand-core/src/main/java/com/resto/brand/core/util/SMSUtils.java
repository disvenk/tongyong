package com.resto.brand.core.util;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import java.math.BigDecimal;
import java.util.Map;

public class SMSUtils {
	public final static String CODE_SMS_TEMP = "SMS_6460321";  //验证码短信模板
	public final static String WAIMAI_SMS_TEMP = "SMS_6470317"; //外卖短信模板
	public final static String SMS_NOTICE_TEMP = "SMS_9990138";//欠费通知
	public final static String SMS_LESS_TEMP = "SMS_9920083";//余额不足通知
	public final static String SMS_CHARGE_SUCCESS = "SMS_10626409";//短信订单充值成功通知
	public final static String SMS_WAKE_LOSS = "SMS_88300007";//流失唤醒优惠券模版
	public final static String SMS_BAD_WARNING = "SMS_111775018";//差评预警模板
	public final static String SMS_SERVER_ERROR = "SMS_63315362";//系统异常模板
	public final static String SMS_CONSUMPTION_REBATE = "SMS_124325032";//消费返利模板

	public final static String SMS_PRODUCTION = "SMS_150174497";//流失唤醒优惠券模版


	public final static String DAY_SMS_SEND = "SMS_46725122";//日结短信

	public final static String REVIEW_AMOUNT_DAY_SMS_SEND = "SMS_148861777";//日结短信 开启newpos复核金额后的日结短信模板 (味千)

    public final static String SMS_NEW_DAY_SEND = "SMS_151232459"; //2018款日结短信模板

	//public final static String BRAND_ACCOUNT_SMS = "SMS_83915006";//品牌账户余额欠费通知

	public final static String ACCOUNT_NOT_ENOUGH = "SMS_84485070";//品牌账户余额不足通知

	public final static String ACCOUNT_NOT_USED = "SMS_84580055";//品牌账户余额欠费通知

	public final static String SMS_NEW_URL = "https://eco.taobao.com/router/rest";

	public final static String SIGN = "餐加";

	private static final String key = "23875993";

	private static final String secret = "decdaf64b1517d1f1469726cb297f82a";


	private final static String ALI_API = "https://ca.aliyuncs.com/gw/alidayu/sendSms";
	private final static String APP_KEY = "23330769";
	private final static String APP_SECRET = "f361c703a517d26fbf4cefdbec5a9fd1";

//	public static JSONObject sendCode(String brandName, String code, String phone, Map<String, String> logMap) {
//		//String data = "{'code':'" + code + "','product':'" + brandName + "'}";
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("code",code);
//		jsonObject.put("product",brandName);
//		return sendMessage(phone, jsonObject, SIGN, CODE_SMS_TEMP, logMap);
//	}
//
//	/**
//	 * 发送短信充值的短信
//	 *
//	 * @return
//	 */
//
//	public static JSONObject sendChargeMessagetoBrand(String brandName, BigDecimal chargeMoney, String phone,
//												  Integer remainderNum, Integer usedNum, String date, Map<String, String> logMap) {
//		//String data = "{'name':'" + brandName + "','time':'" + date + "','pay':'" + chargeMoney + "','nm1':'" + remainderNum + "','nm2':'" + usedNum + "'}";
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("name",brandName);
//		jsonObject.put("time",date);
//		jsonObject.put("pay",chargeMoney);
//		jsonObject.put("nm1",remainderNum);
//		jsonObject.put("nm2",usedNum);
//		return sendMessage(phone, jsonObject, SIGN, SMS_CHARGE_SUCCESS, logMap);
//	}

	/**
	 * 短信余额不足和欠费提醒通知
	 *
	 剩余短信条数/欠费短信条数
	 jsonObject 中 有两个参数 name:brandName num
	 * @param phone
	 * @return
	 */
	public static JSONObject sendNoticeToBrand(JSONObject jsonObject, String phone) {
		int num = jsonObject.getInteger("num");
		int temp = Math.abs(num);
		jsonObject.put("num",temp);
		//String data = "{'name':'" + brandName + "','num':'" + temp + "'}";
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("name",brandName);
//		jsonObject.put("num",temp);
		if (num < 0) {
			return sendMessage(phone, jsonObject, SIGN, SMS_NOTICE_TEMP);
		} else {
			return sendMessage(phone, jsonObject, SIGN, SMS_LESS_TEMP);
		}
	}


	/**
	 *
	 * @param phone
	 * @param param
	 * @param sign 签名 默认 餐加
	 * @param codeSmsTemp 模板id
	 * @return
	 */
	public static JSONObject sendMessage(String phone, JSONObject param, String sign, String codeSmsTemp) {
		//初始化
		TaobaoClient client = new DefaultTaobaoClient(SMS_NEW_URL, key, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");//短信类型，传入值请填写normal
		req.setSmsFreeSignName(sign);
		req.setSmsParam(JSONObject.toJSONString(param));//短信模板变量
		req.setRecNum(phone);//群发短信需传入多个号码，以英文逗号分隔
		req.setSmsTemplateCode(codeSmsTemp);//短信模板ID
		JSONObject aliResult = new JSONObject();
		try {
			AlibabaAliqinFcSmsNumSendResponse response = client.execute(req);
			//阿里大于返回 responese中如果 errorCode为 null说明返回成功 直接去获取body中的值

			System.out.println("阿里大鱼发送短信结果为："+JSONObject.toJSONString(response));
			if(response.getErrorCode()==null){//返回成功
				JSONObject bodyJson = JSONObject.parseObject(response.getBody());
				JSONObject responseJson = bodyJson.getJSONObject("alibaba_aliqin_fc_sms_num_send_response");
				aliResult=responseJson.getJSONObject("result");
			}else {
				aliResult.put("err_code",response.getErrorCode());
				aliResult.put("success",false);
				aliResult.put("msg",response.getSubMsg());
			}

		} catch (ApiException e) {
			aliResult.put("err_code",-100);
			aliResult.put("success",false);
			aliResult.put("msg","请求阿里大鱼出错");
			e.printStackTrace();
		}
		return  aliResult;
	}


	public static void main(String[] args) throws ApiException {
		JSONObject smsParam = new JSONObject();
		smsParam.put("code","1916");
		smsParam.put("product","MEX&CO");
		smsParam.put("sss","23321");
		JSONObject jsonObject = sendMessage("13317182430,15123849097",smsParam,"餐加","SMS_6460321");

//		String data = "{'code':'" + 123 + "','product':'" + 222 + "'}";
//		TaobaoClient client = new DefaultTaobaoClient(SMS_NEW_URL, key, secret);
//		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//		req.setSmsType("normal");
//		req.setSmsFreeSignName(SIGN);
//		req.setSmsParamString(data);
//		req.setRecNum("18621943805");
//		req.setSmsTemplateCode("SMS_6460321");
//		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
//		System.out.println(rsp.getBody());

//        String token = WeChatUtils.getAccessToken("wxc2aea09ab96e5ddb", "e757d5bdfa4b63b40904ff9cdb9f69d7");
//        org.json.JSONObject qrParam = new org.json.JSONObject();
//        qrParam.put("QrCodeId", "ffc2f11705ad47e9bd28c1f588f3a3bd");
//        String result = WeChatUtils.getParamQrCode(token, qrParam.toString());//二维码的附带参数字符串类型，长度不能超过64
//        org.json.JSONObject obj = new org.json.JSONObject(result);
//        String img = obj.has("ticket")?obj.getString("ticket"):"";
//        String sss = WeChatUtils.showQrcode(img);
//        System.out.println(sss);
	}





}

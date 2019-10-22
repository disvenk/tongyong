package com.resto.api.brand.util;

import com.resto.conf.util.WeChatUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WeChatJsUtils {
	public final static String onMenuShareTimeline = "onMenuShareTimeline";
	public final static String onMenuShareAppMessage = "onMenuShareAppMessage";
	public final static String onMenuShareQQ = "onMenuShareQQ";
	public final static String onMenuShareWeibo = "onMenuShareWeibo";
	public final static String onMenuShareQZone = "onMenuShareQZone";
	public final static String startRecord = "startRecord";
	public final static String stopRecord = "stopRecord";
	public final static String onVoiceRecordEnd = "onVoiceRecordEnd";
	public final static String playVoice = "playVoice";
	public final static String pauseVoice = "pauseVoice";
	public final static String stopVoice = "stopVoice";
	public final static String onVoicePlayEnd = "onVoicePlayEnd";
	public final static String uploadVoice = "uploadVoice";
	public final static String downloadVoice = "downloadVoice";
	public final static String chooseImage = "chooseImage";
	public final static String previewImage = "previewImage";
	public final static String uploadImage = "uploadImage";
	public final static String downloadImage = "downloadImage";
	public final static String translateVoice = "translateVoice";
	public final static String getNetworkType = "getNetworkType";
	public final static String openLocation = "openLocation";
	public final static String getLocation = "getLocation";
	public final static String hideOptionMenu = "hideOptionMenu";
	public final static String showOptionMenu = "showOptionMenu";
	public final static String hideMenuItems = "hideMenuItems";
	public final static String showMenuItems = "showMenuItems";
	public final static String hideAllNonBaseMenuItem = "hideAllNonBaseMenuItem";
	public final static String showAllNonBaseMenuItem = "showAllNonBaseMenuItem";
	public final static String closeWindow = "closeWindow";
	public final static String scanQRCode = "scanQRCode";
	public final static String chooseWXPay = "chooseWXPay";
	public final static String openProductSpecificView = "openProductSpecificView";
	public final static String addCard = "addCard";
	public final static String chooseCard = "chooseCard";
	public final static String openCard = "openCard";


	public static JSONObject getJsConfig(String[] jsApiList, boolean debug,String url,String appid,String secret) {
		String jsapi_ticket = WeChatUtils.getJsAccessToken(appid,secret);
		String nonceStr = RandomStringUtils.randomAlphanumeric(16);
		long timestamp = System.currentTimeMillis()%1000000000;
		String params = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsapi_ticket,	nonceStr, ""+timestamp, url);
		String signature = sha1Sign(params);
		JSONObject ob = new JSONObject();
		ob.put("debug", debug);
		ob.put("appId", appid);
		ob.put("timestamp", timestamp);
		ob.put("nonceStr", nonceStr);
		ob.put("signature", signature);
		ob.put("jsApiList", jsApiList);
		return ob;
	}


	public static String sha1Sign(String params) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(params.getBytes());
			byte[] bytes = digest.digest();
			params = byte2hex(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return params;
	}

	public static String byte2hex(byte[] b) // 二行制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}
}

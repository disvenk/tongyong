package com.resto.wechat.web.util;

import com.resto.brand.core.util.HttpRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import net.sf.json.JSONArray;
import org.json.JSONObject;

import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xielc on 2017/8/26.
 */
public class WeChatCardUtils {
    static final String GET_JS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    static final String GET_MEMBERCARD_URL = "https://api.weixin.qq.com/card/membercard/userinfo/get?access_token=TOKEN";
    public static Map<String, String> api_ticket_map = new HashMap<String, String>();
    public static String appidCard = "";
    public static String appsecretCard = "";

    public static JSONObject getJsSDKConfig(String accessToken, String[] jsApiList, boolean debug, String url, String appid, String secret, String card_id) {
        appidCard=appid;
        appsecretCard=secret;
        String nonceStr = RandomStringUtils.randomAlphanumeric(16);
        long timestamp = System.currentTimeMillis()%1000000000;
        String params = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", accessToken,	nonceStr, ""+timestamp, url);
        String signature = sha1Sign(params);
        if(api_ticket_map.get(appid)==null){
            getApiTicket(appid, accessToken);
        }
        String api_ticket=api_ticket_map.get(appid);
        // wx.addCard的注意，两者的签名方式不一样
        Map<String, String> ret_addCard = SignAddCard.sign(card_id,api_ticket);
        Map<String, String> ret_chooseCard = SignChooseCard.sign(appid,card_id,api_ticket);
        JSONObject ob = new JSONObject();
        ob.put("debug", debug);
        ob.put("appId", appid);
        ob.put("timestamp", timestamp);
        ob.put("nonceStr", nonceStr);
        ob.put("signature", signature);
        ob.put("jsApiList", jsApiList);
        ob.put("card_id",ret_addCard.get("card_id"));
        ob.put("timestamp_addCard",ret_addCard.get("timestamp"));
        ob.put("noncestr_addCard",ret_addCard.get("noncestr"));
        ob.put("signature_addCard",ret_addCard.get("signature"));
        //ob.put("card_id_chooseCard",ret_chooseCard.get("card_id"));
        ob.put("card_type",ret_chooseCard.get("card_type"));
        ob.put("timestamp_chooseCard",ret_chooseCard.get("timestamp"));
        ob.put("noncestr_chooseCard",ret_chooseCard.get("noncestr"));
        ob.put("signature_chooseCard",ret_chooseCard.get("signature"));
        return ob;
    }

    public static String getApiTicket(String appid, String accessToken){
        StringBuffer url_ticket = new StringBuffer(GET_JS_TICKET_URL + "?");
        url_ticket.append("access_token=" + accessToken);
        url_ticket.append("&type=" + "wx_card");
        String result= HttpRequest.get(url_ticket.toString()).body();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String api_ticket = jsonObject.getString("ticket");
        api_ticket_map.put(appid,api_ticket);
        return api_ticket;
    }

    public static String getPhone(String cardId, String userCardCode,String accessToken){
        //String accessToken=com.resto.brand.core.util.WeChatUtils.getAccessToken(appidCard, appsecretCard);
        // 需要提交的json数据
        String jsonData = "{\"card_id\":\"%s\",\"code\":\"%s\"}";
        net.sf.json.JSONObject jsonObject=HttpClientUtil.httpsRequest(GET_MEMBERCARD_URL.replace("TOKEN", accessToken),"POST",String.format(jsonData, cardId, userCardCode));
        System.out.println("调用微信拉取会员接口返回信息:"+jsonObject);
        net.sf.json.JSONObject ob=jsonObject.getJSONObject("user_info");
        System.out.println("user_info:"+ob);
        JSONArray arr = ob.getJSONArray("common_field_list");
        net.sf.json.JSONObject op=net.sf.json.JSONObject.fromObject(arr.get(0));
        String phone=op.getString("value");
        return phone;
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static String messageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new ArticleText().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 扩展xstream使其支持CDATA
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

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

    public static JSONObject getJsConfig(String[] jsApiList, boolean debug,String url,String appid,String accessToken) {
        String nonceStr = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16);
        long timestamp = System.currentTimeMillis()%1000000000;
        String params = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", accessToken,	nonceStr, ""+timestamp, url);
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

    public static String byte2hex(byte[] b) // 二行制转字符串
    {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

}

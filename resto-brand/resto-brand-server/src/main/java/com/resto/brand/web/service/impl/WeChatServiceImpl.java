package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.resto.brand.core.entity.MdlUpload;
import com.resto.brand.core.entity.PictureResult;
import com.resto.brand.core.entity.ResultDto;
import com.resto.brand.core.util.*;
import com.resto.brand.core.util.HttpRequest.HttpRequestException;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.util.HttpClientUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;
@Component
@Service
public class WeChatServiceImpl implements WeChatService {
    @Value("http://${thired.wechat.token}")
    private String tokenUrl;

    @Override
    public String getUserAuthorizeUrl(String state, String redirectUrl, String appid) {
        StringBuffer url = new StringBuffer(USER_AUTHORIZE_URL + "?");
        url.append("appid=" + appid);
        try {
            url.append("&redirect_uri=" + URLEncoder.encode(redirectUrl, "utf-8"));
            url.append("&response_type=code");
            url.append("&scope=snsapi_userinfo");
            url.append("&state=" + state);
            url.append("#wechat_redirect");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    @Override
    public String getUrlAccessToken(String code, String appid, String secret) {
        StringBuffer url = new StringBuffer(GET_WEB_ACCESS_TOKEN_URL + "?");
        url.append("appid=" + appid);
        url.append("&secret=" + secret);
        url.append("&code=" + code);
        url.append("&grant_type=authorization_code");
        return HttpRequest.get(url.toString()).body();
    }

    @Override
    public String getUrlRefreshToken(String appid, String refresh_token) {
        StringBuffer url = new StringBuffer(WEIXIN_REFRESH_URL + "?");
        url.append("appid=" + appid);
        url.append("&grant_type=refresh_token");
        url.append("&refresh_token="+refresh_token);
        return HttpRequest.get(url.toString()).body();
    }

    @Override
    public String getURLCheckToken(String access_token,String openid){
        StringBuffer url = new StringBuffer(WEIXIN_CHECK_TOKEN_URL + "?");
        url.append("access_token=" + access_token);
        url.append("&openid=" + openid);
        return HttpRequest.get(url.toString()).body();
    }

    @Override
    public String getUserInfo(String token, String openid) {
        StringBuffer url = new StringBuffer(GET_USER_INFO_URL + "?");
        url.append("access_token=" + token);
        url.append("&openid=" + openid);
        url.append("&lang=zh_CN");
        return HttpRequest.get(url.toString()).body();
    }

    @Override
    public String getUserInfoSubscribe(String token, String openid) {
        StringBuffer url = new StringBuffer(GET_USER_INFO_URL_SUBSCRIBE + "?");
        url.append("access_token=" + token);
        url.append("&openid=" + openid);
        url.append("&lang=zh_CN");
        return HttpRequest.get(url.toString()).body();
    }

    @Override
    public String getJsAccessToken(String appid, String secret) {
        String url =tokenUrl + "/jstoken/getstr/" + appid + "/" + secret;
        return HttpRequest.get(url).body();
    }

    @Override
    public String getAccessToken(String appid, String secret) {
        String url = tokenUrl + "/token/getstr/" + appid + "/" + secret;
        return HttpRequest.get(url).body();
    }

    @Override
    public void flushAccessToken(String appid, String secret) {
        String url = tokenUrl + "/token/flush/" + appid + "/" + secret;
        HttpRequest.get(url).body();
    }

    @Override
    public String sendCustomerMsg(String message, String openid, String appid, String secret) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("touser", openid);
        data.put("msgtype", "text");
        Map<String, String> content = new HashMap<String, String>();
        content.put("content", message);
        data.put("text", content);
        String accessToken = getAccessToken(appid, secret);
        String url = SEND_CUSTOM_MESSAGE_URL + "=" + accessToken;
        String jsonData = JSON.toJSONString(data);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
            result = e.getMessage();
        }
        if(result.indexOf("\"errmsg\":\"ok\"") == -1){
            Map map = new HashMap(4);
            map.put("brandName", appid);
            map.put("fileName", openid);
            map.put("type", "weMsgAction");
            map.put("content", "系统向用户:"+openid+"推送微信消息:" + content + ",\n微信返回内容:" + result + "请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc(map);
        }
        return result;
    }

    @Override
    public String sendCustomerImageMsg(String mediaId, String openid, String appid, String secret) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("touser", openid);
        data.put("msgtype", "image");
        Map<String, String> content = new HashMap<String, String>();
        content.put("media_id", mediaId);
        data.put("image", content);
        String accessToken = getAccessToken(appid, secret);
        String url = SEND_CUSTOM_MESSAGE_URL + "=" + accessToken;
        String jsonData = JSON.toJSONString(data);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @Override
    public String getApiSetIndustry(String appid, String secret) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("industry_id1", 1);
        data.put("industry_id2", 10);
        String accessToken = getAccessToken(appid, secret);
        String url = GET_API_SET_INDUSTRY + accessToken;
        String jsonData = JSON.toJSONString(data);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @Override
    public String getTemplate(String templateIdShort, String appid, String secret){
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("template_id_short", templateIdShort);
        String accessToken = getAccessToken(appid, secret);
        String url = GET_TEMPLATE + accessToken;
        String jsonData = JSON.toJSONString(data);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @Override
    public String sendTemplate(String opendId, String templateId, String jumpUrl, Map<String, Map<String, Object>> content, String appid, String secret){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("touser", opendId);
        param.put("template_id", templateId);
        param.put("url", jumpUrl);
        param.put("data", content);
        String accessToken = getAccessToken(appid, secret);
        String url = SEND_TEMPLATE + accessToken;
        String jsonData = JSON.toJSONString(param);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @Override
    public String sendTemplate(String opendId, String templateId, String jumpUrl, Map<String, Object> miniprogram, Map<String, Map<String, Object>> content, String appid, String secret){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("touser", opendId);
        param.put("template_id", templateId);
        param.put("url", jumpUrl);
        param.put("miniprogram", miniprogram);
        param.put("data", content);
        String accessToken = getAccessToken(appid, secret);
        String url = SEND_TEMPLATE + accessToken;
        String jsonData = JSON.toJSONString(param);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @Override
    public String delTemplate(String templateId, String appid, String secret){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("template_id", templateId);
        String accessToken = getAccessToken(appid, secret);
        String url = DEL_TEMPLATE + accessToken;
        String jsonData = JSON.toJSONString(param);
        String result = "";
        try {
            result = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
        } catch (HttpRequestException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @Override
    public boolean checkSignature(String signature, String timestamp,
                                         String nonce) {
        if (signature == null || timestamp == null || nonce == null) {
            return false;
        }
        String[] arr = new String[]{TOKEN, timestamp, nonce};
        // 将 token, timestamp, nonce 三个参数进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 shal 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param digest
     * @return
     */
    private String byteToStr(byte[] digest) {
        String strDigest = "";
        for (int i = 0; i < digest.length; i++) {
            strDigest += byteToHexStr(digest[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param b
     * @return
     */
    private String byteToHexStr(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b >>> 4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];

        String s = new String(tempArr);
        return s;
    }


    /**
     * 将文本消息封装成微信规定的xml格式
     *
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    @Override
    public String getXmlText(String toUserName, String fromUserName,
                                    String content) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(toUserName);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(fromUserName);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");
        sb.append(content);
        sb.append("]]></Content></xml>");
        return sb.toString();
    }

    /**
     * 生成单图文消息
     *
     * @param toUsername   收件人
     * @param fromUsername 发件人
     * @param title        标题
     * @param description  描述
     * @param picUrl       图片链接
     * @param url          消息链接（消息点开后转跳到对应链接）
     * @return
     */
    @Override
    public String getXmlImageUrl(String toUsername, String fromUsername,
                                        String title, String description, String picUrl, String url) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(toUsername);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(fromUsername);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[news]]></MsgType>");
        sb.append("<ArticleCount>1</ArticleCount>");
        sb.append("<Articles><item><Title><![CDATA[");
        sb.append(title);
        sb.append("]]></Title><Description><![CDATA[");
        sb.append(description);
        sb.append("]]></Description><PicUrl><![CDATA[");
        sb.append(picUrl);
        sb.append("]]></PicUrl><Url><![CDATA[");
        sb.append(url);
        sb.append("]]></Url></item></Articles></xml>");
        return sb.toString();
    }

    @Override
    public String getXmlImg(String toUsername, String fromUsername, String picUrl) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(toUsername);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(fromUsername);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[image]]></MsgType>");
        sb.append("<PicUrl><![CDATA[");
        sb.append(picUrl);
        sb.append("]]></PicUrl><MsgId>5836982871638042400</MsgId></xml>");
        return sb.toString();
    }

    @Override
    public void sendCustomerMsgASync(final String message, final String wechatId, final String appid, final String appsecret) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                sendCustomerMsg(message, wechatId, appid, appsecret);
            }
        });
        t.start();
    }

    @Override
    public void sendDayCustomerMsgASync(final String message, final String wechatId, final String appid, final String appsecret, final String telephone, final String brandName,final String shopName) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = sendCustomerMsg(message, wechatId, appid, appsecret);
                Map map = new HashMap(4);
                map.put("brandName",brandName);
                map.put("fileName",telephone);
                map.put("type","daySmsAction");
                map.put("content","发微日结短信微信推送的店铺为:"+shopName+"微信返回的内容:"+result+"请求服务器地址:"+MQSetting.getLocalIP());
                doPostAnsc(map);
            }
        });
        t.start();
    }

    /**
     * 获取带参数的二维码
     * 参考：https://mp.weixin.qq.com/wiki/18/167e7d94df85d8389df6c94a7a8f78ba.html
     * @param token
     * @param qrParam（二维码的附带参数字符串类型，长度不能超过64）
     * @author lmx
     * @return
     */
    @Override
    public String getParamQrCode(String token,String qrParam) {
        String url = GET_PARAM_QRCODE + token;
        JSONObject param2 = new JSONObject();
        param2.put("scene_str", qrParam);
        JSONObject param1 = new JSONObject();
        param1.put("scene", param2);
        JSONObject param = new JSONObject();
        param.put("action_name", "QR_LIMIT_STR_SCENE");
        param.put("action_info", param1);
        String result = HttpClient.doPostJson(url,param.toString());
        return result;
    }

    /**
     * 生成带参数的二维码
     */
    @Override
    public String showQrcode(String ticket) {
        String url = SHOW_QRCODE + ticket;
        return url;
    }

    @Override
    public String downLoadWXFile(String appid, String secret, String mediaId, String savePath, String systemPath) {
        String filePath = null;
        String endSavePath = null;
        String endPath = null;
        String accessToken = getAccessToken(appid, secret);
        String requestUrl = GET_FILE + accessToken + "&media_id=" + mediaId;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            String endwith = conn.getContentType();
            String[] end = endwith.split("/");

            systemPath = systemPath.replaceAll("\\\\", "/");
            int lastR = systemPath.lastIndexOf("/");
            systemPath = systemPath.substring(0,lastR)+"/";
            systemPath = systemPath.replace("wechat","shop");

            filePath = systemPath + savePath ;
            File file = new File(filePath);
            file.mkdirs();
            endSavePath = savePath + mediaId + "." + end[1];
            endPath = systemPath + endSavePath;

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(endPath));
            byte[] buf = new byte[8096];
            int size = 0;
            while ((size = bis.read(buf)) != -1)
                fos.write(buf, 0, size);
            fos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PictureResult picResult = UploadFilesUtil.uploadPic(new File(endPath));
        if(picResult.getError() == 0){
            return picResult.getUrl();
        }else{
            return null;
        }
    }

    @Override
    public ResultDto<MdlUpload> uploadPhoto(String accessToken, String type, String filePath) {
        ResultDto<MdlUpload> result = new ResultDto<MdlUpload>();
        String url = MEDIA_URL.replace("ACCESSTOKEN", accessToken).replace("TYPEA", type);
        File file = new File(filePath);
        net.sf.json.JSONObject jsonObject;
        try {
            HttpPostUtil post = new HttpPostUtil(url);
            post.addParameter("media", file);
            String s = post.send();
            jsonObject = net.sf.json.JSONObject.fromObject(s);
            if (jsonObject.containsKey("media_id")) {
                MdlUpload upload=new MdlUpload();
                upload.setMedia_id(jsonObject.getString("media_id"));
                upload.setType(jsonObject.getString("type"));
                upload.setCreated_at(jsonObject.getString("created_at"));
                result.setObj(upload);
                result.setErrmsg("success");
                result.setErrcode("0");
            } else {
                result.setErrmsg(jsonObject.getString("errmsg"));
                result.setErrcode(jsonObject.getString("errcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrmsg("Upload Exception:"+e.toString());
        }
        return result;
    }

    @Override
    public String getOpenMemberCardUrl(String appid,String secret,String cardId){
        String accessToken=this.getAccessToken(appid.trim(), secret.trim());
        //1.会员卡设置开卡激活填写的字段
        String jsonActivateData = "{\"card_id\":\"%s\",\"required_form\":{%s}}";
        String requiredForm="\"common_field_id_list\": [\"USER_FORM_INFO_FLAG_MOBILE\"]";
        net.sf.json.JSONObject jsonObject1= HttpClientUtil.httpsRequest(GET_ACTIVATEUSER_URL.replace("TOKEN", accessToken),"POST",String.format(jsonActivateData, cardId.trim(), requiredForm));
        String url="";
        if("0".equals(jsonObject1.getString("errcode"))){
            //2.获取开卡组件链接接口
            String jsonUrlData = "{\"card_id\":\"%s\",\"outer_str\":\"%s\"}";
            net.sf.json.JSONObject jsonObject2=HttpClientUtil.httpsRequest(GET_ACTIVATE_URL.replace("ACCESS_TOKEN", accessToken),"POST",String.format(jsonUrlData, cardId.trim(), "xx00"));
            url=jsonObject2.getString("url");
        }
        return url;

    }


}

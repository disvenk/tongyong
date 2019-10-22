package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.web.service.ElectronicTicketConfigService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatFaPiaoService;
import com.resto.brand.web.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * Created by disvenk.dai on 2018-09-17 15:54
 */
@Component
@Service
public class WechatFaPiaoServiceImpl implements WechatFaPiaoService {

    private static Logger log = LoggerFactory.getLogger(WechatFaPiaoServiceImpl.class);

    @Autowired
    WeChatService weChatService;

    @Autowired
    ElectronicTicketConfigService electronicTicketConfigService;

    /**
    *@Description:获取授权页链接
    *@Author:disvenk.dai
    *@Date:11:20 2018/9/19 0019
    */
    @Override
    public String getAuthUrl(String appId,String appSecret,String json) {
        String s = HttpRequestUtils.httpPost("https://api.weixin.qq.com/card/invoice/getauthurl?access_token=" + weChatService.getAccessToken(appId,appSecret), json);
        log.info("获取授权页链接json"+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String auth_url = jsonObject.getString("auth_url");
        log.info("授权页链接"+auth_url);
        if(auth_url==null || "".equals(auth_url)){
            log.error("获取授权页链接失败");
            throw new RuntimeException("获取授权页链接失败");
        }
        return auth_url;
    }

    /**
     * @Description:获取S_pappid
     * @Author:disvenk.dai
     * @Date:19:05 2018/6/1 0001
     */
    @Override
    public String getS_pappid(String appid, String secret) {
        String url = "https://api.weixin.qq.com/card/invoice/seturl?access_token=" +weChatService.getAccessToken(appid, secret);
        String token = HttpRequestUtils.httpPost(url,"{}");
        log.info("获取s_pappidJson"+token);
        JSONObject jsonObject = JSONObject.parseObject(token);
        String invoice_url = jsonObject.getString("invoice_url");
        String s_pappid = invoice_url.split("&")[1].split("=")[1];
        return s_pappid;
    }

    /**
     * @Description:设置商户联系方式
     * @Author:disvenk.dai
     * @Date:20:04 2018/6/1 0001
     * param:联系方式
     * param:开票超时时间
     */
    @Override
    public void setPhone(String tel, Integer time,String appid, String secret) throws Exception {
        log.info("商户联系方式："+tel);
        String jsonStr =
                "{ \n" +
                        "\"contact\":{ \"phone\":\n" + tel + "," +
                        "\"time_out\":\n" + time +
                        "}\n" +
                        "}";
        String s = HttpRequestUtils.httpPost("https://api.weixin.qq.com/card/invoice/setbizattr?action=set_contact&access_token=" + weChatService.getAccessToken(appid, secret), jsonStr);
        log.info("设置商户联系方式json"+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String errmsg = jsonObject.getString("errmsg");
        if(!"ok".equals(errmsg)){
            log.error("设置商户联系方式失败");
            throw new RuntimeException("设置商户联系方式失败");
        }
    }

    /**
     * @Description:获取授权页ticket
     * @Author:disvenk.dai
     * @Date:19:59 2018/6/1 0001
     */
    @Override
    public String getTicket(String appid, String secret) {
        String s = HttpRequestUtils.httpGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" +  weChatService.getAccessToken(appid, secret) + "&type=wx_card&offset_type=1");
        log.info("获取授权页ticketJson"+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String errmsg = jsonObject.getString("errmsg");
        if(!"ok".equals(errmsg)){
            log.error("获取授权页ticket失败");
            throw new RuntimeException("获取授权页ticket失败");
        }
        String ticket = jsonObject.getString("ticket");
        return ticket;
    }

    @Override
    public String writeReceipt(String appId,String appSecret,String json) {
        String s = HttpRequestUtils.httpPost("https://api.weixin.qq.com/card/invoice/makeoutinvoice?access_token=" + weChatService.getAccessToken(appId, appSecret), json);
        return s;
    }


}

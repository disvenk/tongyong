package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.web.model.ElectronicTicketConfig;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.*;
import com.resto.shop.web.dao.ReceiptMapper;
import com.resto.shop.web.model.Receipt;
import com.resto.shop.web.service.OrderPaymentItemService;
import com.resto.shop.web.service.ReceiptService;
import com.resto.shop.web.service.WeChatReceiptService;
import com.resto.shop.web.util.HttpRequestUtils;
import com.resto.shop.web.util.SejsUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by disvenk.dai on 2018-09-19 11:12
 */
@Component
@Service
public class WeChatReceiptServiceImpl  implements WeChatReceiptService {

    public Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WeChatService weChatService;

    @Autowired
    WechatFaPiaoService wechatFaPiaoService;

    @Autowired
    WechatConfigService wechatConfigService;

    @Autowired
    ElectronicTicketConfigService electronicTicketConfigService;

    @Autowired
    OrderPaymentItemService orderPaymentItemService;

    @Autowired
    BrandService brandService;

    @Autowired
    ReceiptMapper receiptMapper;

    @Override
    public String getAuthUrl(Receipt receipt,String brandId, String wechatConfigId, String orderId, String orderNum, Integer type) throws Exception {
        WechatConfig wechatConfig = wechatConfigService.selectById(wechatConfigId);

        ElectronicTicketConfig config = electronicTicketConfigService.selectByBrandId(brandId);

        BigDecimal money = orderPaymentItemService.selectEnableTicketMoney(orderId);
        log.info("可开票金额"+money);
        if(money==null || money.compareTo(BigDecimal.ZERO)==0){
            log.error("可开票金额非法");
            throw new RuntimeException("可开票金额非法");
        }

        //设置商户联系方式，获取授权页之前必须设置
        wechatFaPiaoService.setPhone(config.getPayeePhone(),100000,wechatConfig.getAppid(),wechatConfig.getAppsecret());//开票之前要设置商户联系方式

        JSONObject json = new JSONObject();
        json.put("s_pappid", wechatFaPiaoService.getS_pappid(wechatConfig.getAppid(),wechatConfig.getAppsecret()));//开票平台在微信的标识号，商户需要找开票平台提供
        json.put("order_id",orderNum);//订单id，在商户内单笔开票请求的唯一识别号，
        log.info("订单号流水号"+orderNum);
        json.put("money",money.multiply(BigDecimal.valueOf(100)));//订单金额，以分为单位
        json.put("timestamp", System.currentTimeMillis()/1000);//	时间戳
        json.put("source","web");//开票来源，app：app开票，web：微信h5开票，wxa：小程序开发票，wap：普通网页开票
        json.put("redirect_url"," http://7dfaa259.ngrok.io/wechat/index/index");//授权成功后跳转页面。本字段只有在source为H5的时候需要填写，引导用户在微信中进行下一步流程
        json.put("ticket",wechatFaPiaoService.getTicket(wechatConfig.getAppid(),wechatConfig.getAppsecret()));//授权页ticket
        json.put("type",type);//授权类型，0：开票授权，1：填写字段开票授权，2：领票授权

        String authUrl = wechatFaPiaoService.getAuthUrl(wechatConfig.getAppid(), wechatConfig.getAppsecret(), json.toJSONString());

        receipt.setPayeeRegisterNo(config.getPayeeRegisterNo());
        String dateStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS");

       String serialFix = config.getWechatPayNum().substring(6);
        log.info("商户号"+config.getWechatPayNum());
        log.info("流水号"+serialFix+dateStr);
        receipt.setSerialNo(wechatConfig.getMchid()+dateStr);
        receipt.setTicketType(1);
        receipt.setState(0);
        receipt.setCreateTime(new Date());
        receiptMapper.insert(receipt);
        return authUrl;
    }

    /**
     *@Description:开蓝票
     *@Author:disvenk.dai
     *@Date:16:20 2018/6/2 0002
     */
    @Override
    public String blueTicket(String brandId,String serialNum,BigDecimal money,String openId,String appid, String secret){

        ElectronicTicketConfig config = electronicTicketConfigService.selectByBrandId(brandId);

        //查询授权完成的订单
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("order_id", serialNum);
        jsonObject1.put("s_pappid", wechatFaPiaoService.getS_pappid(appid,secret));

        String json = HttpRequestUtils.httpPost("https://api.weixin.qq.com/card/invoice/getauthdata?access_token=" + weChatService.getAccessToken(appid, secret), jsonObject1.toJSONString());
        log.info("查询授权完成的订单json"+json);
        JSONObject obj = JSONObject.parseObject(json);
        String errmsg = obj.getString("errmsg");
        if(!"ok".equals(errmsg)){
            log.error("获取授权完成状态失败");
            throw new RuntimeException("获取授权完成状态失败");
        }
        JSONObject user_auth_info = obj.getJSONObject("user_auth_info");
        JSONObject biz_field = user_auth_info.getJSONObject("biz_field");
        String title = biz_field.getString("title");//购货方抬头

        //税额计算工具
        Map<String, String> sejs = SejsUtil.sejs(money);

        String jshj = money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

        JSONObject jsonObject = new JSONObject();
        JSONObject invoiceinfo = new JSONObject();
        invoiceinfo.put("wxopenid",openId);//用户的openid 用户知道是谁在开票
        invoiceinfo.put("ddh",serialNum);//订单号，企业自己内部的订单号码,（订单号）需要和拉起授权页时的order_id保持一致，否则会出现未授权订单号的报错
        String dateStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSSS");


        log.info("流水号"+"135065"+dateStr);
        invoiceinfo.put("fpqqlsh", "135065"+dateStr);//发票请求流水号，1350659001唯一识别开票请求的流水号,为开票的唯一标识，头六位需要和后台的商户识别号保持一致
        invoiceinfo.put("nsrsbh",config.getPayeeRegisterNo());//纳税人识别码
        invoiceinfo.put("nsrmc",config.getName());//纳税人名称
        invoiceinfo.put("nsrdz",config.getAddress());//纳税人地址
        invoiceinfo.put("nsrdh",config.getPayeePhone());//纳税人电话
        invoiceinfo.put("nsrbank","");//纳税人开户行
        invoiceinfo.put("nsrbankid","");//纳税人银行账号
        invoiceinfo.put("ghfmc",title);//购货方名称,消费者
        invoiceinfo.put("kpr",config.getOperator());//	开票人
        invoiceinfo.put("jshj",jshj.toString());//价税合计
        invoiceinfo.put("hjje",sejs.get("hjje"));//合计金额
        invoiceinfo.put("hjse",sejs.get("hjse"));//合计税额

        List<JSONObject> list = new ArrayList<>();
        JSONObject invoicedetail = new JSONObject();
        invoicedetail.put("fphxz","0");//发票行性质 0 正常 1折扣 2 被折扣
        invoicedetail.put("spbm","3070401000000000000");//19位税收分类编码
        invoicedetail.put("xmmc","餐饮费");//项目名称
        invoicedetail.put("xmsl","1");//项目数量
        invoicedetail.put("xmdj",sejs.get("hjje"));//项目单价
        invoicedetail.put("xmje",sejs.get("hjje"));//项目金额 不含税，单位元 两位小数
        invoicedetail.put("sl","0.06");//税率 精确到两位小数 如0.01
        invoicedetail.put("se", sejs.get("hjse"));//税额 单位元 两位小数
        list.add(invoicedetail);

        invoiceinfo.put("invoicedetail_list",list);//发票行项目数据

        jsonObject.put("invoiceinfo",invoiceinfo);

        String s = wechatFaPiaoService.writeReceipt(appid,secret, jsonObject.toJSONString());
        log.info("开票完成后json"+s);
        JSONObject result = JSONObject.parseObject(s);
        String success = result.getString("errmsg");
        if(!"sucesss".equals(success)){
            log.error("开票失败,请联系商家");
            throw new RuntimeException("开票失败,请联系商家");
        }
        return success;
    }

    @Override
    public int updateReceipt(String serialNo) {
        return receiptMapper.updateStatusById(serialNo);
    }

}

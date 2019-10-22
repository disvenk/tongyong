package com.resto.shop.web.aspect;

import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.WaitModerState;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.GetNumber;
import com.resto.shop.web.model.TemplateSytle;
import com.resto.shop.web.service.CustomerService;
import com.resto.shop.web.service.GetNumberService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 * Created by carl on 2016/10/16.
 */
@Component
@Aspect
public class GetNumberAspect {

    Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private BrandSettingService brandSettingService;

    @Resource
    private CustomerService customerService;

    @Resource
    private WechatConfigService wechatConfigService;

    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private GetNumberService getNumberService;

    @Resource
    private BrandService brandService;

    @Resource
    TemplateService templateService;

    @Autowired
    WeChatService weChatService;

    @Resource
    private BrandTemplateEditService brandTemplateEditService;

    @Pointcut("execution(* com.resto.shop.web.service.GetNumberService.updateGetNumber(..))")
    public void updateGetNumber(){};

    @AfterReturning(value = "updateGetNumber()", returning = "getNumber")
    public void updateGetNumberAfter(GetNumber getNumber) {
//        Customer customer = null;
//        WechatConfig config = null;
//        BrandSetting setting = null;
        if(getNumber.getCustomerId() != null){
            ShopDetail shop = shopDetailService.selectByPrimaryKey(getNumber.getShopDetailId());
            Customer customer = customerService.selectById(getNumber.getCustomerId());
            WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
            BrandSetting setting = brandSettingService.selectByBrandId(customer.getBrandId());
            if(getNumber.getState().equals(WaitModerState.WAIT_MODEL_NUMBER_ZERO) ){
            	log.info("发送叫号提示");
                if(setting.getTemplateEdition()==0){
                    StringBuffer msg = new StringBuffer();
    //                msg.append(customer.getNickname() + "，请至餐厅就餐，您一共获得" + getNumber.getFinalMoney().setScale(2,   BigDecimal.ROUND_HALF_UP) + "元的等位红包。\n");
                    msg.append(shop.getWaitJiaohao());
                    weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", setting.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }else{
                    List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM206094658");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM206094658",TemplateSytle.READY_EAT);
                    if(templateFlowList!=null&&templateFlowList.size()!=0){
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl ="";
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "你好！请前往就餐！");
                            }
                        }else {
                            first.put("value", "你好！请前往就餐！");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shop.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", getNumber.getCodeValue());
                        keyword2.put("color", "#000000");
                        //获取此getNumber取号单前方还有多少位等位桌数
                        List<GetNumber> getNumberList = getNumberService.selectBeforeNumberByCodeId(shop.getId(), getNumber.getCodeId(), getNumber.getCreateTime());
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        keyword3.put("value", getNumberList.size());
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "--");
                        keyword4.put("color", "#000000");
                        Map<String, Object> keyword5 = new HashMap<String, Object>();
                        keyword5.put("value", "叫号中");
                        keyword5.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", shop.getWaitJiaohao());
                            }
                        }else {
                            remark.put("value", shop.getWaitJiaohao());
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("keyword5", keyword5);
                        content.put("remark", remark);
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);

                        //发送短信
                        if(setting.getMessageSwitch()==1){
                            com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                            com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_109465280");
                        }
                    }else{
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
            } else if(getNumber.getState().equals(WaitModerState.WAIT_MODEL_NUMBER_ONE) ) {
            	log.info("发送就餐提示");
                if(setting.getTemplateEdition()==0){
                    StringBuffer msg = new StringBuffer();
    //                msg.append("亲，您一共获得"+getNumber.getFinalMoney().setScale(2,   BigDecimal.ROUND_HALF_UP)+"元等位红包，红包金额在本次消费中将直接使用哦。\n");
    //                msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?subpage=tangshi&shopId=" + getNumber.getShopDetailId() + " '>立即点餐</a>");
                    msg.append(shop.getWaitJiucan() + "\n");
                    msg.append("<a href='" + setting.getWechatWelcomeUrl() + "?dialog=waitScan'>点击此处，扫一扫桌位二维码，开启美食之旅！</a>");
                    weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", setting.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }else{
                    List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM206094658");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM206094658",TemplateSytle.SIT_DOWM_EAT);
                    if(templateFlowList!=null&&templateFlowList.size()!=0){
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl =setting.getWechatWelcomeUrl() + "?dialog=waitScan";
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", shop.getWaitJiucan());
                            }
                        }else {
                            first.put("value", shop.getWaitJiucan());
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shop.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", getNumber.getCodeValue());
                        keyword2.put("color", "#000000");
                        //获取此getNumber取号单前方还有多少位等位桌数
                        List<GetNumber> getNumberList = getNumberService.selectBeforeNumberByCodeId(shop.getId(), getNumber.getCodeId(), getNumber.getCreateTime());
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        keyword3.put("value", getNumberList.size());
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "--");
                        keyword4.put("color", "#000000");
                        Map<String, Object> keyword5 = new HashMap<String, Object>();
                        keyword5.put("value", "已就餐");
                        keyword5.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", "点击此处，扫一扫桌位二维码，开启美食之旅");
                            }
                        }else {
                            remark.put("value", "点击此处，扫一扫桌位二维码，开启美食之旅");
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("keyword5", keyword5);
                        content.put("remark", remark);
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);

                        //发送短信
                        if(setting.getMessageSwitch()==1){
                            com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                            com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_109530262");
                        }
                    }else{
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
            } else if(getNumber.getState().equals(WaitModerState.WAIT_MODEL_NUMBER_TWO)) {
            	log.info("发送过号提示");
                if(setting.getTemplateEdition()==0){
                    StringBuffer msg = new StringBuffer();
    //                msg.append(customer.getNickname() + "已过号，谢谢您的支持与谅解，" + getNumber.getFinalMoney().setScale(2,   BigDecimal.ROUND_HALF_UP) + "元等位红包已失效，期待您的下次光临。\n");
                    msg.append(shop.getWaitGuohao());
                    weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", setting.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }else{
                    List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM206094658");
                    BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM206094658",TemplateSytle.MISS_NUMBER);
                    if(templateFlowList!=null&&templateFlowList.size()!=0){
                        String templateId = templateFlowList.get(0).getTemplateId();
                        String jumpUrl ="";
                        Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                        Map<String, Object> first = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                first.put("value", brandTemplateEdit.getStartSign());
                            }else {
                                first.put("value", "您的号码已过号，欢迎下次再来！");
                            }
                        }else {
                            first.put("value", "您的号码已过号，欢迎下次再来！");
                        }

                        first.put("color", "#00DB00");
                        Map<String, Object> keyword1 = new HashMap<String, Object>();
                        keyword1.put("value", shop.getName());
                        keyword1.put("color", "#000000");
                        Map<String, Object> keyword2 = new HashMap<String, Object>();
                        keyword2.put("value", getNumber.getCodeValue());
                        keyword2.put("color", "#000000");
                        //获取此getNumber取号单前方还有多少位等位桌数
                        List<GetNumber> getNumberList = getNumberService.selectBeforeNumberByCodeId(shop.getId(), getNumber.getCodeId(), getNumber.getCreateTime());
                        Map<String, Object> keyword3 = new HashMap<String, Object>();
                        keyword3.put("value", getNumberList.size());
                        keyword3.put("color", "#000000");
                        Map<String, Object> keyword4 = new HashMap<String, Object>();
                        keyword4.put("value", "--");
                        keyword4.put("color", "#000000");
                        Map<String, Object> keyword5 = new HashMap<String, Object>();
                        keyword5.put("value", "已过号");
                        keyword5.put("color", "#000000");
                        Map<String, Object> remark = new HashMap<String, Object>();
                        if(brandTemplateEdit!=null){
                            if(brandTemplateEdit.getBigOpen()){
                                remark.put("value", brandTemplateEdit.getEndSign());
                            }else {
                                remark.put("value", shop.getWaitGuohao());
                            }
                        }else {
                            remark.put("value", shop.getWaitGuohao());
                        }

                        remark.put("color", "#173177");
                        content.put("first", first);
                        content.put("keyword1", keyword1);
                        content.put("keyword2", keyword2);
                        content.put("keyword3", keyword3);
                        content.put("keyword4", keyword4);
                        content.put("keyword5", keyword5);
                        content.put("remark", remark);
                        String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);

                        //发送短信
                        if(setting.getMessageSwitch()==1){
                            com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                            com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_109350263");
                        }
                    }else{
                        Brand brand = brandService.selectById(customer.getBrandId());
                        Map map = new HashMap(4);
                        map.put("brandName", brand.getBrandName());
                        map.put("fileName", customer.getId());
                        map.put("type", "UserAction");
                        map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                        doPostAnsc(map);
                    }
                }
            }

            if(shop.getWaitRemindSwitch() == 1 && shop.getWaitRemindNumber() > 0 &&
                    (getNumber.getState().equals(WaitModerState.WAIT_MODEL_NUMBER_ONE) || getNumber.getState().equals(WaitModerState.WAIT_MODEL_NUMBER_TWO))){
                List<GetNumber> getNumberList = getNumberService.selectAfterNumberByCodeId(getNumber.getShopDetailId(), getNumber.getCodeId(), getNumber.getCreateTime());
                if(getNumberList.size() > (shop.getWaitRemindNumber() - 1) && (getNumberList.size() + 1) >= shop.getWaitRemindNumber()){
                    try {
                        GetNumber gn = getNumberList.get(shop.getWaitRemindNumber());
                        Customer c = customerService.selectById(gn.getCustomerId());
                        StringBuffer msg = new StringBuffer();
                        if(setting.getTemplateEdition()==0){
                            msg.append(shop.getWaitRemindText());
                            weChatService.sendCustomerMsg(msg.toString(), c.getWechatId(), config.getAppid(), config.getAppsecret());
                        }else{
                            List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM206094658");
                            BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM206094658",TemplateSytle.READY_EAT);
                            if(templateFlowList!=null&&templateFlowList.size()!=0){
                                String templateId = templateFlowList.get(0).getTemplateId();
                                String jumpUrl ="";
                                Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
                                Map<String, Object> first = new HashMap<String, Object>();
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        first.put("value", brandTemplateEdit.getStartSign());
                                    }else {
                                        first.put("value", "你好！请前往就餐！");
                                    }
                                }else {
                                    first.put("value", "你好！请前往就餐！");
                                }

                                first.put("color", "#00DB00");
                                Map<String, Object> keyword1 = new HashMap<String, Object>();
                                keyword1.put("value", shop.getName());
                                keyword1.put("color", "#000000");
                                Map<String, Object> keyword2 = new HashMap<String, Object>();
                                keyword2.put("value", gn.getCodeValue());
                                keyword2.put("color", "#000000");
                                //获取此getNumber取号单前方还有多少位等位桌数
                                //List<GetNumber> getNumberList = getNumberService.selectBeforeNumberByCodeId(shop.getId(), getNumber.getCodeId(), getNumber.getCreateTime());
                                Map<String, Object> keyword3 = new HashMap<String, Object>();
                                keyword3.put("value", getNumberList.size() - 1);
                                keyword3.put("color", "#000000");
                                Map<String, Object> keyword4 = new HashMap<String, Object>();
                                keyword4.put("value", "--");
                                keyword4.put("color", "#000000");
                                Map<String, Object> keyword5 = new HashMap<String, Object>();
                                keyword5.put("value", "排队中");
                                keyword5.put("color", "#000000");
                                Map<String, Object> remark = new HashMap<String, Object>();
                                if(brandTemplateEdit!=null){
                                    if(brandTemplateEdit.getBigOpen()){
                                        remark.put("value", brandTemplateEdit.getEndSign());
                                    }else {
                                        remark.put("value", shop.getWaitRemindText());
                                    }
                                }else {
                                    remark.put("value", shop.getWaitRemindText());
                                }

                                remark.put("color", "#173177");
                                content.put("first", first);
                                content.put("keyword1", keyword1);
                                content.put("keyword2", keyword2);
                                content.put("keyword3", keyword3);
                                content.put("keyword4", keyword4);
                                content.put("keyword5", keyword5);
                                content.put("remark", remark);
                                String result = weChatService.sendTemplate(c.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
                                //发送短信
                                if(setting.getMessageSwitch()==1){
                                    com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
                                    com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(c.getTelephone(),smsParam,"餐加","SMS_109365264");
                                }
                            }else{
                                Brand brand = brandService.selectById(customer.getBrandId());
                                Map map = new HashMap(4);
                                map.put("brandName", brand.getBrandName());
                                map.put("fileName", customer.getId());
                                map.put("type", "UserAction");
                                map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
                                doPostAnsc(map);
                            }
                        }
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }
}

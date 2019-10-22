package com.resto.shop.web.aspect;

import com.resto.api.appraise.entity.*;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraiseComment;
import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.api.appraise.service.NewAppraiseCommentService;
import com.resto.api.appraise.service.NewAppraiseNewService;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.brand.core.util.LogUtils;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.AppraiseNewService;
import com.resto.shop.web.service.AppraiseService;
import com.resto.shop.web.service.CustomerService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 * Created by carl on 2016/11/22.
 */
@Component
@Aspect
public class AppraiseAspect {

    Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private CustomerService customerService;
    @Resource
    private NewAppraiseService newAppraiseService;
    @Resource
    private WechatConfigService wechatConfigService;
    @Resource
    private ShopDetailService shopDetailService;
    @Resource
    private BrandService brandService;
    @Resource
    private NewAppraiseNewService newAppraiseNewService;

    @Autowired
    WeChatService weChatService;

    @Pointcut("execution(* com.resto.api.appraise.service.NewAppraisePraiseService.updateCancelPraise(..))")
    public void updateCancelPraise(){};

    @AfterReturning(value = "updateCancelPraise()", returning = "appraisePraise")
    public void updateCancelPraiseAfter(AppraisePraise appraisePraise) {
        if(appraisePraise.getIsDel() == 0){
            Appraise appraise = newAppraiseService.dbSelectByPrimaryKey(appraisePraise.getBrandId(),appraisePraise.getAppraiseId());
            if(appraise!=null){
                if(!appraise.getCustomerId().equals(appraisePraise.getCustomerId())){
                    Customer aCustomer = customerService.selectById(appraise.getCustomerId());
                    ShopDetail shopDetail = shopDetailService.selectById(appraise.getShopDetailId());
                    Brand brand = brandService.selectById(aCustomer.getBrandId());
                    Customer customer = customerService.selectById(appraisePraise.getCustomerId());
                    WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
                    StringBuffer msg = new StringBuffer();
                    String url = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/appraise?appraiseId=" + appraise.getId() + "&baseUrl=" + "http://" + brand.getBrandSign() + ".restoplus.cn";
                    if(customer.getSex() == 1){
                        msg.append(customer.getNickname() + "为你在" + shopDetail.getName() + "写的评论点了赞，\n<a href='" + url+ "'>快去回复他吧~</a>");
                    }else if(customer.getSex() == 2){
                        msg.append(customer.getNickname() + "为你在" + shopDetail.getName() + "写的评论点了赞，\n<a href='" + url+ "'>快去回复她吧~</a>");
                    }else{
                        msg.append(customer.getNickname() + "为你在" + shopDetail.getName() + "写的评论点了赞，\n<a href='" + url+ "'>快去回复TA吧~</a>");
                    }
                    weChatService.sendCustomerMsg(msg.toString(), aCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }else{
                AppraiseNew appraiseNew = newAppraiseNewService.dbSelectByPrimaryKey(appraisePraise.getBrandId(),appraisePraise.getAppraiseId());
                if(!appraiseNew.getCustomerId().equals(appraisePraise.getCustomerId())){
                    Customer aCustomer = customerService.selectById(appraiseNew.getCustomerId());
                    ShopDetail shopDetail = shopDetailService.selectById(appraiseNew.getShopDetailId());
                    Brand brand = brandService.selectById(aCustomer.getBrandId());
                    Customer customer = customerService.selectById(appraisePraise.getCustomerId());
                    WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
                    StringBuffer msg = new StringBuffer();
                    String url = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/appraise?appraiseId=" + appraiseNew.getId() + "&baseUrl=" + "http://" + brand.getBrandSign() + ".restoplus.cn";
                    if(customer.getSex() == 1){
                        msg.append(customer.getNickname() + "为你在" + shopDetail.getName() + "写的评论点了赞，\n<a href='" + url+ "'>快去回复他吧~</a>");
                    }else if(customer.getSex() == 2){
                        msg.append(customer.getNickname() + "为你在" + shopDetail.getName() + "写的评论点了赞，\n<a href='" + url+ "'>快去回复她吧~</a>");
                    }else{
                        msg.append(customer.getNickname() + "为你在" + shopDetail.getName() + "写的评论点了赞，\n<a href='" + url+ "'>快去回复TA吧~</a>");
                    }
                    weChatService.sendCustomerMsg(msg.toString(), aCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
                    Map map = new HashMap(4);
                    map.put("brandName", brand.getBrandName());
                    map.put("fileName", customer.getId());
                    map.put("type", "UserAction");
                    map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                    doPostAnsc(map);
                }
            }
        }
    }

    @Pointcut("execution(* com.resto.api.appraise.service.NewAppraiseCommentService.insertComment(..))")
    public void insertComment(){};

    @AfterReturning(value = "insertComment()", returning = "appraiseComment")
    public void insertCommentAfter(AppraiseComment appraiseComment) {
        Customer appCustomer = new Customer();
        ShopDetail shopDetail = new ShopDetail();
        AppraiseNew appraiseNew=new  AppraiseNew();
        Appraise appraise = newAppraiseService.dbSelectByPrimaryKey(appraiseComment.getBrandId(),appraiseComment.getAppraiseId());
        if(appraise!=null){
            appCustomer=customerService.selectById(appraise.getCustomerId());
            shopDetail=shopDetailService.selectById(appraise.getShopDetailId());
        }else{
            appraiseNew = newAppraiseNewService.dbSelectByPrimaryKey(appraiseComment.getBrandId(),appraiseComment.getAppraiseId());
            appCustomer=customerService.selectById(appraiseNew.getCustomerId());
            shopDetail=shopDetailService.selectById(appraiseNew.getShopDetailId());
        }
        Brand brand = brandService.selectById(appCustomer.getBrandId());
        Customer customer = customerService.selectById(appraiseComment.getCustomerId());
        WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
        StringBuffer msg = new StringBuffer();
        String url ="";
        if(appraise!=null&&appraise.getId()!=null){
            url = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/appraise?appraiseId=" + appraise.getId() + "&baseUrl=" + "http://" + brand.getBrandSign() + ".restoplus.cn";
        }else{
            url = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/appraise?appraiseId=" + appraiseNew.getId() + "&baseUrl=" + "http://" + brand.getBrandSign() + ".restoplus.cn";
        }
        if(customer.getSex() == 1){
            msg.append(customer.getNickname() + "回复了您在" + shopDetail.getName() + "写的评论，" + customer.getNickname() + "说：\"" + appraiseComment.getContent() + "\"\n<a href='" + url+ "'>快去回复他吧~</a>");
        }else if(customer.getSex() == 2){
            msg.append(customer.getNickname() + "回复了您在" + shopDetail.getName() + "写的评论，" + customer.getNickname() + "说：\"" + appraiseComment.getContent() + "\"\n<a href='" + url+ "'>快去回复她吧~</a>");
        }else{
            msg.append(customer.getNickname() + "回复了您在" + shopDetail.getName() + "写的评论，" + customer.getNickname() + "说：\"" + appraiseComment.getContent() + "\"\n<a href='" + url+ "'>快去回复TA吧~</a>");
        }
        if(appraise!=null&&appraise.getCustomerId()!=null){
            if(!appraise.getCustomerId().equals(appraiseComment.getCustomerId())){
                weChatService.sendCustomerMsg(msg.toString(), appCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            }
        }else{
            if(!appraiseNew.getCustomerId().equals(appraiseComment.getCustomerId())){
                weChatService.sendCustomerMsg(msg.toString(), appCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            }
        }
        //继续发送给你回复的人
        if(appraiseComment.getPid() != null && appraiseComment.getPid().length() > 30){
            Customer faCustomer = customerService.selectById(appraiseComment.getPid());
            if(!faCustomer.getWechatId().equals(appCustomer.getWechatId())){
                weChatService.sendCustomerMsg(msg.toString(), faCustomer.getWechatId(), config.getAppid(), config.getAppsecret());
                Map map = new HashMap(4);
                map.put("brandName", brand.getBrandName());
                map.put("fileName", customer.getId());
                map.put("type", "UserAction");
                map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
                doPostAnsc(map);
            }
        }
    }
}

package com.resto.shop.web.aspect;

import com.alibaba.druid.util.StringUtils;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.BehaviorType;
import com.resto.brand.core.enums.DetailType;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.model.Coupon;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.Order;
import com.resto.shop.web.model.TemplateSytle;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.BrandAccountSendUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

@Component
@Aspect
public class BindPhoneAspect {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	CustomerService customerService;
	@Resource
	NewCustomerService newCustomerService;
	@Resource
	NewCustomCouponService newCustomerCouponService;
	@Resource
	CouponService couponService;
	@Resource
	ShareSettingService shareSettingService;
	@Resource
	SmsLogService smsLogService;
	@Resource
	ShopDetailService shopDetailService;
    @Resource
    BrandService brandService;
	@Resource
	WechatConfigService wechatConfigService;

	@Autowired
	MQMessageProducer mqMessageProducer;

	@Resource
	BrandSettingService brandSettingService;

	@Resource
	BrandAccountService brandAccountService;

	@Resource
	BrandAccountLogService brandAccountLogService;

	@Resource
	AccountSettingService accountSettingService;

	@Resource
	AccountNoticeService accountNoticeService;

	@Resource
	TemplateService templateService;

	@Resource
	OrderService orderService;

	@Resource
	BrandTemplateEditService brandTemplateEditService;

	@Autowired
	WeChatService weChatService;

	@Pointcut("execution(* com.resto.shop.web.service.CustomerService.bindPhone(..))")
	public void bindPhone(){};

	@Around("bindPhone()")
	public Object bindPhoneAround(ProceedingJoinPoint pj) throws Throwable{
		String customerId = (String) pj.getArgs()[1];
		Integer couponType = (Integer) pj.getArgs()[2];
        String shopId = (String) pj.getArgs()[3];
		String shareCustomer = (String) pj.getArgs()[4];
		String shareOrderId = (String) pj.getArgs()[5];
		if(customerId.equals(shareCustomer)){
			shareCustomer = null;
		}
		ShopDetail shopDetail = shopDetailService.selectById(shopId);
		com.resto.api.customer.entity.Customer  cus = newCustomerService.dbSelectByPrimaryKey(shopDetail.getBrandId(),customerId);
		boolean isFirstBind = !cus.getIsBindPhone();
		Object obj = pj.proceed();
		Brand brand = brandService.selectById(cus.getBrandId());
		log.info("当前用户注册的状态" + !isFirstBind);
		if(isFirstBind){
			log.info("进入用户注册发放优惠券" + isFirstBind);
			newCustomerCouponService.giftCoupon(cus,couponType,shopId);
			//如果有分享者，那么给分享者发消息
//			if(!StringUtils.isEmpty(cus.getShareCustomer())){
			if(!StringUtils.isEmpty(shareCustomer)){
//				mqMessageProducer.sendNoticeShareMessage(cus);
				Customer sc = customerService.selectById(shareCustomer);
				ShareSetting shareSetting = shareSettingService.selectValidSettingByBrandId(cus.getBrandId());
				BigDecimal sum = new BigDecimal(0);
				List<Coupon> couponList = new ArrayList<>();
				//品牌专属优惠券
				List<Coupon> couponList1 = couponService.listCouponByStatus("0", cus.getId(),cus.getBrandId(),null);
				couponList.addAll(couponList1);
				List<ShopDetail> listShop = shopDetailService.selectByBrandId(cus.getBrandId());
				for(ShopDetail s : listShop){
					//店铺专属优惠券
					List<Coupon> couponList2 = couponService.listCouponByStatus("0", cus.getId(),null,s.getId());
					couponList.addAll(couponList2);
				}
				for (Coupon coupon : couponList) {
					sum = sum.add(coupon.getValue());
				}

				WechatConfig config = wechatConfigService.selectByBrandId(cus.getBrandId());
				StringBuffer msg = new StringBuffer("亲，感谢您的分享，您的好友");
				if(shareSetting == null){
					msg.append(cus.getNickname()).append("已领取").append(sum).append("元红包，")
							.append(cus.getNickname()).append("如到店消费您将获得红包返利");
				}else{
					msg.append(cus.getNickname()).append("已领取").append(sum).append("元红包，")
							.append(cus.getNickname()).append("如到店消费您将获得").append(shareSetting.getMinMoney())
							.append("-").append(shareSetting.getMaxMoney()).append("元红包返利");
				}

				log.info("异步发送分享注册微信通知ID:" + shareCustomer + " 内容:" + msg);
				BrandSetting setting = brandSettingService.selectByBrandId(cus.getBrandId());
				if(setting.getTemplateEdition()==0){
					weChatService.sendCustomerMsg(msg.toString(), sc.getWechatId(), config.getAppid(), config.getAppsecret());
					Map map = new HashMap(4);
					map.put("brandName", brand.getBrandName());
					map.put("fileName", sc.getId());
					map.put("type", "UserAction");
					map.put("content", "系统向用户:"+sc.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
					doPostAnsc( map);
				}else{
					log.info("发送注册模板消息:订单id"+shareOrderId);
					List<TemplateFlow> templateFlowList=templateService.selectTemplateId(config.getAppid(),"OPENTM207012446");

					BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(config.getAppid(),"OPENTM207012446", TemplateSytle.REGISTRY);
					if(templateFlowList!=null&&templateFlowList.size()!=0){
						String templateId = templateFlowList.get(0).getTemplateId();
						String jumpUrl ="";
						Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
						Map<String, Object> first = new HashMap<String, Object>();

						if(brandTemplateEdit!=null){
							if(brandTemplateEdit.getBigOpen()){
								first.put("value", brandTemplateEdit.getStartSign());
							}else {
								first.put("value", msg.toString());
							}
						}else {
							first.put("value", msg.toString());
						}

						first.put("color", "#00DB00");
						Map<String, Object> keyword1 = new HashMap<String, Object>();
						if(!StringUtils.isEmpty(shareOrderId)){
							Order order=orderService.selectById(shareOrderId);
							keyword1.put("value", order.getSerialNumber());
						}else{
							keyword1.put("value", "-");
						}
						keyword1.put("color", "#000000");
						Map<String, Object> keyword2 = new HashMap<String, Object>();
						if(!StringUtils.isEmpty(shareOrderId)){
							Order order=orderService.selectById(shareOrderId);
							keyword2.put("value",DateUtil.formatDate(order.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
						}else{
							keyword2.put("value","-");
						}
						keyword2.put("color", "#000000");
						Map<String, Object> remark = new HashMap<String, Object>();

						if(brandTemplateEdit!=null){
							if(brandTemplateEdit.getBigOpen()){
								remark.put("value", brandTemplateEdit.getEndSign());
							}else {
								remark.put("value", "立即分享好友");
							}
						}else {
							remark.put("value", "立即分享好友");
						}

						remark.put("color", "#173177");
						content.put("first", first);
						content.put("keyword1", keyword1);
						content.put("keyword2", keyword2);
						content.put("remark", remark);
						String result = weChatService.sendTemplate(sc.getWechatId(), templateId, jumpUrl, content, config.getAppid(), config.getAppsecret());
						Map map = new HashMap(4);
						map.put("brandName", brand.getBrandName());
						map.put("fileName", sc.getId());
						map.put("type", "UserAction");
						map.put("content", "系统向用户:" + sc.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
						doPostAnsc( map);
					}else{
						Map map = new HashMap(4);
						map.put("brandName", brand.getBrandName());
						map.put("fileName", sc.getId());
						map.put("type", "UserAction");
						map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
						doPostAnsc(map);
					}
				}
			}
			//yz 2017/07/28 计费系统 注册收费
			BrandSetting brandSetting = brandSettingService.selectByBrandId(cus.getBrandId());
			if(brandSetting.getOpenBrandAccount()==1){//开启了品牌账户信息
				BrandAccount brandAccount = brandAccountService.selectByBrandId(brand.getId());
				//获取品牌账户设置
				AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
				BigDecimal money = BigDecimal.ZERO;
				if(accountSetting.getOpenNewCustomerRegister()==1){
					money = accountSetting.getNewCustomerValue();
				}

				//品牌剩余的money 不计算剩余 在sql中控制
				//BigDecimal remain = brandAccount.getAccountBalance().subtract(money);
				//更新日志
				BrandAccountLog blog = new BrandAccountLog();
				blog.setCreateTime(new Date());
				blog.setGroupName(brand.getBrandName());
				blog.setBehavior(BehaviorType.REGISTER);
				blog.setFoundChange(money.negate());
				//blog.setRemain(remain);
				blog.setDetail(DetailType.NEW_CUSTOMER_REGISTER);
				blog.setAccountId(brandAccount.getId());
				blog.setShopId(shopId);
				blog.setBrandId(brand.getId());
				blog.setSerialNumber(DateUtil.getRandomSerialNumber());
				//记录 品牌账户更新日志 + 更新账户
//				Integer brandAccountId = brandAccount.getId();
//				brandAccount = new BrandAccount();
//				brandAccount.setId(brandAccountId);
				//brandAccount.setAccountBalance(remain);
				brandAccount.setUpdateTime(new Date());
				brandAccountLogService.updateBrandAccountAndLog(blog,brandAccount.getId(),money);
				List<AccountNotice> noticeList = accountNoticeService.selectByAccountId(brandAccount.getId());
			    Result result =  BrandAccountSendUtil.sendSms(brandAccount,noticeList,brand.getBrandName(),accountSetting);
			    if(result.isSuccess()){
					Long id = accountSetting.getId();
					AccountSetting as = new AccountSetting();
					as.setId(id);
					as.setType(1);
					accountSettingService.update(as);//设置为不可以发短信
					log.info(brand.getBrandName()+"品牌账户余额欠费生产者开始生产欠费消息");
					mqMessageProducer.sendBrandAccountSms(brand.getId(),MQSetting.DELAY_TIME);
				}
			}
			log.info("首次绑定手机，执行指定动作");

		}else{
			log.info("不是首次绑定，无任何动作");
		}
		return obj;
	}

//	@AfterReturning(value = "bindPhone()", returning = "customer")
//	public void bindPhoneAround(JoinPoint jp, Customer customer) throws Throwable{
//		boolean isFirstBind = !customer.getIsBindPhone();
//		Integer couponType = (Integer) jp.getArgs()[2];
//		String shopId = (String) jp.getArgs()[3];
//		if(isFirstBind){
//			newCustomerCouponService.giftCoupon(customer,couponType,shopId);
//			//如果有分享者，那么给分享者发消息
//			if(!StringUtils.isEmpty(customer.getShareCustomer())){
//				mqMessageProducer.sendNoticeShareMessage(customer);
//			}
//			log.info("首次绑定手机，执行指定动作");
//
//		}else{
//			log.info("不是首次绑定，无任何动作");
//		}
//	}

}

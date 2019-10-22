package com.resto.shop.web.aspect;


import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.BehaviorType;
import com.resto.brand.core.enums.DetailType;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.SmsLogType;
import com.resto.shop.web.model.SmsLog;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.service.SmsLogService;
import com.resto.shop.web.util.BrandAccountSendUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by yz 2017/08/17
 *
 * 短信切面 发短信后 扣费的扣费 品牌账户减余额的余额
 */
@Component
@Aspect
public class SendMessageAspect {


	@Resource
	private BrandSettingService brandSettingService;

	@Resource
	private AccountSettingService accountSettingService;

	@Resource
	private AccountNoticeService accountNoticeService;

	@Resource
	private BrandAccountService brandAccountService;

	@Resource
	private BrandService brandService;

	@Resource
	private BrandUserService brandUserService;

	@Resource
	private SmsAcountService smsAcountService;

	@Resource
	private BrandAccountLogService brandAccountLogService;

	@Autowired
	MQMessageProducer mqMessageProducer;

	@Resource
	private SmsLogService smsLogService;

	Logger log = LoggerFactory.getLogger(getClass());


	private void sendNotice(BrandUser brandUser) {
		SmsAcount smsAcount = smsAcountService.selectByBrandId(brandUser.getBrandId());
		//获取短信账户短信提醒
		String str = smsAcount.getSmsRemind();
		String[] arrs = str.split(",");
		//获取商家短信剩余数量
		int remindNum = smsAcount.getRemainderNum();
		//判断是否需要提醒
		if(this.isHave(arrs, remindNum+"")){
			//提醒商家充值
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("num",remindNum);
			jsonObject.put("name",brandUser.getBrandName());
			SMSUtils.sendNoticeToBrand(jsonObject, smsAcount.getSmsNoticeTelephone());
		}

	}


	/**
	 * String数组获取最小值
	 * @param arrs
	 * @return
	 */

	public int getMinStr(String[] arrs){
		if(arrs == null ||arrs.length<=0){
			throw new IllegalArgumentException("空数组无法获取最小值");
		}
		int min = Integer.parseInt(arrs[0]);
		for(int i=0;i<arrs.length;i++){
			int temp = Integer.parseInt(arrs[i]);
			if(min>temp){
				min = temp;
			}
		}
		return min;
	}

	public  boolean isHave(String[] strs,String s){
		  /*此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串
		   * */
		for(int i=0;i<strs.length;i++){
			if(strs[i].indexOf(s)!=-1){//循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;//查找到了就返回真，不在继续查询
			}
		}
		return false;//没找到返回false
	}


	@Pointcut("execution(* com.resto.shop.web.service.SmsLogService.sendMessage(..))")
	public void sendMessage() {
	}

	/**
	 * 发模板短信 之前 判断 1.是否开启品牌账户---开启--判断accountSetting中type=0则 判断账户余额是否在 设置值中 --是 则给商户发短信提醒
	 *
	 *
	 */
//String brandId, String shopId,int smsType, String sign, String code_temp,String phone,JSONObject jsonObject
	@Before(value = "sendMessage()")
	public void sendMessage(JoinPoint jp) throws Throwable {
		log.info("开始发送模板短信----");
		//String telephone, JSONObject sms, String sign, String code_temp,String brandId
		String brandId = (String) jp.getArgs()[0];
		Brand brand = brandService.selectByPrimaryKey(brandId);
		BrandUser brandUser = brandUserService.selectOneByBrandId(brandId);
		BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);
		//如果开启了
		if (brandSetting != null && brandSetting.getOpenBrandAccount() == 1) {
			//判断type是否等于1 type =0代表是需要发短信（账户小于设置则发短信通知） type=1是不需要发短信
			log.info("该品牌开启了品牌账户信息--------");
			//获取品牌账户设置
			AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
			//品牌账户
			BrandAccount brandAccount = brandAccountService.selectByBrandSettingId(brandSetting.getId());

			//短信通知的 梯度
			List<AccountNotice> noticeList = accountNoticeService.selectByAccountId(brandAccount.getId());
			//判断是否需要发短信通知欠费
			Result result = BrandAccountSendUtil.sendSms(brandAccount, noticeList, brand.getBrandName(), accountSetting);
			if (result.isSuccess()) {
				Long id = accountSetting.getId();
				AccountSetting as = new AccountSetting();
				as.setId(id);
				as.setType(1);
				accountSettingService.update(as);
				//发送延时消息 24小时
				log.info("发送欠费消息后把账户设置改为已发送状态,并发送消息队列。。。");
				mqMessageProducer.sendBrandAccountSms(brandId, MQSetting.DELAY_TIME);

			}
		}else { //未开启品牌账户
			log.info("该品牌未开启品牌账户 -- ");
			//判断是否要提醒商家充值短信账户
			sendNotice(brandUser);
		}
	}

	//String brandId, String shopId,int smsType, String sign, String code_temp,String phone,JSONObject jsonObject
	@AfterReturning(value = "sendMessage()",returning = "aliResult")
	public void sendMessage(JoinPoint jp, JSONObject aliResult)throws Throwable{
		String brandId = (String) jp.getArgs()[0];
		String shopId = (String) jp.getArgs()[1];
		int smsType = (int)jp.getArgs()[2];
		String phone = (String)jp.getArgs()[5];
		JSONObject jsonObject = (JSONObject)jp.getArgs()[6];
		String content = "";
		if(smsType== SmsLogType.AUTO_CODE){//如果是验证码
			content = jsonObject.getString("code");
		}else if(smsType==SmsLogType.DAYMESSGAGE){
			content = "日结短信";
		}
		SmsLog smsLog = new SmsLog();
		smsLog.setBrandId(brandId);
		smsLog.setShopDetailId(shopId);
		smsLog.setContent(content);
		smsLog.setSmsType(smsType);
		smsLog.setCreateTime(new Date());
		smsLog.setPhone(phone);
		smsLog.setSmsResult(JSONObject.toJSONString(aliResult));
		smsLog.setIsSuccess(false);

		log.info("短信发送结果:"+ JSONObject.toJSONString(aliResult));

		//后置通知
		try {
			if(aliResult.getBoolean("success")){
				//如果发短发送成功
				smsLog.setIsSuccess(true);
				//成功时记录一条记录
				smsLogService.insert(smsLog);
				BrandSetting brandSetting = brandSettingService.selectByBrandId(brandId);
				//如果开启了
				if (brandSetting != null && brandSetting.getOpenBrandAccount() == 1) {
					//获取品牌账户设置
					AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());
					//品牌账户
					BrandAccount brandAccount = brandAccountService.selectByBrandSettingId(brandSetting.getId());
					Brand brand = brandService.selectByPrimaryKey(brandId);

					//定义每条短信的单价
					BigDecimal sms_unit = BigDecimal.ZERO;
					if(accountSetting.getOpenSendSms()==1){
						sms_unit = accountSetting.getSendSmsValue();
					}
					//剩余账户余额 在sql中控制 以防在扣费的时候 有其它的已经扣费 导致数据不准
					//BigDecimal remain = brandAccount.getAccountBalance().subtract(sms_unit);
					BrandAccountLog blog = new BrandAccountLog();
					blog.setCreateTime(new Date());
					blog.setGroupName(brand.getBrandName());
					blog.setBehavior(BehaviorType.SMS);
					//负数
					blog.setFoundChange(sms_unit.negate());
					//blog.setRemain(remain);//剩余账户余额
					int detailType = 0;
					//如果是验证码
					if(smsType== SmsLogType.AUTO_CODE){
						detailType = DetailType.SMS_CODE;
					}else if(smsType==SmsLogType.DAYMESSGAGE){
						detailType = DetailType.SMS_DAY_MESSAGE;
					}
					blog.setDetail(detailType);
					blog.setAccountId(brandAccount.getId());
					blog.setBrandId(brandId);
					blog.setShopId(shopId);
					//这个流水号目前使用当前时间搓+4位随机字符串
					blog.setSerialNumber(DateUtil.getRandomSerialNumber());
//				Integer accountId = brandAccount.getId();
//				brandAccount = new BrandAccount();
//				brandAccount.setId(accountId);
//				brandAccount.setAccountBalance(remain);
					//记录品牌账户的更新日志 + 更新账户
					brandAccountLogService.updateBrandAccountAndLog(blog,brandAccount.getId(),sms_unit);
				}else {

					//判断短信账户的余额是否充足
					SmsAcount smsAcount = smsAcountService.selectByBrandId(brandId);
					//获取剩余短信条数
					int remindNum = smsAcount.getRemainderNum();
					String [] arrs = smsAcount.getSmsRemind().split(",");
					BrandUser brandUser = brandUserService.selectOneByBrandId(brandId);
					//判断剩余短信条数是否大于设定的最小可发短信值
					if(remindNum<this.getMinStr(arrs)) {
						//我们提醒商家充值
						JSONObject jsonObject2 = new JSONObject();
						jsonObject2.put("num",smsAcount.getRemainderNum());
						jsonObject2.put("name",brandUser.getBrandName());
						SMSUtils.sendNoticeToBrand(jsonObject2,brandUser.getPhone());

					}
					//更新短信账户的信息
					smsAcountService.updateByBrandId(brandId);
				}

			}else {
				//失败时记录一条记录
				smsLogService.insert(smsLog);

			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}



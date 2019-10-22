package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.entity.*;
import com.resto.api.customer.service.NewAccountLogService;
import com.resto.api.customer.service.NewAccountService;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.core.util.SMSUtils;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.AccountLogType;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dao.AccountMapper;
import com.resto.shop.web.dao.ChargeOrderMapper;
import com.resto.shop.web.model.*;
import com.resto.shop.web.model.Account;
import com.resto.shop.web.model.AccountLog;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.LogTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 *
 */
@Component
@Service
public class AccountServiceImpl extends GenericServiceImpl<Account, String> implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    OrderPaymentItemService orderPaymentItemService;
    
    @Resource
    CustomerService customerService;
    
    @Resource
    AccountLogService accountLogService;
    
    @Resource
    ChargeOrderService chargeOrderService;
    
    @Resource
    ChargeOrderMapper chargeOrderMapper;
        
    @Resource
    ChargeLogService chargeLogService;
    
    @Resource
    ChargeSettingService chargeSettingService;
    
    @Resource
    BrandService brandService;

    @Resource
    RedPacketService redPacketService;

    @Resource
    BonusSettingService bonusSettingService;

    @Resource
    BonusLogService bonusLogService;

	@Resource
	TemplateService templateService;

	@Resource
	BrandSettingService brandSettingService;

	@Autowired
	BrandTemplateEditService brandTemplateEditService;

	@Autowired
	WeChatService weChatService;

	@Autowired
	NewAccountService newAccountService;

	@Autowired
	NewAccountLogService newAccountLogService;

	@Autowired
	ShopDetailService shopDetailService;

    @Override
    public GenericDao<Account, String> getDao() {
        return accountMapper;
    }

	@Override
	public BigDecimal useAccount(BigDecimal payMoney, com.resto.api.customer.entity.Account account,Integer source,String shopDetailId) {
    	if(account.getRemain().doubleValue() <= 0 || payMoney.doubleValue() <= 0){
    		throw new RuntimeException("金额为0，异常");
		}
//
//		if(account.getRemain().doubleValue() < payMoney.doubleValue()){
//    		//如果账户余额比 要支付的金额小的话
//			throw new RuntimeException("金额异常，要支付的金额 比 实际金额大");
//		}


//		if(account.getRemain().equals(BigDecimal.ZERO)||payMoney.equals(BigDecimal.ZERO)){
//			return BigDecimal.ZERO;
//		}
		//如果 需要支付的金额大于余额，则扣除所有余额
		BigDecimal useAccountValue = BigDecimal.ZERO;
		if(payMoney.compareTo(account.getRemain())>=0){
			useAccountValue=account.getRemain();
		}else{  //如果 需要支付的金额 小于 余额
			useAccountValue = payMoney;
		}
		account.setRemain(account.getRemain().subtract(useAccountValue));
		String remark= "使用余额:"+useAccountValue+"元";
		addLog(useAccountValue, account, remark, AccountLogType.PAY,source,shopDetailId);
		newAccountService.dbUpdate(shopDetailService.selectById(shopDetailId).getBrandId(),account);
		return useAccountValue;
	}

	@Override
	public void addAccount(BigDecimal value, String accountId, String remark,Integer source,String shopDetailId) {
		com.resto.api.customer.entity.Account account = newAccountService.dbSelectByPrimaryKey(shopDetailService.selectById(shopDetailId).getBrandId(),accountId);
		account.setRemain(account.getRemain().add(value));
		if(value.doubleValue() > 0){
			addLog(value, account, remark, AccountLogType.INCOME,source,shopDetailId);
		}else{
			addLog(new BigDecimal(-1).multiply(value), account, remark, AccountLogType.PAY,source,shopDetailId);
		}
		newAccountService.dbUpdate(shopDetailService.selectById(shopDetailId).getBrandId(),account);
	}

	private void addLog(BigDecimal money,com.resto.api.customer.entity.Account account,String remark,int type,int source,String shopDetailId){
		com.resto.api.customer.entity.AccountLog  acclog = new com.resto.api.customer.entity.AccountLog ();
		acclog.setCreateTime(new Date());
		acclog.setMoney(money);
		acclog.setRemain(account.getRemain());
		acclog.setPaymentType(type);
		acclog.setRemark(remark);
		acclog.setAccountId(account.getId());
		acclog.setSource(source);
        acclog.setShopDetailId(shopDetailId);
		newAccountLogService.dbSave(shopDetailService.selectById(shopDetailId).getBrandId(),acclog);
	}

	@Override
	public Account selectAccountAndLogByCustomerId(String customerId) {
		Account account = accountMapper.selectAccountByCustomerId(customerId);
		if(account!=null){
			List<AccountLog> accountLogs = accountLogService.selectLogsByAccountId(account.getId());
			account.setAccountLogs(accountLogs);
		}
		return account;
	}

	@Override
	public Account createCustomerAccount(Customer cus) {
		Account acc =new Account();
		acc.setId(ApplicationUtils.randomUUID());
		acc.setRemain(BigDecimal.ZERO);
		insert(acc);
		cus.setAccountId(acc.getId());
		customerService.update(cus);
		return acc;
	}

	@Override
	public BigDecimal payOrder(Order order, BigDecimal payMoney, Customer customer, Brand brand, ShopDetail shopDetail) {
		com.resto.api.customer.entity.Account account = newAccountService.dbSelectByPrimaryKey(shopDetailService.selectById(shopDetail.getId()).getBrandId(),customer.getAccountId());  //找到用户帐户
		BigDecimal balance = chargeOrderService.selectTotalBalance(customer.getId()); //获取所有剩余充值金额
		if(balance==null){
			balance = BigDecimal.ZERO;
		}
		//计算剩余红包金额
		BigDecimal redPackageMoney = account.getRemain().subtract(balance);
		BigDecimal realPay = useAccount(payMoney,account,AccountLog.SOURCE_PAYMENT,order.getShopDetailId());  //得出真实支付的值
		//算出 支付比例
		BigDecimal redPay = BigDecimal.ZERO;
		if(realPay.compareTo(BigDecimal.ZERO)>0){ //如果支付金额大于0
			if(redPackageMoney.compareTo(realPay)>=0){ //如果红包金额足够支付所有金额，则只添加红包金额支付项
				redPay = realPay;
			}else{ //如果红包金额不足够支付所有金额，则剩余金额从充值订单里面扣除
				redPay = redPackageMoney;
				BigDecimal remainPay = realPay.subtract(redPay).setScale(2, BigDecimal.ROUND_HALF_UP);  //除去红包后，需要支付的金额
				chargeOrderService.useChargePay(remainPay,customer.getId(),order,brand.getBrandName());
			}
		}
		if(redPay.compareTo(BigDecimal.ZERO)>0){
            redPacketService.useRedPacketPay(redPay,customer.getId(),order,brand,shopDetail);
			OrderPaymentItem item = new OrderPaymentItem();
			item.setId(ApplicationUtils.randomUUID());
			item.setOrderId(order.getId());
			item.setPaymentModeId(PayMode.ACCOUNT_PAY);
			item.setPayTime(new Date());
			item.setPayValue(redPay);
			item.setRemark("余额(红包)支付:" + item.getPayValue());
//			item.setResultData(account.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
			item.setToPayId(account.getId());
			orderPaymentItemService.insert(item);
//            Map map = new HashMap(4);
//            map.put("brandName", brand.getBrandName());
//            map.put("fileName", order.getId());
//            map.put("type", "orderAction");
//            map.put("content", "订单:" + order.getId() + "订单使用余额(红包)支付了:"+item.getPayValue()+",请求服务器地址为:" + MQSetting.getLocalIP());
//            doPostAnsc(url, map);
            LogTemplateUtils.getAccountByOrderType(brand.getBrandName(),order.getId(),item.getPayValue());

//            Map customerMap = new HashMap(4);
//            customerMap.put("brandName", brand.getBrandName());
//            customerMap.put("fileName", order.getCustomerId());
//            customerMap.put("type", "UserAction");
//            customerMap.put("content", "用户:"+customer.getNickname()+"使用余额(红包)支付了:"+item.getPayValue()+"订单Id为:"+order.getId()+",请求服务器地址为:" + MQSetting.getLocalIP());
//            doPostAnsc(url, customerMap);
            LogTemplateUtils.getAccountByUserType(brand.getBrandName(),customer.getId(),customer.getNickname(),item.getPayValue());

		}
		return realPay;
	}

	@Override
	public BigDecimal houFuPayOrder(Order order,BigDecimal payMoney, Customer customer, Brand brand, ShopDetail shopDetail) {
		com.resto.api.customer.entity.Account account = newAccountService.dbSelectByPrimaryKey(shopDetailService.selectById(shopDetail.getId()).getBrandId(),customer.getAccountId());  //找到用户帐户
		BigDecimal balance = chargeOrderService.selectTotalBalance(customer.getId()); //获取所有剩余充值金额
		if(balance==null){
			balance = BigDecimal.ZERO;
		}
		//计算剩余红包金额
		BigDecimal redPackageMoney = account.getRemain().subtract(balance);
		BigDecimal realPay = useAccount(payMoney,account,AccountLog.SOURCE_PAYMENT,order.getShopDetailId());  //得出真实支付的值
		//算出 支付比例
		BigDecimal redPay = BigDecimal.ZERO;
		if(realPay.compareTo(BigDecimal.ZERO)>0){ //如果支付金额大于0
			if(redPackageMoney.compareTo(realPay)>=0){ //如果红包金额足够支付所有金额，则只添加红包金额支付项
				redPay = realPay;
			}else{ //如果红包金额不足够支付所有金额，则剩余金额从充值订单里面扣除
				redPay = redPackageMoney;
				BigDecimal remainPay = realPay.subtract(redPay).setScale(2, BigDecimal.ROUND_HALF_UP);  //除去红包后，需要支付的金额
				chargeOrderService.useChargePay(remainPay,customer.getId(),order,brand.getBrandName());
			}
		}
		if(redPay.compareTo(BigDecimal.ZERO)>0){
            redPacketService.useRedPacketPay(redPay,customer.getId(),order,brand,shopDetail);
			OrderPaymentItem item = new OrderPaymentItem();
			item.setId(ApplicationUtils.randomUUID());
			item.setOrderId(order.getId());
			item.setPaymentModeId(PayMode.ACCOUNT_PAY);
			item.setPayTime(new Date());
			item.setPayValue(redPay);
			item.setRemark("余额(红包)支付:" + item.getPayValue());
//			item.setResultData(account.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
			item.setToPayId(account.getId());
			orderPaymentItemService.insert(item);
		}
		return realPay;
	}
	
	@Override
	public void updateCustomerAccount(String operationPhone,String customerPhone,ChargeSetting chargeSetting,String customerId,String accountId,Brand brand,ShopDetail shopDetail) {
		try{
	    	ChargeOrder chargeOrder = new ChargeOrder();
	    	chargeOrder.setId(ApplicationUtils.randomUUID());
	    	chargeOrder.setChargeMoney(chargeSetting.getChargeMoney());
	    	chargeOrder.setRewardMoney(chargeSetting.getRewardMoney());
	    	chargeOrder.setOrderState((byte)1);
	    	chargeOrder.setCreateTime(new Date());
	    	chargeOrder.setFinishTime(new Date());
	    	chargeOrder.setCustomerId(customerId);
	    	chargeOrder.setBrandId(brand.getId());
			chargeOrder.setType(0);
	    	chargeOrder.setShopDetailId(shopDetail.getId());
	    	chargeOrder.setChargeBalance(chargeSetting.getChargeMoney());
	    	chargeOrder.setNumberDayNow(chargeSetting.getNumberDay() - 1);
	    	BigDecimal amount = chargeSetting.getRewardMoney().divide(new BigDecimal(chargeSetting.getNumberDay()),2,BigDecimal.ROUND_FLOOR);
	    	chargeOrder.setArrivalAmount(amount);
	    	chargeOrder.setRewardBalance(amount);
	    	chargeOrder.setTotalBalance(chargeOrder.getChargeBalance().add(amount));
	    	BigDecimal endAmount = chargeSetting.getRewardMoney().subtract(amount.multiply(new BigDecimal(chargeSetting.getNumberDay() - 1)));
			chargeOrder.setEndAmount(endAmount);
	    	chargeOrderMapper.insert(chargeOrder);
	    	chargeLogService.insertChargeLogService(operationPhone, customerPhone, chargeOrder.getChargeBalance(), shopDetail,chargeOrder.getId());
	    	addAccount(chargeOrder.getChargeBalance(), accountId, "自助充值",AccountLog.SOURCE_CHARGE,shopDetail.getId());
	    	addAccount(chargeOrder.getRewardBalance(), accountId, "充值赠送",AccountLog.SOURCE_CHARGE_REWARD,shopDetail.getId());
	    	//微信推送
			wxPush(chargeOrder);
            BonusSetting bonusSetting = bonusSettingService.selectByChargeSettingId(chargeSetting.getId());
            if (bonusSetting != null){
                BonusLog bonusLog = new BonusLog();
                bonusLog.setId(ApplicationUtils.randomUUID());
                bonusLog.setChargeOrderId(chargeOrder.getId());
                bonusLog.setBonusSettingId(bonusSetting.getId());
                BigDecimal chargeMoney = chargeOrder.getChargeMoney();
                Integer bonusAmount = chargeMoney.multiply(new BigDecimal(bonusSetting.getChargeBonusRatio()).divide(new BigDecimal(100))).intValue();
                Integer shopownerBonusAmount = new BigDecimal(bonusAmount).multiply(new BigDecimal(bonusSetting.getShopownerBonusRatio()).divide(new BigDecimal(100))).intValue();
                Integer employeeBonusAmount = bonusAmount - shopownerBonusAmount;
                bonusLog.setBonusAmount(bonusAmount);
                bonusLog.setState(0);
                bonusLog.setShopownerBonusAmount(shopownerBonusAmount);
                bonusLog.setEmployeeBonusAmount(employeeBonusAmount);
                bonusLog.setCreateTime(new Date());
                bonusLog.setWishing(bonusSetting.getWishing());
                bonusLogService.insert(bonusLog);
            }
            Map map = new HashMap(4);
            map.put("brandName", brand.getBrandName());
            map.put("fileName", shopDetail.getName());
            map.put("type", "posAction");
            map.put("content", "店铺:"+shopDetail.getName()+"执行pos端充值(金额为:"+chargeOrder.getChargeBalance()+")操作手机号:"+operationPhone+",请求服务器地址为:" + MQSetting.getLocalIP());
            doPostAnsc( map);
    	}catch (Exception e) {
    		log.error("插入ChargeOrder或AccountLog失败!");
    		throw e;
		}
	}
	
	public void wxPush(ChargeOrder chargeOrder){
		log.info("----------品牌Id为:"+chargeOrder.getBrandId()+"");
		log.info("----------用户Id为:"+chargeOrder.getCustomerId()+"");
		Brand brand = brandService.selectById(chargeOrder.getBrandId());
		Customer customer = customerService.selectById(chargeOrder.getCustomerId());
		Account account = accountMapper.selectByPrimaryKey(customer.getAccountId());
		BrandSetting setting = brandSettingService.selectByBrandId(chargeOrder.getBrandId());
		DecimalFormat df = new DecimalFormat("0.00");
		//如果不是立即到账 优先推送一条提醒
		if(chargeOrder.getNumberDayNow() > 0){
			if(setting.getTemplateEdition()==0){
				String msgFrist = "充值成功！充值赠送红包会在" + (chargeOrder.getNumberDayNow() + 1) + "天内分批返还给您，请注意查收～";
				weChatService.sendCustomerMsg(msgFrist.toString(), customer.getWechatId(), brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
				Map map = new HashMap(4);
				map.put("brandName", brand.getBrandName());
				map.put("fileName", customer.getId());
				map.put("type", "UserAction");
				map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:充值成功！充值赠送红包会在" + (chargeOrder.getNumberDayNow() + 1) + "天内分批返还给您，请注意查收～,请求服务器地址为:" + MQSetting.getLocalIP());
				doPostAnsc(map);
			}else{
				List<TemplateFlow> templateFlowList=templateService.selectTemplateId(brand.getWechatConfig().getAppid(),"OPENTM412427536");
				BrandTemplateEdit brandTemplateEdit = brandTemplateEditService.selectOneByManyTerm(brand.getWechatConfig().getAppid(),"OPENTM412427536",TemplateSytle.RECHARGE_RETURN_MONEY);
				if(templateFlowList!=null&&templateFlowList.size()!=0){
					String templateId = templateFlowList.get(0).getTemplateId();
					String jumpUrl ="";
					Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
					Map<String, Object> first = new HashMap<String, Object>();

					if(brandTemplateEdit!=null){
						if(brandTemplateEdit.getBigOpen()){
								first.put("value", brandTemplateEdit.getStartSign());
						}else {
							first.put("value", "恭喜您！充值成功！");
						}
					}else {
						first.put("value", "恭喜您！充值成功！");
					}

					first.put("color", "#00DB00");
					Map<String, Object> keyword1 = new HashMap<String, Object>();
					keyword1.put("value", df.format(chargeOrder.getChargeMoney()));
					keyword1.put("color", "#000000");
					Map<String, Object> keyword2 = new HashMap<String, Object>();
					keyword2.put("value", DateUtil.formatDate(chargeOrder.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
					keyword2.put("color", "#000000");
					Map<String, Object> keyword3 = new HashMap<String, Object>();
					//keyword3.put("value", df.format(chargeOrder.getRewardMoney()));
					keyword3.put("value", df.format(account.getRemain()));
					keyword3.put("color", "#000000");
					Map<String, Object> remark = new HashMap<String, Object>();

					if(brandTemplateEdit!=null){
						if(brandTemplateEdit.getBigOpen()){
							remark.put("value", brandTemplateEdit.getEndSign());
						}else {
							remark.put("value", "充值赠送红包会在"+ (chargeOrder.getNumberDayNow() + 1) +"天内分批返还给您，请注意查收～");
						}
					}else {
						remark.put("value", "充值赠送红包会在"+ (chargeOrder.getNumberDayNow() + 1) +"天内分批返还给您，请注意查收～");
					}

					remark.put("color", "#173177");
					content.put("first", first);
					content.put("keyword1", keyword1);
					content.put("keyword2", keyword2);
					content.put("keyword3", keyword3);
					content.put("remark", remark);
					String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
					Map map = new HashMap(4);
					map.put("brandName", brand.getBrandName());
					map.put("fileName", customer.getId());
					map.put("type", "UserAction");
					map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
					doPostAnsc( map);
					//发送短信
					if(setting.getMessageSwitch()==1){
						com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
						smsParam.put("key1",chargeOrder.getNumberDayNow() + 1);
						com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_105745023");
					}
				}else{
					Map map = new HashMap(4);
					map.put("brandName", brand.getBrandName());
					map.put("fileName", customer.getId());
					map.put("type", "UserAction");
					map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
					doPostAnsc(map);
				}
			}
		}
		if(setting.getTemplateEdition()==0){
			StringBuffer msg = new StringBuffer();
			msg.append("今日充值余额已到账，快去看看吧~");
			String jumpurl = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
			msg.append("<a href='" + jumpurl+ "'>查看账户</a>");
			weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
			Map map = new HashMap(4);
			map.put("brandName", brand.getBrandName());
			map.put("fileName", customer.getId());
			map.put("type", "UserAction");
			map.put("content", "系统向用户:"+customer.getNickname()+"推送微信消息:"+msg.toString()+",请求服务器地址为:" + MQSetting.getLocalIP());
			doPostAnsc( map);
		}else{
			List<TemplateFlow> templateFlowList=templateService.selectTemplateId(brand.getWechatConfig().getAppid(),"OPENTM412000235");
			BrandTemplateEdit brandTemplateEdit =  brandTemplateEditService.selectOneByManyTerm(brand.getWechatConfig().getAppid(),"OPENTM412000235",TemplateSytle.RECHARGE);
			if(templateFlowList!=null&&templateFlowList.size()!=0){
				String templateId = templateFlowList.get(0).getTemplateId();
				String jumpUrl ="http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
				Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
				Map<String, Object> first = new HashMap<String, Object>();
				if(chargeOrder.getNumberDayNow()==0){
					if(brandTemplateEdit!=null){
						if(brandTemplateEdit.getBigOpen()){
							first.put("value", brandTemplateEdit.getStartSign());
						}else {
							first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
						}
					}else {
						first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
					}
				}else if(chargeOrder.getNumberDayNow()==1){
					if(brandTemplateEdit!=null){
						if(brandTemplateEdit.getBigOpen()){
							first.put("value", brandTemplateEdit.getStartSign());
						}else {
							first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
						}
					}else {
						first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
					}
				}else{
					if(brandTemplateEdit!=null){
						if(brandTemplateEdit.getBigOpen()){
							first.put("value", brandTemplateEdit.getStartSign());
						}else {
							first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
						}
					}else {
						first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
					}
				}
				first.put("color", "#00DB00");
				Map<String, Object> keyword1 = new HashMap<String, Object>();
				keyword1.put("value",df.format(chargeOrder.getChargeMoney()));
				keyword1.put("color", "#000000");
				Map<String, Object> keyword2 = new HashMap<String, Object>();
				if(chargeOrder.getNumberDayNow()==0){
					keyword2.put("value",df.format(chargeOrder.getRewardBalance()));
				}else if(chargeOrder.getNumberDayNow()==1){
					keyword2.put("value",df.format(chargeOrder.getArrivalAmount()));
				}else{
					keyword2.put("value",df.format(chargeOrder.getEndAmount()));
				}
				keyword2.put("color", "#000000");
				Map<String, Object> keyword3 = new HashMap<String, Object>();
				keyword3.put("value",DateUtil.formatDate(chargeOrder.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				keyword3.put("color", "#000000");
				Map<String, Object> remark = new HashMap<String, Object>();

				if(brandTemplateEdit!=null){
					if(brandTemplateEdit.getBigOpen()){
						remark.put("value", brandTemplateEdit.getEndSign());
					}else {
						remark.put("value", "点击这里查看账户余额");
					}
				}else {
					remark.put("value", "点击这里查看账户余额");
				}

				remark.put("color", "#173177");
				content.put("first", first);
				content.put("keyword1", keyword1);
				content.put("keyword2", keyword2);
				content.put("keyword3", keyword3);
				content.put("remark", remark);
				String result = weChatService.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
				Map map = new HashMap(4);
				map.put("brandName", brand.getBrandName());
				map.put("fileName", customer.getId());
				map.put("type", "UserAction");
				map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
				doPostAnsc(map);
				//发送短信
				if(setting.getMessageSwitch()==1){
					com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
					com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_105805058");
				}
			}else{
				Map map = new HashMap(4);
				map.put("brandName", brand.getBrandName());
				map.put("fileName", customer.getId());
				map.put("type", "UserAction");
				map.put("content", "系统数据库表tb_template_flow不存在模板消息的template_id,请求服务器地址为:" + MQSetting.getLocalIP());
				doPostAnsc(map);
			}
		}
	}

	@Override
	public List<Account> selectRebate() {
		return accountMapper.selectRebate();
	}

	@Override
	public Account selectAccountByCustomerId(String customerId) {
		return accountMapper.selectAccountByCustomerId(customerId);
	}

	@Override
	public List<Account> selectAccountByIds(List<String> ids) {
		return accountMapper.selectAccountByIds(ids);
	}
}

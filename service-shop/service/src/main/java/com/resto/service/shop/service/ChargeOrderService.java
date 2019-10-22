package com.resto.service.shop.service;

import org.json.JSONObject;
import com.resto.api.brand.define.api.*;
import com.resto.api.brand.dto.*;
import com.resto.api.brand.util.JSONResult;
import com.resto.api.brand.util.LogUtils;
import com.resto.api.brand.util.WeChatPayUtils;
import com.resto.conf.util.*;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.AccountLogType;
import com.resto.service.shop.constant.PayMode;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.mapper.ChargeOrderMapper;
import com.resto.service.shop.mapper.ChargeSettingMapper;
import com.resto.service.shop.util.LogTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.conf.util.HttpClient.doPostAnsc;

@Service
public class ChargeOrderService extends BaseService<ChargeOrder, String> {

    @Autowired
    private ChargeSettingMapper chargeSettingMapper;
    @Autowired
    private ChargeOrderMapper chargeorderMapper;
    @Autowired
    private ChargePaymentService chargePaymentService;
    @Autowired
    private AccountService accountService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderPaymentItemService orderPaymentItemService;
	@Autowired
	AccountLogService accountLogService;
    @Autowired
	ChargeOrderMapper chargeOrderMapper;
    @Autowired
    BonusSettingService bonusSettingService;
    @Autowired
    BonusLogService bonusLogService;
	@Autowired
	BrandApiTemplate templateService;
	@Autowired
	BrandApiBrandSetting brandSettingService;
	@Autowired
	BrandApi brandService;
	@Autowired
	BrandApiWechatConfig wechatConfigService;
	@Autowired
	BrandApiShopDetail shopDetailService;
	@Autowired
	BrandApiWxServerConfig wxServerConfigService;

	@Override
    public BaseDao<ChargeOrder, String> getDao() {
        return chargeorderMapper;
    }

	public ChargeOrder createChargeOrder(String settingId, String customerId, String shopId, String brandId) {
		ChargeSetting chargeSetting = chargeSettingMapper.selectByPrimaryKey(settingId);
		byte orderState = 0;
		ChargeOrder chargeOrder = new ChargeOrder(ApplicationUtils.randomUUID(),chargeSetting.getChargeMoney(),
				chargeSetting.getRewardMoney(),orderState,new Date(),customerId,shopId,brandId);
        chargeOrder.setChargeSettingId(settingId);
		chargeOrder.setChargeBalance(BigDecimal.ZERO);
		chargeOrder.setRewardBalance(BigDecimal.ZERO);
		chargeOrder.setTotalBalance(BigDecimal.ZERO);
		chargeOrder.setNumberDayNow(chargeSetting.getNumberDay() - 1);
		BigDecimal amount = chargeSetting.getRewardMoney().divide(new BigDecimal(chargeSetting.getNumberDay()),2,BigDecimal.ROUND_FLOOR);
		chargeOrder.setArrivalAmount(amount);
		BigDecimal endAmount = chargeSetting.getRewardMoney().subtract(amount.multiply(new BigDecimal(chargeSetting.getNumberDay() - 1)));
		chargeOrder.setEndAmount(endAmount);
		chargeOrder.setType(1);
		chargeorderMapper.insert(chargeOrder);
		return chargeOrder;
	}

	public void chargeorderWxPaySuccess(ChargePayment cp) {
		ChargeOrder chargeOrder = selectById(cp.getChargeOrderId());
		if (chargeOrder != null && chargeOrder.getOrderState() == 0) {
			logger.info("充值金额成功chargeId:"+chargeOrder.getId()+" paymentId:"+cp.getId());
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			BigDecimal chargeMoney = chargeOrder.getChargeMoney();
			BigDecimal reward = chargeOrder.getRewardMoney();
			// 开始充值余额
			accountService.addAccount(chargeMoney, customer.getAccountId(), "自助充值",AccountLog.SOURCE_CHARGE,cp.getShopDetailId());
			if(chargeOrder.getArrivalAmount() != null && chargeOrder.getArrivalAmount().doubleValue() > 0){
				accountService.addAccount(chargeOrder.getArrivalAmount(), customer.getAccountId(), "充值赠送",AccountLog.SOURCE_CHARGE_REWARD,cp.getShopDetailId());
			}
			// 添加充值记录
			chargeOrder.setOrderState((byte) 1);
			chargeOrder.setFinishTime(new Date());
			chargeOrder.setChargeBalance(chargeMoney);
			chargeOrder.setRewardBalance(chargeOrder.getArrivalAmount());
			if(chargeOrder.getArrivalAmount() != null && chargeOrder.getArrivalAmount().doubleValue() > 0){
				chargeOrder.setTotalBalance(chargeMoney.add(chargeOrder.getArrivalAmount()));
			}else{
				chargeOrder.setTotalBalance(chargeMoney);
			}
			chargePaymentService.insert(cp);
			update(chargeOrder);// 只能更新状态和结束时间
			//微信推送
			wxPush(chargeOrder);
            BonusSetting bonusSetting = bonusSettingService.selectByChargeSettingId(chargeOrder.getChargeSettingId());
            if (bonusSetting != null){
                BonusLog bonusLog = new BonusLog();
                bonusLog.setId(ApplicationUtils.randomUUID());
                bonusLog.setChargeOrderId(chargeOrder.getId());
                bonusLog.setBonusSettingId(bonusSetting.getId());
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
		}
	}

	private BigDecimal useCharge(ChargeOrder order, BigDecimal needToPay) {
		BigDecimal chargeBalance = order.getChargeBalance();
		if(chargeBalance.compareTo(needToPay)<0){ //如果余额不够支付剩余所需金额，则返回剩余余额
			return chargeBalance;
		}
		return needToPay; //否则返回需要支付的金额
	}

	private BigDecimal useReward(ChargeOrder order, BigDecimal rewardPay) {
		BigDecimal totalCharge = order.getChargeMoney().add(order.getRewardMoney());
		BigDecimal scalc = order.getRewardMoney().divide(totalCharge,2,BigDecimal.ROUND_HALF_UP); //支付比例
		BigDecimal useReward = rewardPay.multiply(scalc).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal rewardBalance = order.getRewardBalance();
		if(rewardBalance.compareTo(useReward)<0 || useReward.doubleValue() < 0.01){  //如果剩余赠送金额不够支付，则返回剩余赠送金额
			if(rewardPay.compareTo(rewardBalance) < 0){
				return rewardPay;
			}
			return rewardBalance;
		}
		return useReward; //否则返回需要支付的金额
	}

	private void useBalance(BigDecimal[] result, BigDecimal remindPay, String customerId, Order order,String brandName) {
		ChargeOrder chargeOrder = chargeorderMapper.selectFirstBalanceOrder(customerId);
		Customer c = customerService.selectById(customerId);
		if(chargeOrder!=null){
			BigDecimal useReward = useReward(chargeOrder,remindPay);  //使用返利支付
			BigDecimal useCharge = useCharge(chargeOrder,remindPay.subtract(useReward).setScale(2, BigDecimal.ROUND_HALF_UP));  //使用充值支付
			result[0] = useCharge;
			result[1] = useReward;
			chargeorderMapper.updateBalance(chargeOrder.getId(),useCharge,useReward);
			BigDecimal totalPay = result[0].add(result[1]);
			if(useCharge.compareTo(BigDecimal.ZERO)>0){
				OrderPaymentItem item = new OrderPaymentItem();
				item.setId(ApplicationUtils.randomUUID());
				item.setOrderId(order.getId());
				item.setPaymentModeId(PayMode.CHARGE_PAY);
				item.setPayTime(new Date());
				item.setPayValue(useCharge);
				item.setRemark("充值余额支付:" + item.getPayValue());
				item.setResultData(chargeOrder.getId());
				orderPaymentItemService.insert(item);
				//记录充值余额支付 orderAction
                LogTemplateUtils.getChargeByOrderType(brandName,item.getPayValue(),order.getId());
                LogTemplateUtils.getChargeByUserType(brandName,c,item.getPayValue());
			}
			if(useReward.compareTo(BigDecimal.ZERO)>0){
				OrderPaymentItem item = new OrderPaymentItem();
				item.setId(ApplicationUtils.randomUUID());
				item.setOrderId(order.getId());
				item.setPaymentModeId(PayMode.REWARD_PAY);
				item.setPayTime(new Date());
				item.setPayValue(useReward);
				item.setRemark("赠送余额支付:" + item.getPayValue());
				item.setResultData(chargeOrder.getId());
				orderPaymentItemService.insert(item);
				//记录充值赠送余额支付 orderActon
                LogTemplateUtils.getChargeRewardByOrderType(brandName,item.getPayValue(),order.getId());
                //记录充值赠送 userAction
                LogTemplateUtils.getChargeByUserType(brandName,c,item.getPayValue());
			}
			if(remindPay.compareTo(totalPay)>0){
				remindPay = remindPay.subtract(totalPay).setScale(2, BigDecimal.ROUND_HALF_UP);
				useBalance(result,remindPay,customerId,order,brandName);
			}
		}
	}

	public void wxPush(ChargeOrder chargeOrder){
		BrandDto brand = brandService.selectById(chargeOrder.getBrandId());
		Customer customer = customerService.selectById(chargeOrder.getCustomerId());
		BrandSettingDto setting = brandSettingService.selectByBrandId(chargeOrder.getBrandId());
		DecimalFormat df = new DecimalFormat("0.00");
		//如果不是立即到账 优先推送一条提醒
		if(chargeOrder.getNumberDayNow() > 0){
			if(setting.getTemplateEdition()==0){
				String msgFrist = "充值成功！充值赠送红包会在" + (chargeOrder.getNumberDayNow() + 1) + "天内分批返还给您，请注意查收～";
				WeChatUtils.sendCustomerMsg(msgFrist.toString(), customer.getWechatId(), brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
				Map map = new HashMap(4);
				map.put("brandName", brand.getBrandName());
				map.put("fileName", customer.getId());
				map.put("type", "UserAction");
				map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msgFrist.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
				doPostAnsc(LogUtils.url, map);
			}else{
				List<TemplateFlowDto> templateFlowList=templateService.selectTemplateId(brand.getWechatConfig().getAppid(),"OPENTM412427536");
				String templateId = templateFlowList.get(0).getTemplateId();
				String jumpUrl ="";
				Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
				Map<String, Object> first = new HashMap<String, Object>();
				first.put("value", "恭喜您！充值成功！");
				first.put("color", "#00DB00");
				Map<String, Object> keyword1 = new HashMap<String, Object>();
				keyword1.put("value", df.format(chargeOrder.getChargeMoney()));
				keyword1.put("color", "#000000");
				Map<String, Object> keyword2 = new HashMap<String, Object>();
				keyword2.put("value", DateUtil.formatDate(chargeOrder.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				keyword2.put("color", "#000000");
				Map<String, Object> keyword3 = new HashMap<String, Object>();
				keyword3.put("value", df.format(chargeOrder.getRewardMoney()));
				keyword3.put("color", "#000000");
				Map<String, Object> remark = new HashMap<String, Object>();
				remark.put("value", "充值赠送红包会在"+ (chargeOrder.getNumberDayNow() + 1) +"天内分批返还给您，请注意查收～");
				remark.put("color", "#173177");
				content.put("first", first);
				content.put("keyword1", keyword1);
				content.put("keyword2", keyword2);
				content.put("keyword3", keyword3);
				content.put("remark", remark);
				String result = WeChatUtils.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
				Map map = new HashMap(4);
				map.put("brandName", brand.getBrandName());
				map.put("fileName", customer.getId());
				map.put("type", "UserAction");
				map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
				doPostAnsc(LogUtils.url, map);
				//发送短信
				com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
				smsParam.put("key1",chargeOrder.getNumberDayNow() + 1);
				com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_105745023");
			}
        }
        if(setting.getTemplateEdition()==0){
			StringBuffer msg = new StringBuffer();
			msg.append("今日充值余额已到账，快去看看吧~");
			String jumpurl = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
			msg.append("<a href='" + jumpurl + "'>查看账户</a>");
			WeChatUtils.sendCustomerMsg(msg.toString(), customer.getWechatId(), brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
			Map map = new HashMap(4);
			map.put("brandName", brand.getBrandName());
			map.put("fileName", customer.getId());
			map.put("type", "UserAction");
			map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
			doPostAnsc(LogUtils.url, map);
		}else{
			List<TemplateFlowDto> templateFlowList=templateService.selectTemplateId(brand.getWechatConfig().getAppid(),"OPENTM412000235");
			String templateId = templateFlowList.get(0).getTemplateId();
			String jumpUrl ="http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
			Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
			Map<String, Object> first = new HashMap<String, Object>();
			if(chargeOrder.getNumberDayNow()==0){
				first.put("value", "今日充值赠送红包"+chargeOrder.getRewardBalance()+"元已到账！");
			}else if(chargeOrder.getNumberDayNow()==1){
				first.put("value", "今日充值赠送红包"+chargeOrder.getArrivalAmount()+"元已到账！");
			}else{
				first.put("value", "今日充值赠送红包"+chargeOrder.getEndAmount()+"元已到账！");
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
			remark.put("value", "点击这里查看账户余额");
			remark.put("color", "#173177");
			content.put("first", first);
			content.put("keyword1", keyword1);
			content.put("keyword2", keyword2);
			content.put("keyword3", keyword3);
			content.put("remark", remark);
			String result = WeChatUtils.sendTemplate(customer.getWechatId(), templateId, jumpUrl, content, brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
			Map map = new HashMap(4);
			map.put("brandName", brand.getBrandName());
			map.put("fileName", customer.getId());
			map.put("type", "UserAction");
			map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + content.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
			doPostAnsc(LogUtils.url, map);
			//发送短信
			com.alibaba.fastjson.JSONObject smsParam = new com.alibaba.fastjson.JSONObject();
			com.alibaba.fastjson.JSONObject jsonObject = SMSUtils.sendMessage(customer.getTelephone(),smsParam,"餐加","SMS_105805058");
		}
    }

	public BigDecimal selectByCustomerIdNotChangeId(String customerId) {
		return chargeOrderMapper.selectByCustomerIdNotChangeId(customerId);
	}

	public JSONResult withdrawals(BigDecimal money, String customerId) {
		JSONResult jsonResult = new JSONResult();
		List<ChargeOrder> chargeOrderList = chargeOrderMapper.selectByCustomerIdNotChangeIdList(customerId);
		jsonResult = withdrawalsMoney(chargeOrderList, money);
		if(!jsonResult.isSuccess()){
			return jsonResult;
		}
		Customer customer = customerService.selectById(customerId);
		//用户提现金额， 在用户余额中抹去
		Account account = accountService.selectById(customer.getAccountId());
		account.setRemain(account.getRemain().subtract(money));
		accountService.update(account);
		//记录用户提现的account_log
		AccountLog accountLog = new AccountLog();
		accountLog.setCreateTime(new Date());
		accountLog.setId(ApplicationUtils.randomUUID());
		accountLog.setMoney(money);
		accountLog.setRemain(account.getRemain());
		accountLog.setPaymentType(AccountLogType.PAY);
		accountLog.setRemark("用户提现");
		accountLog.setAccountId(account.getId());
		accountLog.setSource(AccountLog.WITHDRAWALS);
		accountLogService.insert(accountLog);
		return jsonResult;
	}

	public JSONResult withdrawalsMoney(List<ChargeOrder> chargeOrderList, BigDecimal money){
		JSONResult jsonResult = new JSONResult();
		for(ChargeOrder chargeOrder : chargeOrderList){
			if(money.doubleValue() > 0){
				if(chargeOrder.getChargeBalance().doubleValue() > 0){
					WechatConfigDto config = wechatConfigService.selectByBrandId(chargeOrder.getBrandId());
					ShopDetailDto shopDetail = shopDetailService.selectByPrimaryKey(chargeOrder.getShopDetailId());
					ChargePayment chargePayment = chargePaymentService.selectByChargeOrderId(chargeOrder.getId());
					JSONObject obj = new JSONObject(chargePayment.getPayData());
					int refund = (money.compareTo(chargeOrder.getChargeBalance()) > 0 ? chargeOrder.getChargeBalance() : money).multiply(new BigDecimal(100)).intValue();
					Map<String, String> result = null;
					if (shopDetail.getWxServerId() == null) {
						result = WeChatPayUtils.refund(ApplicationUtils.randomUUID(), obj.getString("transaction_id"),
								obj.getInt("total_fee"), refund, config.getAppid(), config.getMchid(),
								config.getMchkey(), config.getPayCertPath());
					} else {
						WxServerConfigDto wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());

						result = WeChatPayUtils.refundNew(ApplicationUtils.randomUUID(), obj.getString("transaction_id"),
								obj.getInt("total_fee"), refund, wxServerConfig.getAppid(), wxServerConfig.getMchid(),
								StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid(), wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
					}
					if (result.containsKey("ERROR")) {
						jsonResult.setSuccess(false);
						logger.error("微信退款异常！" + new JSONObject(result).toString());
						jsonResult.setMessage("微信退款异常！");
						return jsonResult;
					}else{
						jsonResult.setSuccess(true);
						jsonResult.setMessage("微信退款成功！");
					}
				}
				if(money.compareTo(chargeOrder.getChargeBalance()) > 0){
					money = money.subtract(chargeOrder.getChargeBalance());
					chargeOrder.setChargeBalance(new BigDecimal(0));
					chargeOrder.setTotalBalance(new BigDecimal(0));
				}else{
					chargeOrder.setChargeBalance(chargeOrder.getChargeBalance().subtract(money));
					chargeOrder.setTotalBalance(chargeOrder.getTotalBalance().subtract(money));
					money = new BigDecimal(0);
				}
				chargeorderMapper.updateByPrimaryKeySelective(chargeOrder);
			}
		}
		return jsonResult;
	}

	public void useChargePay(BigDecimal remainPay,String customerId,Order order,String brandName) {
		BigDecimal[] result = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
		useBalance(result,remainPay,customerId,order,brandName);

	}

	public BigDecimal selectTotalBalance(String customerId) {
		return chargeorderMapper.selectTotalBalance(customerId);
	}

	public void refundCharge(BigDecimal payValue, String id,String shopDetailId) {
		ChargeOrder chargeOrder= selectById(id);
		if(chargeOrder!=null){
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			chargeorderMapper.refundCharge(payValue,id);
			accountService.addAccount(payValue, customer.getAccountId(), "退还充值金额", AccountLog.CHARGE_PAY_REFUND,shopDetailId);
		}
	}

	public void refundReward(BigDecimal payValue, String id,String shopDetailId) {
		ChargeOrder chargeOrder= selectById(id);
		if(chargeOrder!=null){
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			chargeorderMapper.refundReward(payValue,id);
			accountService.addAccount(payValue, customer.getAccountId(), "退还充值赠送金额", AccountLog.REWARD_PAY_REFUND,shopDetailId);
		}
	}

	public void refundMoney(BigDecimal charge, BigDecimal reward, String id, String shopDetailId) {
		ChargeOrder chargeOrder= selectById(id);
		if(chargeOrder!=null){
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			accountService.addAccount(charge, customer.getAccountId(), "退还充值金额", AccountLog.CHARGE_PAY_REFUND,shopDetailId);
			chargeorderMapper.refundMoney(charge,reward,id);
			accountService.addAccount(reward, customer.getAccountId(), "退还充值赠送金额", AccountLog.REWARD_PAY_REFUND,shopDetailId);
		}
	}

	public List<ChargeOrder> selectByCustomerIdAndBrandId(String customerId, String brandId) {
		return chargeorderMapper.selectByCustomerIdAndBrandId(customerId,brandId);
	}

}
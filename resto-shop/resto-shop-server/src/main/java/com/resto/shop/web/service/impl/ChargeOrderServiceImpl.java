package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.*;
import com.resto.brand.web.dto.*;
import com.resto.brand.web.model.*;
import com.resto.api.customer.entity.Customer;
import com.resto.brand.web.service.*;
import com.resto.shop.web.constant.AccountLogType;
import com.resto.shop.web.constant.PayMode;
import com.resto.shop.web.dao.ChargeOrderMapper;
import com.resto.shop.web.dao.ChargeSettingMapper;
import com.resto.shop.web.model.*;
import com.resto.shop.web.report.ChargeOrderMapperReport;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.LogTemplateUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 *
 */
@Component
@Service
public class ChargeOrderServiceImpl extends GenericServiceImpl<ChargeOrder, String> implements ChargeOrderService {


    @Resource
    private ChargeSettingMapper chargeSettingMapper;

    @Resource
    private ChargeOrderMapper chargeorderMapper;

    @Resource
	private ChargeOrderMapperReport chargeOrderMapperReport;

    @Resource
    private ChargePaymentService chargePaymentService;

    @Resource
    private AccountService accountService;

    @Resource
    CustomerService customerService;

    @Resource
    OrderPaymentItemService orderPaymentItemService;

    @Resource
    BrandService brandService;

    @Resource
    ChargeOrderMapper chargeOrderMapper;

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
	AccountLogService accountLogService;

	@Autowired
    WechatConfigService wechatConfigService;

	@Autowired
    ShopDetailService shopDetailService;

	@Autowired
    WxServerConfigService wxServerConfigService;

	@Autowired
	WeChatService weChatService;

	@Autowired
	NewCustomerService newCustomerService;

    @Override
    public GenericDao<ChargeOrder, String> getDao() {
        return chargeorderMapper;
    }

	@Override
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

	@Override
	public void chargeorderWxPaySuccess(ChargePayment cp) {
		ChargeOrder chargeOrder = selectById(cp.getChargeOrderId());
		if(chargeOrder.getArrivalAmount() == null){
			chargeOrder.setArrivalAmount(new BigDecimal(0));
		}
		if (chargeOrder != null && chargeOrder.getOrderState() == 0) {
			log.info("充值金额成功chargeId:"+chargeOrder.getId()+" paymentId:"+cp.getId());
			Customer customer = newCustomerService.dbSelectByPrimaryKey(chargeOrder.getBrandId(),chargeOrder.getCustomerId());
			BigDecimal chargeMoney = chargeOrder.getChargeMoney();
			BigDecimal reward = chargeOrder.getRewardMoney();
			// 开始充值余额
			accountService.addAccount(chargeMoney, customer.getAccountId(), "自助充值",AccountLog.SOURCE_CHARGE,cp.getShopDetailId());
			if(chargeOrder.getArrivalAmount() != null && chargeOrder.getArrivalAmount().doubleValue() > 0){
				accountService.addAccount(chargeOrder.getArrivalAmount(), customer.getAccountId(), "充值赠送",AccountLog.SOURCE_CHARGE_REWARD,cp.getShopDetailId());
			}
			//accountService.addAccount(chargeOrder.getArrivalAmount(), customer.getAccountId(), "充值赠送",AccountLog.SOURCE_CHARGE_REWARD,cp.getShopDetailId());
			// 添加充值记录
			chargeOrder.setOrderState((byte) 1);
			chargeOrder.setFinishTime(new Date());
			chargeOrder.setChargeBalance(chargeMoney);
			chargeOrder.setRewardBalance(chargeOrder.getArrivalAmount());
//			chargeOrder.setTotalBalance(chargeMoney.add(chargeOrder.getArrivalAmount()));
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

	@Override
	public BigDecimal selectTotalBalance(String customerId) {
		return chargeorderMapper.selectTotalBalance(customerId);
	}

	@Override
	public void useChargePay(BigDecimal remainPay,String customerId,Order order,String brandName) {
		BigDecimal[] result = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
		useBalance(result,remainPay,customerId,order,brandName);

	}

	private void useBalance(BigDecimal[] result, BigDecimal remindPay, String customerId, Order order,String brandName) {
		ChargeOrder chargeOrder = chargeorderMapper.selectFirstBalanceOrder(customerId);
		Customer c = newCustomerService.dbSelectByPrimaryKey(chargeOrder.getBrandId(),customerId);
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
//				item.setResultData(chargeOrder.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
				item.setToPayId(chargeOrder.getId());
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
//				item.setResultData(chargeOrder.getId()); //现在resultData字段只用来存放微信、支付宝支付的回调  20171213 wtl
				item.setToPayId(chargeOrder.getId());
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

	@Override
	public void refundCharge(BigDecimal payValue, String id,String shopDetailId) {
		ChargeOrder chargeOrder= selectById(id);
		if(chargeOrder!=null){
			Customer customer = newCustomerService.dbSelectByPrimaryKey(chargeOrder.getBrandId(),chargeOrder.getCustomerId());
			chargeorderMapper.refundCharge(payValue,id);
			accountService.addAccount(payValue, customer.getAccountId(), "退还充值金额", AccountLog.CHARGE_PAY_REFUND,shopDetailId);
		}
	}

	@Override
	public void refundMoney(BigDecimal charge, BigDecimal reward, String id, String shopDetailId) {
		ChargeOrder chargeOrder= selectById(id);
		if(chargeOrder!=null){
			Customer customer = newCustomerService.dbSelectByPrimaryKey(chargeOrder.getBrandId(),chargeOrder.getCustomerId());
			accountService.addAccount(charge, customer.getAccountId(), "退还充值金额", AccountLog.CHARGE_PAY_REFUND,shopDetailId);
			chargeorderMapper.refundMoney(charge,reward,id);
			accountService.addAccount(reward, customer.getAccountId(), "退还充值赠送金额", AccountLog.REWARD_PAY_REFUND,shopDetailId);
		}
	}

	@Override
	public void refundReward(BigDecimal payValue, String id,String shopDetailId) {
		ChargeOrder chargeOrder= selectById(id);
		if(chargeOrder!=null){
			Customer customer = newCustomerService.dbSelectByPrimaryKey(chargeOrder.getBrandId(),chargeOrder.getCustomerId());
			chargeorderMapper.refundReward(payValue,id);
			accountService.addAccount(payValue, customer.getAccountId(), "退还充值赠送金额", AccountLog.REWARD_PAY_REFUND,shopDetailId);
		}
	}

    @Override
    public List<ChargeOrder> selectByDateAndShopId(String beginDate, String endDate, String shopId) {

       Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));

        return chargeorderMapper.selectByDateAndShopId(begin,end,shopId);
    }

    @Override
    public List<ChargeOrder> selectByDateAndBrandId(String beginDate, String endDate, String brandId) {

        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));

        return chargeorderMapper.selectByDateAndBrandId(begin,end,brandId);
    }

    /**
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 *
	 */
	@Override
	public List<ChargeOrder> shopChargeCodes(String shopDetailId, Date beginDate, Date endDate) {
		return chargeOrderMapperReport.shopChargeCodes(shopDetailId,beginDate,endDate);
	}

	/**
	 * 下载报表
	 * @param shopDetailId
	 * @param beginDate
	 * @param endDate
	 * @param
	 * @return
	 */


	@Override
	public Map<String, Object> shopChargeCodesSetDto(String shopDetailId, String beginDate, String endDate, String shopname) {
		Date begin = DateUtil.getformatBeginDate(beginDate);
		Date end = DateUtil.getformatEndDate(endDate);
		List<ChargeOrder>  chargeList=chargeOrderMapperReport.shopChargeCodes(shopDetailId,begin,end);
		List<ShopDetailDto> ShopDetailDtoList=new ArrayList<>();
		if(chargeList!=null&&chargeList.size()>0){
	    for (ChargeOrder charge:chargeList) {
		   ShopDetailDto ShopDetailDto=new ShopDetailDto(
		   		   shopname
				   ,null==charge.getChargeMoney()?BigDecimal.ZERO:charge.getChargeMoney()
				   ,null==charge.getRewardMoney()?BigDecimal.ZERO:charge.getRewardMoney()
				   ,null==charge.getFinishTime()?new Date():charge.getFinishTime()
				   ,charge.getType()
				   ,charge.getChargelog().getOperationPhone()
				   ,charge.getChargelog().getCustomerPhone()
		           );
		   //	ShopDetailDto ShopDetailDto=new ShopDetailDto("ss",new BigDecimal(10),new BigDecimal(10),new Date(),1,"122333344","222");
		   ShopDetailDtoList.add(ShopDetailDto);
	   }

     }

		Map<String, Object> map = new HashMap<>();
		map.put("shopDetailMap", ShopDetailDtoList);
		return map;
	}

	public void wxPush(ChargeOrder chargeOrder){
		Brand brand = brandService.selectById(chargeOrder.getBrandId());
		Customer customer = newCustomerService.dbSelectByPrimaryKey(brand.getId(),chargeOrder.getCustomerId());
		Account account = accountService.selectById(customer.getAccountId());
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
				map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msgFrist.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
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
					doPostAnsc(map);
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
					doPostAnsc( map);
				}
			}
        }
        if(setting.getTemplateEdition()==0){
			StringBuffer msg = new StringBuffer();
			msg.append("今日充值余额已到账，快去看看吧~");
			String jumpurl = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?dialog=myYue&subpage=my";
			msg.append("<a href='" + jumpurl + "'>查看账户</a>");
			weChatService.sendCustomerMsg(msg.toString(), customer.getWechatId(), brand.getWechatConfig().getAppid(), brand.getWechatConfig().getAppsecret());
			Map map = new HashMap(4);
			map.put("brandName", brand.getBrandName());
			map.put("fileName", customer.getId());
			map.put("type", "UserAction");
			map.put("content", "系统向用户:" + customer.getNickname() + "推送微信消息:" + msg.toString() + ",请求服务器地址为:" + MQSetting.getLocalIP());
			doPostAnsc(map);
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
    public List<Map<String, Object>> selectByShopToDay(Map<String, Object> selectMap) {
        return chargeorderMapper.selectByShopToDay(selectMap);
    }

    @Override
    public List<ChargeOrder> selectListByDateAndShopId(String zuoriDay, String zuoriDay1, String id) {
        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(zuoriDay));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(zuoriDay1));

        return chargeorderMapper.selectListByDateAndShopId(begin,end,id);
    }

    @Override
    public List<ChargeOrder> selectByCustomerIdAndBrandId(String customerId, String brandId) {
        return chargeorderMapper.selectByCustomerIdAndBrandId(customerId,brandId);
    }

    @Override
    public List<RedPacketDto> selectChargeRedPacket(Map<String, Object> selectMap) {
        return chargeOrderMapperReport.selectChargeRedPacket(selectMap);
    }

    @Override
    public List<ChargeOrder> selectMonthDto(Map<String, Object> selectMap) {
        return chargeOrderMapperReport.selectMonthDto(selectMap);
    }

    @Override
    public RechargeLogDto selectRechargeLog(String beginDate, String endDate, String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargeOrderMapperReport.selectRechargeLog(begin,end,brandId);
    }

    @Override
    public RechargeLogDto selectShopRechargeLog(String beginDate, String endDate, String shopId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return chargeOrderMapperReport.selectShopRechargeLog(begin,end,shopId);
    }



	@Override
	public List<Map<String, Object>> getChargeSumInfo(Map<String, Object> selectMap) {
		return chargeOrderMapper.getChargeSumInfo(selectMap);
	}

	@Override
	public List<String> selectCustomerChargeOrder(List<String> customerIds) {
		return chargeOrderMapper.selectCustomerChargeOrder(customerIds);
	}

    @Override
    public BigDecimal selectTotalBalanceByTimeAndShopId(Date monthBegin, Date monthEnd, String shopId) {
        return chargeOrderMapper.selectTotalBalanceByTimeAndShopId(monthBegin,monthEnd,shopId);
    }

	@Override
	public List<ChargeOrder> selectRemainderReturn() {
		return chargeOrderMapper.selectRemainderReturn();
	}

	@Override
	public void updateRemainderReturn(ChargeOrder chargeOrder) {
		//记录此次的返还金额
		BigDecimal returnAmount = BigDecimal.ZERO;
		//还剩余多次返还
		if (chargeOrder.getNumberDayNow() > 1){
			returnAmount = chargeOrder.getArrivalAmount();
		}else{ //最后一次返还
			returnAmount = chargeOrder.getEndAmount();
		}
		chargeOrder.setNumberDayNow(chargeOrder.getNumberDayNow() - 1);
		chargeOrder.setRewardBalance(chargeOrder.getRewardBalance().add(returnAmount));
		chargeOrder.setTotalBalance(chargeOrder.getTotalBalance().add(returnAmount));
		chargeOrderMapper.updateByPrimaryKeySelective(chargeOrder);
		accountService.addAccount(returnAmount, chargeOrder.getCustomer().getAccountId(), "充值赠送", AccountLog.SOURCE_CHARGE_REWARD, chargeOrder.getShopDetailId());
	}

	@Override
	public List<WeChatBill> selectChargeWeChatBill(String beginDate, String endDate, String shopId) {
		return chargeOrderMapperReport.selectChargeWeChatBill(beginDate, endDate, shopId);
	}

	@Override
	public ChargeTotalDto selectChargeTotal() {
		return chargeOrderMapperReport.selectChargeTotal();
	}

	@Override
	public JSONResult withdrawals(BigDecimal money, String customerId) {
		JSONResult jsonResult = new JSONResult();
		List<ChargeOrder> chargeOrderList = chargeOrderMapper.selectByCustomerIdNotChangeIdList(customerId);
		jsonResult = withdrawalsMoney(chargeOrderList, money);
		if(!jsonResult.isSuccess()){
			return jsonResult;
		}
		Customer customer = newCustomerService.dbSelectByPrimaryKey(chargeOrderList.get(0).getBrandId(),customerId) ;
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
                    WechatConfig config = wechatConfigService.selectByBrandId(chargeOrder.getBrandId());
                    ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(chargeOrder.getShopDetailId());
                    ChargePayment chargePayment = chargePaymentService.selectByChargeOrderId(chargeOrder.getId());
                            JSONObject obj = new JSONObject(chargePayment.getPayData());
                    int refund = (money.compareTo(chargeOrder.getChargeBalance()) > 0 ? chargeOrder.getChargeBalance() : money).multiply(new BigDecimal(100)).intValue();
                    Map<String, String> result = null;
                    String outRefundNo = obj.getString("out_trade_no");
                    //品牌账户
                    if (shopDetail.getWxServerId() == null) {
						result = WeChatPayUtils.refund(outRefundNo, obj.getString("transaction_id"),
								obj.getInt("total_fee"), refund, config.getAppid(), config.getMchid(),
								config.getMchkey(), config.getPayCertPath());

                    } else {
                    	//子账户
                        WxServerConfig wxServerConfig = wxServerConfigService.selectById(shopDetail.getWxServerId());
                        //WxServerConfig wxServerConfig = null;
                        String sub_mchid = StringUtils.isEmpty(shopDetail.getMchid()) ? config.getMchid() : shopDetail.getMchid();
                        result = WeChatPayUtils.refundNew(outRefundNo, obj.getString("transaction_id"),
                                obj.getInt("total_fee"), refund, wxServerConfig.getAppid(), wxServerConfig.getMchid(),sub_mchid
                                , wxServerConfig.getMchkey(), wxServerConfig.getPayCertPath());
                    }
                    if (result.containsKey("ERROR")) {
                        jsonResult.setSuccess(false);
                        log.error("微信退款异常！" + new JSONObject(result).toString());
                        String error = result.get("err_code_des");
                        if(!error.contains("余额")){
                            error = "微信退款异常！";
                        }
                        jsonResult.setMessage(error);
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

	@Override
	public BigDecimal selectByCustomerIdNotChangeId(String customerId) {
		return chargeOrderMapper.selectByCustomerIdNotChangeId(customerId);
	}
}
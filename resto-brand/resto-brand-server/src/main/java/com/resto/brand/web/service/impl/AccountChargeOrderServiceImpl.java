package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.alipay.config.AlipayConfig;
import com.resto.brand.core.enums.BehaviorType;
import com.resto.brand.core.enums.ChargeOrderStatus;
import com.resto.brand.core.enums.ChargePayType;
import com.resto.brand.core.enums.DetailType;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dao.AccountChargeOrderMapper;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 */
@Component
@Service
public class AccountChargeOrderServiceImpl extends GenericServiceImpl<AccountChargeOrder, String> implements AccountChargeOrderService {

    @Resource
    private AccountChargeOrderMapper accountchargeorderMapper;

    @Resource
    private BrandAccountService brandAccountService;


    @Resource
    private BrandSettingService brandSettingService;


    @Resource
    private AccountSettingService accountSettingService;

    @Resource
    private BrandAccountLogService brandAccountLogService;

    @Override
    public GenericDao<AccountChargeOrder,String> getDao() {
        return accountchargeorderMapper;
    }


    @Override
    public AccountChargeOrder saveChargeOrder(String brandId, String total_fee) {
        BigDecimal chargeMoney = new BigDecimal(total_fee).setScale(2, BigDecimal.ROUND_HALF_DOWN);//保留两位小数，不进行四舍五入操作
        AccountChargeOrder accountChargeOrder = new AccountChargeOrder();
        accountChargeOrder.setId(ApplicationUtils.randomUUID());
        accountChargeOrder.setBrandId(brandId);
        accountChargeOrder.setChargeMoney(chargeMoney);
        accountChargeOrder.setCreateTime(new Date());
        accountChargeOrder.setOrderStatus(ChargeOrderStatus.NO_PAY);
        accountchargeorderMapper.insertSelective(accountChargeOrder);
        return accountChargeOrder;

    }



    @Override
    public boolean checkAccountChargeOrder_AliPay(Map<String,String> resultMap) {
        boolean flag = false;
        AccountChargeOrder accountChargeOrder = accountchargeorderMapper.selectByPrimaryKey(resultMap.get("out_trade_no"));
        String remark = appendResult(accountChargeOrder.getRemark(),resultMap,"支付宝","支付成功");
        accountChargeOrder.setRemark(remark);
        //accountChargeOrder.setRemark(PayConfigUtil.appendResult(accountChargeOrder.getRemark(), resultMap, "支付宝", "支付成功"));
        BigDecimal chargeMoney = accountChargeOrder.getChargeMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);//得到两位小数，不进行四舍五入操作
        BigDecimal totalFee = new BigDecimal(resultMap.get("total_fee")).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        log.info("支付宝：返回的的  支付金额为："+totalFee+"  --->   数据库储存的应支付金额为："+chargeMoney);
        // 判断当前订单是否与创建的订单信息匹配
        if (chargeMoney.equals(totalFee) && AlipayConfig.seller_id.equals(resultMap.get("seller_id"))
                && ChargeOrderStatus.NO_PAY == accountChargeOrder.getOrderStatus()) {
            flag = updateAccountOrder(accountChargeOrder, resultMap.get("trade_no"), ChargePayType.ALI_PAY,resultMap.get("body"));
        }
        return flag;

    }


	@Override
    public List<AccountChargeOrder> selectHasPayList() {
        return accountchargeorderMapper.selectHasPayList();
    }

	@Override
	public List<AccountChargeOrder> selectHasPayListByBrandId(String brandId) {
		return accountchargeorderMapper.selectHasPayListByBrandId(brandId);
	}

	@Override
	public List<AccountChargeOrder> selectListByBrandIdAndTime(String beginTime, String endTime, String brandId) {
    	Date beginDate = DateUtil.getDateBegin(DateUtil.fomatDate(beginTime));
    	Date endDate = DateUtil.getDateEnd(DateUtil.fomatDate(endTime));
		return accountchargeorderMapper.selectListByBrandIdAndTime(beginDate,endDate,brandId);
	}


	//更新数据库中的账户充值订单
    public boolean updateAccountOrder(AccountChargeOrder accountChargeOrder, String trade_no, int payType,String body){
        //获取brandSetting
        BrandSetting brandSetting = brandSettingService.selectByBrandId(accountChargeOrder.getBrandId());
        AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(brandSetting.getId());

        boolean flag = false;
        //这个时候账户已经更新了
        int row = brandAccountService.chargeAccount(accountChargeOrder.getBrandId(),accountChargeOrder.getChargeMoney());
        if (row > 0) {//判断是否成功执行充值操作
            accountChargeOrder.setOrderStatus(ChargeOrderStatus.HAS_PAY);
            accountChargeOrder.setPushOrderTime(new Date());
            accountChargeOrder.setTradeNo(trade_no);
            accountChargeOrder.setPayType(payType);
            //查询品牌账户
            BrandAccount brandAccount = brandAccountService.selectByBrandId(accountChargeOrder.getBrandId());

            //插入一条记录
            String[] arr = body.split(",");//0 shopId 1 shopName 2 brandId 3 brandName
            BrandAccountLog brandAccountLog = new BrandAccountLog();
            brandAccountLog.setFoundChange(accountChargeOrder.getChargeMoney());//资金变动
            brandAccountLog.setCreateTime(new Date());//时间
            brandAccountLog.setGroupName(arr[3]);
            brandAccountLog.setBehavior(BehaviorType.CHARGE);
            brandAccountLog.setRemain(brandAccount.getAccountBalance());//剩余账户余额（账户已经更新）
            brandAccountLog.setAccountId(brandAccount.getId());
            brandAccountLog.setBrandId(arr[2]);
            brandAccountLog.setShopId(arr[0]);
            brandAccountLog.setDetail(DetailType.BRAND_ACCOUNT_CHARGE);//详情V
            brandAccountLog.setSerialNumber(trade_no);
            brandAccountLogService.insert(brandAccountLog);//记录品牌账户变化日志日志
			accountchargeorderMapper.updateByPrimaryKeySelective(accountChargeOrder);//更新品牌账户

            flag = true;
            //发短信通知 商家充值成功 // TODO: 2017/7/24

        }else{
            accountChargeOrder.setOrderStatus(ChargeOrderStatus.NO_CHARGE);
            StringBuffer str = new StringBuffer(accountChargeOrder.getRemark());
            str.append(DateUtil.getTime()+"执行充值语句影响了 "+row+" 行");
            accountChargeOrder.setRemark(str.toString());
            accountchargeorderMapper.updateByPrimaryKeySelective(accountChargeOrder);
        }
        return flag;
    }


	@Override
	public boolean checAccountChargeOrder_WxPay(Map<String, String> resultMap) {
		boolean flag = false;
		AccountChargeOrder accountChargeOrder = accountchargeorderMapper.selectByPrimaryKey(resultMap.get("out_trade_no"));
		String remark = appendResult(accountChargeOrder.getRemark(), resultMap, "微信支付", "支付成功");
		accountChargeOrder.setRemark(remark);
		BigDecimal chargeMoney = accountChargeOrder.getChargeMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN);//得到两位小数，不进行四舍五入操作
		//微信金额的单位是 分，比较时，需要除以一百
		BigDecimal totalFee = new BigDecimal(resultMap.get("total_fee")).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
		log.info("微信：返回的的  支付金额为："+totalFee+"  --->   数据库储存的应支付金额为："+chargeMoney);
		// 判断当前订单是否与创建的订单信息匹配
		if (chargeMoney.equals(totalFee) && ChargeOrderStatus.NO_PAY == accountChargeOrder.getOrderStatus()) {
			flag = updateAccountOrder(accountChargeOrder, resultMap.get("transaction_id"), ChargePayType.WECHAT_PAY,resultMap.get("attach"));
		}
		return flag;
	}

	@Override
	public void saveResultParam(Map<String, String> resultMap, String payType) {
		AccountChargeOrder accountChargeOrder = accountchargeorderMapper.selectByPrimaryKey(resultMap.get("out_trade_no"));
		accountChargeOrder.setRemark(appendResult(accountChargeOrder.getRemark(), resultMap, payType, "支付失败"));
		accountchargeorderMapper.updateByPrimaryKeySelective(accountChargeOrder);
	}


	//------------wxpay end ----------------


	public  String appendResult(String oldRemark, Map<String, String> resultMap, String payType, String payResult){
		StringBuilder remark = new StringBuilder(oldRemark!=null?oldRemark:"");
		remark.append("{\"支付类型\":\""+payType+"\",\"操作时间\":\""+ DateUtil.getTime()+"\",\"支付结果\":\""+payResult+"\",\"返回参数\":\""+ JSONObject.toJSONString(resultMap)+"\"};");
		return remark.toString();
	}



}

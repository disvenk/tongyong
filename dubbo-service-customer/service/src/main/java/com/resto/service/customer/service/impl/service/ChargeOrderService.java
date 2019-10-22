package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.constants.AccountLogType;
import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.entity.ChargeOrder;
import com.resto.api.customer.entity.Customer;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.*;
import com.resto.brand.web.dto.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.ChargeOrderMapper;
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
@Service
public class ChargeOrderService extends BaseServiceResto<ChargeOrder, ChargeOrderMapper> {



    @Resource
    private ChargeOrderMapper chargeorderMapper;


    @Resource
    private AccountService accountService;

    @Resource
    CustomerService customerService;


    @Resource
    ChargeOrderMapper chargeOrderMapper;

	@Autowired
	AccountLogService accountLogService;

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

	public void refundCharge(BigDecimal payValue, String id,String shopDetailId) {
		ChargeOrder chargeOrder= dbSelectByPrimaryKey(id);
		if(chargeOrder!=null){
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			chargeorderMapper.refundCharge(payValue,id);
			accountService.addAccount(payValue, customer.getAccountId(), "退还充值金额", AccountLog.CHARGE_PAY_REFUND,shopDetailId);
		}
	}

	public void refundMoney(BigDecimal charge, BigDecimal reward, String id, String shopDetailId) {
		ChargeOrder chargeOrder= dbSelectByPrimaryKey(id);
		if(chargeOrder!=null){
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			accountService.addAccount(charge, customer.getAccountId(), "退还充值金额", AccountLog.CHARGE_PAY_REFUND,shopDetailId);
			chargeorderMapper.refundMoney(charge,reward,id);
			accountService.addAccount(reward, customer.getAccountId(), "退还充值赠送金额", AccountLog.REWARD_PAY_REFUND,shopDetailId);
		}
	}

	public void refundReward(BigDecimal payValue, String id,String shopDetailId) {
		ChargeOrder chargeOrder= dbSelectByPrimaryKey(id);
		if(chargeOrder!=null){
			Customer customer = customerService.selectById(chargeOrder.getCustomerId());
			chargeorderMapper.refundReward(payValue,id);
			accountService.addAccount(payValue, customer.getAccountId(), "退还充值赠送金额", AccountLog.REWARD_PAY_REFUND,shopDetailId);
		}
	}

    public List<ChargeOrder> selectByDateAndShopId(String beginDate, String endDate, String shopId) {

       Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));

        return chargeorderMapper.selectByDateAndShopId(begin,end,shopId);
    }

    public List<ChargeOrder> selectByDateAndBrandId(String beginDate, String endDate, String brandId) {

        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginDate));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(endDate));

        return chargeorderMapper.selectByDateAndBrandId(begin,end,brandId);
    }

    public List<Map<String, Object>> selectByShopToDay(Map<String, Object> selectMap) {
        return chargeorderMapper.selectByShopToDay(selectMap);
    }

    public List<ChargeOrder> selectListByDateAndShopId(String zuoriDay, String zuoriDay1, String id) {
        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(zuoriDay));
        Date end = DateUtil.getDateEnd(DateUtil.fomatDate(zuoriDay1));

        return chargeorderMapper.selectListByDateAndShopId(begin,end,id);
    }


    public List<ChargeOrder> selectByCustomerIdAndBrandId(String customerId, String brandId) {
        return chargeorderMapper.selectByCustomerIdAndBrandId(customerId,brandId);
    }

	public List<Map<String, Object>> getChargeSumInfo(Map<String, Object> selectMap) {
		return chargeOrderMapper.getChargeSumInfo(selectMap);
	}

	public List<String> selectCustomerChargeOrder(List<String> customerIds) {
		return chargeOrderMapper.selectCustomerChargeOrder(customerIds);
	}

    public BigDecimal selectTotalBalanceByTimeAndShopId(Date monthBegin, Date monthEnd, String shopId) {
        return chargeOrderMapper.selectTotalBalanceByTimeAndShopId(monthBegin,monthEnd,shopId);
    }

	public List<ChargeOrder> selectRemainderReturn() {
		return chargeOrderMapper.selectRemainderReturn();
	}

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

	public BigDecimal selectByCustomerIdNotChangeId(String customerId) {
		return chargeOrderMapper.selectByCustomerIdNotChangeId(customerId);
	}
}
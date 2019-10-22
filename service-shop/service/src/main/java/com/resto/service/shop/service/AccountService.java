package com.resto.service.shop.service;

import com.resto.api.brand.dto.BrandDto;
import com.resto.api.brand.dto.ShopDetailDto;
import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.AccountLogType;
import com.resto.service.shop.constant.PayMode;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.mapper.AccountMapper;
import com.resto.service.shop.util.LogTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AccountService extends BaseService<Account, String> {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
	private AccountLogService accountLogService;
    @Autowired
	private CustomerService customerService;
    @Autowired
	private ChargeOrderService chargeOrderService;
    @Autowired
	private RedPacketService redPacketService;
    @Autowired
	private OrderPaymentItemService orderPaymentItemService;

	@Override
	public BaseDao<Account, String> getDao() {
		return accountMapper;
	}

	public Account selectAccountAndLogByCustomerId(String customerId) {
		Account account = accountMapper.selectAccountByCustomerId(customerId);
		if(account!=null){
			List<AccountLog> accountLogs = accountLogService.selectLogsByAccountId(account.getId());
			account.setAccountLogs(accountLogs);
		}
		return account;
	}

	public Account createCustomerAccount(Customer cus) {
		Account acc =new Account();
		acc.setId(ApplicationUtils.randomUUID());
		acc.setRemain(BigDecimal.ZERO);
		insert(acc);
		cus.setAccountId(acc.getId());
		customerService.update(cus);
		return acc;
	}

	public void addAccount(BigDecimal value, String accountId, String remark,Integer source,String shopDetailId) {
		Account account = selectById(accountId);
		account.setRemain(account.getRemain().add(value));
		if(value.doubleValue() > 0){
			addLog(value, account, remark, AccountLogType.INCOME,source,shopDetailId);
		}else{
			addLog(new BigDecimal(-1).multiply(value), account, remark, AccountLogType.PAY,source,shopDetailId);
		}

		update(account);
	}

	private void addLog(BigDecimal money,Account account,String remark,int type,int source,String shopDetailId){
		AccountLog acclog = new AccountLog();
		acclog.setCreateTime(new Date());
		acclog.setId(ApplicationUtils.randomUUID());
		acclog.setMoney(money);
		acclog.setRemain(account.getRemain());
		acclog.setPaymentType(type);
		acclog.setRemark(remark);
		acclog.setAccountId(account.getId());
		acclog.setSource(source);
		acclog.setShopDetailId(shopDetailId);
		accountLogService.insert(acclog);
	}

	public BigDecimal payOrder(Order order, BigDecimal payMoney, Customer customer, BrandDto brand, ShopDetailDto shopDetail) {
		Account account = selectById(customer.getAccountId());  //找到用户帐户
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
			item.setResultData(account.getId());
			orderPaymentItemService.insert(item);
//			UserActionUtils.writeToFtp(LogType.ORDER_LOG, brand.getBrandName(), shopDetail.getName(), order.getId(),
//					"订单使用余额(红包)支付了：" + item.getPayValue());
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

	public BigDecimal useAccount(BigDecimal payMoney, Account account,Integer source,String shopDetailId) {
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
		update(account);
		return useAccountValue;
	}

}

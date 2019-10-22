package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.api.customer.entity.*;
import com.resto.api.customer.service.NewAccountService;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dto.MemberUserDto;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.AccountLogType;
import com.resto.shop.web.constant.RedType;
import com.resto.shop.web.dao.CustomerMapper;
import com.resto.shop.web.dao.SvipMapper;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.model.Account;
import com.resto.shop.web.model.AccountLog;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.report.CustomerMapperReport;
import com.resto.shop.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
@Component
@Service
public class CustomerServiceImpl extends GenericServiceImpl<Customer, String> implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
	private CustomerMapperReport customerMapperReport;
    @Resource
    private AccountService accountService;
	@Resource
	private RedPacketService redPacketService;
	@Resource
	private NewAppraiseService newAppraiseService;
	@Resource
	private OrderService orderService;
	@Resource
	private ShopDetailService shopDetailService;

	@Resource
	ThirdCustomerService thirdCustomerService;

	@Resource
	AccountLogService accountLogService;

	@Autowired
	SvipMapper svipMapper;

	@Resource
	NewCustomerService newCustomerService;

	@Resource
	NewAccountService newAccountService;

    @Override
    public GenericDao<Customer, String> getDao() {
        return customerMapper;
    }

	@Override
	public com.resto.api.customer.entity.Customer login(String brandId,String openid) {
		com.resto.api.customer.entity.Customer customer = selectByOpenId(brandId,openid);
		if(customer!=null){
			Customer change = new Customer();
			change.setId(customer.getId());
			change.setLastLoginTime(new Date());
			update(change);
		}
		return customer;
	}
	
	private com.resto.api.customer.entity.Customer  selectByOpenId(String brandId,String openid) {
		com.resto.api.customer.entity.Customer c = new com.resto.api.customer.entity.Customer();
		c.setWechatId(openid);
		com.resto.api.customer.entity.Customer cus = newCustomerService.dbSelectOne(brandId,c);
		if(cus!=null){
			com.resto.api.customer.entity.Account  account = newAccountService.dbSelectByPrimaryKey(brandId,cus.getAccountId());
			cus.setAccount(account == null ? new BigDecimal(0) : account.getRemain());
		}
		return cus;
	}
	
	@Override
	public Customer selectById(String id) {
		Customer cus = customerMapper.selectByPrimaryKey(id);
		if(cus==null){
			return null;
		}
		Account account = accountService.selectById(cus.getAccountId());
		if(account==null){
			account = accountService.createCustomerAccount(cus);
		}
		cus.setAccount(account.getRemain());
		return cus;
	}
	
	@Override
	public com.resto.api.customer.entity.Customer register(com.resto.api.customer.entity.Customer customer) {
		String customerId = ApplicationUtils.randomUUID();
		com.resto.api.customer.entity.Customer c=new com.resto.api.customer.entity.Customer();
		c.setWechatId(customer.getWechatId());
		com.resto.api.customer.entity.Customer cus = newCustomerService.dbSelectOne(customer.getBrandId(),c);
		if(cus != null){
			return cus;
		}
		customer.setId(customerId);
		com.resto.api.customer.entity.Account  account = new com.resto.api.customer.entity.Account ();
		account.setId(ApplicationUtils.randomUUID());
		account.setRemain(BigDecimal.ZERO);
		account=newAccountService.dbSave(customer.getBrandId(),account);
		customer.setAccountId(account.getId());
		customer.setIsBindPhone(false);
		customer.setLastLoginTime(new Date());
		customer.setRegiestTime(new Date());
        customer.setCreateTime(new Date());
		customer.setAccount(account.getRemain());
		newCustomerService.dbSave(customer.getBrandId(),customer);
		return customer;
	}

	@Override
	public com.resto.api.customer.entity.Customer registerCard(com.resto.api.customer.entity.Customer  customer) {
		com.resto.api.customer.entity.Customer  cus = newCustomerService.dbSelectOne(customer.getBrandId(),customer);
		if(cus != null){
			return cus;
		}
		Account account = new Account();
		account.setId(ApplicationUtils.randomUUID());
		account.setRemain(BigDecimal.ZERO);
		accountService.insert(account);
		customer.setAccountId(account.getId());
		customer.setLastLoginTime(new Date());
		customer.setRegiestTime(new Date());
		customer.setCreateTime(new Date());
		customer.setAccount(account.getRemain());
		newCustomerService.dbSave(customer.getBrandId(),customer);
		return customer;
	}

	@Override
	public void updateCustomer(Customer customer) {
		update(customer);
	}

	@Override
	public com.resto.api.customer.entity.Customer bindPhone(String phone, String currentCustomerId,Integer couponType,String shopId,String shareCustomer,String shareOrderId) throws AppException {
		ShopDetail shopDetail = shopDetailService.selectById(shopId);
		com.resto.api.customer.entity.Customer cus=new com.resto.api.customer.entity.Customer();
		cus.setTelephone(phone);
		com.resto.api.customer.entity.Customer customer = newCustomerService.dbSelectOne(shopDetail.getBrandId(),cus);
		if(customer!=null){
			throw new AppException(AppException.PHONE_IS_BIND);
		}
		customer = new com.resto.api.customer.entity.Customer ();
		customer.setIsBindPhone(true);
		customer.setTelephone(phone);
		customer.setId(currentCustomerId);
		customer.setBindPhoneTime(new Date());
		customer.setBindPhoneShop(shopId);
		if(!currentCustomerId.equals(shareCustomer)){
			customer.setShareCustomer(shareCustomer);
			customer.setShareLink("clearShareLink");
		}
		newCustomerService.dbSave(shopDetail.getBrandId(),customer);
		//判断该用户是否在第三方储值有余额
		ThirdCustomer thirdCustomer = thirdCustomerService.selectByTelephone(customer.getTelephone());
		if(thirdCustomer != null){
			customer = newCustomerService.dbSelectByPrimaryKey(shopDetail.getBrandId(),customer.getId());
			//插入tb_red_packet
			RedPacket redPacket = new RedPacket();
			redPacket.setId(ApplicationUtils.randomUUID());
			redPacket.setRedMoney(thirdCustomer.getMoney());
			redPacket.setCreateTime(new Date());
			redPacket.setCustomerId(customer.getId());
			redPacket.setBrandId(customer.getBrandId());
			redPacket.setShopDetailId(customer.getBindPhoneShop());
			redPacket.setRedRemainderMoney(thirdCustomer.getMoney());
			redPacket.setRedType(RedType.THIRD_MONEY);
			redPacket.setOrderId(null);
			redPacketService.insert(redPacket);
			//修改余额
			Account account = accountService.selectById(customer.getAccountId());
			account.setRemain(account.getRemain().add(thirdCustomer.getMoney()));
			accountService.update(account);
			//修改tb_third_customer表
			thirdCustomer.setType(0);
			thirdCustomer.setMoney(new BigDecimal(0));
			thirdCustomerService.update(thirdCustomer);
			//添加余额日志表
			AccountLog acclog = new AccountLog();
			acclog.setCreateTime(new Date());
			acclog.setId(ApplicationUtils.randomUUID());
			acclog.setMoney(thirdCustomer.getMoney());
			acclog.setRemain(account.getRemain());
			acclog.setPaymentType(AccountLogType.INCOME);
			acclog.setRemark("第三方储值余额");
			acclog.setAccountId(account.getId());
			acclog.setSource(AccountLog.THIRD_MONEY);
			acclog.setShopDetailId(customer.getBindPhoneShop());
			accountLogService.insert(acclog);
		}
		return customer;
	}

	@Override
	public Customer selectNickNameAndTelephone(String customerId) {
		return customerMapper.selectNickNameAndTelephone(customerId);
	}

	@Override
	public List<Customer> selectListByBrandId(String currentBrandId) {
		return customerMapper.selectListByBrandId(currentBrandId);
	}

	@Override
	public List<String> selectTelephoneList() {
		return customerMapper.selectCustomerList();
	}

	@Override
	public void changeLastOrderShop(String shopDetailId, String customerId) {
		customerMapper.changeLastOrderShop(shopDetailId,customerId);
	}

	@Override
	public void unbindphone(String currentCustomerId) {
		Customer customer = customerMapper.selectByPrimaryKey(currentCustomerId);
		customer.setTelephone(null);
		customerMapper.updateByPrimaryKeySelective(customer);
	} 
	
	@Override
	public void updateNewNoticeTime(String id){
		customerMapper.updateNewNoticeTime(id);
	}

	@Override
	public void updateFirstOrderTime(String id) {
		customerMapper.updateFirstOrderTime(id);
	}

	@Override
	public BigDecimal rewareShareCustomer(ShareSetting shareSetting, Order order, Customer shareCustomer, Customer customer) {
		BigDecimal rebate = shareSetting.getRebate();
		BigDecimal money = order.getOrderMoney();
		BigDecimal rewardMoney = money.multiply(rebate).divide(new BigDecimal(100)).setScale(BigDecimal.ROUND_HALF_DOWN, 2);
		if(rewardMoney.compareTo(shareSetting.getMinMoney())<0){
			rewardMoney = shareSetting.getMinMoney();
		}else if(rewardMoney.compareTo(shareSetting.getMaxMoney())>0){
			rewardMoney = shareSetting.getMaxMoney();
		}
		accountService.addAccount(rewardMoney, shareCustomer.getAccountId(), "分享奖励", AccountLog.SOURCE_SHARE_REWARD,customer.getBindPhoneShop());
        RedPacket redPacket = new RedPacket();
        redPacket.setId(ApplicationUtils.randomUUID());
        redPacket.setRedMoney(rewardMoney);
        redPacket.setCreateTime(new Date());
        redPacket.setCustomerId(shareCustomer.getId());
        redPacket.setBrandId(customer.getBrandId());
        redPacket.setShopDetailId(customer.getBindPhoneShop());
        redPacket.setRedRemainderMoney(rewardMoney);
        redPacket.setRedType(RedType.SHARE_RED);
		redPacket.setOrderId(order.getId());
        redPacketService.insert(redPacket);
		log.info("分享奖励用户:"+rewardMoney+" 元"+"  分享者:"+shareCustomer.getId());
		return rewardMoney;
	}

	@Override
	public BigDecimal rewareShareCustomerAgain(ShareSetting shareSetting, Order order, Customer shareCustomer, Customer customer) {
		BigDecimal rebate = shareSetting.getAfterRebate();
		BigDecimal money = order.getOrderMoney();
		BigDecimal rewardMoney = money.multiply(rebate).divide(new BigDecimal(100)).setScale(BigDecimal.ROUND_HALF_DOWN, 2);
		if(rewardMoney.compareTo(shareSetting.getAfterMinMoney())<0){
			rewardMoney = shareSetting.getAfterMinMoney();
		}else if(rewardMoney.compareTo(shareSetting.getAfterMaxMoney())>0){
			rewardMoney = shareSetting.getAfterMaxMoney();
		}
		accountService.addAccount(rewardMoney, shareCustomer.getAccountId(), "分享奖励", AccountLog.SOURCE_SHARE_REWARD,customer.getBindPhoneShop());
		RedPacket redPacket = new RedPacket();
		redPacket.setId(ApplicationUtils.randomUUID());
		redPacket.setRedMoney(rewardMoney);
		redPacket.setCreateTime(new Date());
		redPacket.setCustomerId(shareCustomer.getId());
		redPacket.setBrandId(customer.getBrandId());
		redPacket.setShopDetailId(customer.getBindPhoneShop());
		redPacket.setRedRemainderMoney(rewardMoney);
		redPacket.setRedType(RedType.SHARE_RED);
		redPacket.setOrderId(order.getId());
		redPacketService.insert(redPacket);
		log.info("分享奖励用户:"+rewardMoney+" 元"+"  分享者:"+shareCustomer.getId());
		return rewardMoney;
	}

	@Override
	public Boolean checkRegistered(String id) {
		return customerMapper.checkRegistered(id) > 0;
	}

	@Override
	public Customer selectByOpenIdInfo(String openId) {
		return customerMapper.selectByOpenId(openId);
	}

    @Override
    public List<Customer> selectListByBrandIdHasRegister(String beginDate, String endDate,String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return  customerMapper.selectListByBrandIdHasRegister(begin,end,brandId);
    }
    
    @Override
    public Customer selectCustomerAccount(String telephone) {
    	return customerMapper.selectCustomerAccount(telephone);
    }

	@Override
	public Map<String, Object> selectListMember(String beginDate, String endDate, String brandId) {
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
		return customerMapper.selectListMember(begin,end,brandId);
	}

	@Override
	public List<MemberUserDto> callListMemberUser(String beginDate,String endDate) {
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
		return customerMapperReport.selectListMemberUser(begin,end);
	}
	
	@Override
	public String selectBrandUser() {
		return customerMapperReport.selectBrandUser();
	}

	@Override
	public Integer selectByShareCustomer(String customerId) {
		return customerMapper.selectByShareCustomer(customerId);
	}

	@Override
	public List<Customer> selectBirthUser(int pageNum,int pageSize) {
		return customerMapper.selectBirthUser(pageNum,pageSize);
	}

    @Override
    public Customer selectByTelePhone(String telePhone) {
        return customerMapper.selectByTelePhone(telePhone);
    }

	@Override
	public Customer selectBySerialNumber(String number) {
		return customerMapper.selectBySerialNumber(number);
	}


	@Override
	public Customer getCustomerLimitOne() {
		return customerMapper.getCustomerLimitOne();
	}

    @Override
    public List<Customer> selectByTelePhones(List<String> telePhones) {
        return customerMapper.selectByTelePhones(telePhones);
    }

	@Override
	public List<Customer> getCommentCustomer(String startTime, Integer time,Integer type) {
		return customerMapper.getCommentCustomer(startTime,time,type);
	}

	@Override
	public List<Customer> selectShareCustomerList(String customerId, Integer currentPage, Integer showCount) {
		return customerMapper.selectShareCustomerList(customerId, currentPage, showCount);
	}

	@Override
	public List<Customer> selectBySelectMap(Map<String, Object> selectMap) {
		return customerMapperReport.selectBySelectMap(selectMap);
	}

	@Override
	public int updateCustomerWechatId(Customer customer) {
		return customerMapper.updateCustomerWechatId(customer);
	}

	@Override
	public Customer selectByAccountId(String accountId) {
		return customerMapper.selectByAccountId(accountId);
	}


    @Override
    public Map getCustomerConsumeInfo(String shopId, String customerId) {
		Double average = 0.0;
		int lastScore = 0;
		StringBuffer balanceSb = new StringBuffer();
		com.resto.api.customer.entity.Account account = newAccountService.selectAccountAndLogByCustomerId(shopDetailService.selectById(shopId).getBrandId(),customerId);
		if(account != null){
			balanceSb.append("【余额:" + account.getRemain() + "】");
		}
		ShopDetail shopDetail = shopDetailService.selectById(shopId);
		List<Appraise> appraiseList = newAppraiseService.selectAllAppraiseByShopIdAndCustomerId(shopDetail.getBrandId(),shopId, customerId);
		if(appraiseList != null && appraiseList.size() > 0){
			Double sum = 0.0;
			for(Appraise appraise : appraiseList){
				sum += appraise.getLevel();
			}
			average = sum / appraiseList.size();
			lastScore = appraiseList.get(0).getLevel();
		}

		// 消费次数
		Integer consumptionCount = orderService.selectCompleteOrderCount(shopId, customerId);

		Long svip = svipMapper.isSvip(customerId);

		balanceSb.append("【消费" + consumptionCount + "次】");

		Map consumeInfo = new HashMap();
		if(svip>0){
			consumeInfo.put("CUSTOMER_STATUS", 1);
		}else {
			consumeInfo.put("CUSTOMER_STATUS", 0);
		}
		// 	平均分
		consumeInfo.put("CUSTOMER_SATISFACTION_DEGREE", new BigDecimal(average).setScale(2, RoundingMode.UP));
		// 上次评分
		consumeInfo.put("CUSTOMER_SATISFACTION", lastScore);
		// 用户余额
		consumeInfo.put("CUSTOMER_PROPERTY", balanceSb);
        return consumeInfo;
    }

	/**
	 * 分页查询用户信息，支持手机号用户昵称搜索
	 * @param selectMap
	 * @return
	 */
	@Override
	public Map<String, Object> queryCustomerPaging(Map<String, Object> selectMap) {
		//分页查询数据
		List<Customer> customerList = customerMapperReport.selectBySelectMap(selectMap);
		//获取账户信息集
		List<Account> accountList = customerList.stream().map(Customer::getAccountInfo).collect(Collectors.toList());
		//获取充值订单集
		List<ChargeOrder> chargeOrderList = customerList.stream().map(Customer::getChargeOrderList)
				.reduce(new ArrayList<>(), (chargeOrderList1, chargeOrderList2) -> Stream.of(chargeOrderList1.stream(),chargeOrderList2.stream())
				.flatMap(chargeOrderStream -> chargeOrderStream).collect(Collectors.toList()));
		//返回值
		Map<String, Object> result = new HashMap<String, Object>(){{
			put("customerList", customerList);
			put("accountList", accountList);
			put("chargeOrderList", chargeOrderList);
		}};
		//查询总数据记录
		Optional.ofNullable(selectMap.get("selectCount")).ifPresent(o -> {
			Integer customerCount = customerMapperReport.selectCountBySelectMap(selectMap);
			result.put("customerCount", customerCount);
		});
		return result;
	}

    @Override
    public String selectCustomerByUnionId(String unionId) {
        return customerMapper.selectCustomerByUnionId(unionId);
    }

	@Override
	public List<Customer> selectByUnionIdNotCustomerId(String unionId, String customerId) {
		return customerMapper.selectByUnionIdNotCustomerId(unionId, customerId);
	}

	@Override
	public double selectBirthdayCoupons() {
		return customerMapper.selectBirthdayCoupons();
	}


}

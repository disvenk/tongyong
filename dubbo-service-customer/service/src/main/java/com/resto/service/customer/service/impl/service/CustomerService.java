package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.constants.AccountLogType;
import com.resto.api.customer.constants.RedType;
import com.resto.api.customer.entity.*;
import com.resto.api.customer.exception.AppException;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.web.dto.MemberUserDto;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountMapper;
import com.resto.service.customer.mapper.CustomerMapper;
import com.resto.service.customer.mapper.ThirdCustomerMapper;
import eleme.openapi.sdk.api.service.OrderService;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
@Service
public class CustomerService extends BaseServiceResto<Customer, CustomerMapper> {

    @Resource
    private CustomerMapper customerMapper;

	@Resource
	private AccountMapper accountMapper;

	@Resource
	private ThirdCustomerService thirdCustomerService;

	@Resource
	private AccountService accountService;

	@Resource
	private RedPacketService redPacketService;

	@Resource
	AccountLogService accountLogService;


	public Customer login(String openid) {
		Customer customer = selectByOpenId(openid);
		if(customer!=null){
			Customer change = new Customer();
			change.setId(customer.getId());
			change.setLastLoginTime(new Date());
			dbUpdate(change);
		}
		return customer;
	}
	
	public Customer selectByOpenId(String openid) {
		Customer customer = new Customer();
		customer.setWechatId(openid);
		Customer cus = dbSelectOne(customer);
		if(cus!=null){
			Account account = accountMapper.selectByPrimaryKey(cus.getAccountId());
			cus.setAccount(account == null ? new BigDecimal(0) : account.getRemain());
		}
		return cus;
	}

	public Customer selectById(String id) {
		Customer cus = customerMapper.selectByPrimaryKey(id);
		if(cus==null){
			return null;
		}
		Account account = accountMapper.selectByPrimaryKey(cus.getAccountId());
		if(account==null){
			account = accountService.createCustomerAccount(cus);
		}
		cus.setAccount(account.getRemain());
		return cus;
	}

	public Customer register(Customer customer) {
		String customerId = ApplicationUtils.randomUUID();
		Customer customer1 = new Customer();
		customer1.setWechatId(customer.getWechatId());
		Customer cus = dbSelectOne(customer1);
		if(cus != null){
			return cus;
		}
		customer.setId(customerId);
		Account account = new Account();
		account.setId(ApplicationUtils.randomUUID());
		account.setRemain(BigDecimal.ZERO);
		accountMapper.insert(account);
		customer.setAccountId(account.getId());
		customer.setIsBindPhone(false);
		customer.setLastLoginTime(new Date());
		customer.setRegiestTime(new Date());
        customer.setCreateTime(new Date());
		customer.setAccount(account.getRemain());
		dbSave(customer);
		return customer;
	}

	public Customer registerCard(Customer customer) {
		String customerId = ApplicationUtils.randomUUID();
		Customer customer1 = new Customer();
		customer1.setWechatId(customer.getWechatId());
		Customer cus = dbSelectOne(customer1);
		if(cus != null){
			return cus;
		}
		customer.setId(customerId);
		Account account = new Account();
		account.setId(ApplicationUtils.randomUUID());
		account.setRemain(BigDecimal.ZERO);
		accountMapper.insert(account);
		customer.setAccountId(account.getId());
		customer.setLastLoginTime(new Date());
		customer.setRegiestTime(new Date());
		customer.setCreateTime(new Date());
		customer.setAccount(account.getRemain());
		dbSave(customer);
		return customer;
	}

	public void updateCustomer(Customer customer) {
		dbUpdate(customer);
	}

	public Customer bindPhone(String phone, String currentCustomerId,Integer couponType,String shopId,String shareCustomer,String shareOrderId) throws AppException {
		Customer customer1 = new Customer();
		customer1.setTelephone(phone);
		Customer customer = dbSelectOne(customer1);
		if(customer!=null){
			throw new AppException(AppException.PHONE_IS_BIND);
		}
		customer = new Customer();
		customer.setIsBindPhone(true);
		customer.setTelephone(phone);
		customer.setId(currentCustomerId);
		customer.setBindPhoneTime(new Date());
		customer.setBindPhoneShop(shopId);
		if(!currentCustomerId.equals(shareCustomer)){
			customer.setShareCustomer(shareCustomer);
			customer.setShareLink("clearShareLink");
		}
//		customer.setRegisterShopId(shopId);
//		update(customer);
		customerMapper.registerCustomer(customer);

		//判断该用户是否在第三方储值有余额
		ThirdCustomer thirdCustomerSel = new ThirdCustomer();
		thirdCustomerSel.setTelephone(customer.getTelephone());

		ThirdCustomer thirdCustomer = thirdCustomerService.selectByTelephone(customer.getTelephone());
		if(thirdCustomer != null){
			customer = customerMapper.selectByPrimaryKey(customer.getId());
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
			redPacketService.dbSave(redPacket);
			//修改余额
			Account account = accountService.dbSelectByPrimaryKey(customer.getAccountId());
			account.setRemain(account.getRemain().add(thirdCustomer.getMoney()));
			accountService.dbUpdate(account);
			//修改tb_third_customer表
			thirdCustomer.setType(0);
			thirdCustomer.setMoney(new BigDecimal(0));
			thirdCustomerService.dbUpdate(thirdCustomer);
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
			accountLogService.dbSave(acclog);
		}
		return customer;
	}

	public Customer selectNickNameAndTelephone(String customerId) {
		Customer customer = new Customer();
		customer.setId(customerId);
		return dbSelectOne(customer);
	}

	public List<Customer> selectListByBrandId(String currentBrandId) {
		Customer customer = new Customer();
		customer.setBrandId(currentBrandId);
		return dbSelect(customer);
	}

	public List<String> selectTelephoneList() {
		return customerMapper.selectCustomerList();
	}

	public void changeLastOrderShop(String shopDetailId, String customerId) {
		customerMapper.changeLastOrderShop(shopDetailId,customerId);
	}

	public void unbindphone(String currentCustomerId) {
		Customer customer = customerMapper.selectByPrimaryKey(currentCustomerId);
		customer.setTelephone(null);
		customerMapper.updateByPrimaryKeySelective(customer);
	} 

	public void updateNewNoticeTime(String id){
		customerMapper.updateNewNoticeTime(id);
	}

	public void updateFirstOrderTime(String id) {
		customerMapper.updateFirstOrderTime(id);
	}

	public Boolean checkRegistered(String id) {
		return customerMapper.checkRegistered(id) > 0;
	}

	public Customer selectByOpenIdInfo(String openId) {
		Customer customer = new Customer();
		customer.setWechatId(openId);
		return dbSelectOne(customer);
	}

    public List<Customer> selectListByBrandIdHasRegister(String beginDate, String endDate,String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return  customerMapper.selectListByBrandIdHasRegister(begin,end,brandId);
    }

    public Customer selectCustomerAccount(String telephone) {
    	return customerMapper.selectCustomerAccount(telephone);
    }


	public Integer selectByShareCustomer(String customerId) {
		return customerMapper.selectByShareCustomer(customerId);
	}

    public List<Customer> selectBirthUser() {
        return customerMapper.selectBirthUser();
    }

    public Customer selectByTelePhone(String telePhone) {
	    Customer customer = new Customer();
	    customer.setTelephone(telePhone);
        return dbSelectOne(customer);
    }

	public Customer selectBySerialNumber(String number) {
        Customer customer = new Customer();
        customer.setSerialNumber(Long.parseLong(number));
		return dbSelectOne(customer);
	}


	public Customer getCustomerLimitOne() {
		return customerMapper.getCustomerLimitOne();
	}

    public List<Customer> selectByTelePhones(List<String> telePhones) {
        return customerMapper.selectByTelePhones(telePhones);
    }

	public List<Customer> getCommentCustomer(String startTime, Integer time,Integer type) {
		return customerMapper.getCommentCustomer(startTime,time,type);
	}

	public List<Customer> selectShareCustomerList(String customerId, Integer currentPage, Integer showCount) {
		return customerMapper.selectShareCustomerList(customerId, currentPage, showCount);
	}


	public int updateCustomerWechatId(Customer customer) {
		return customerMapper.updateCustomerWechatId(customer);
	}

	public Customer selectByAccountId(String accountId) {
	    Customer customer = new Customer();
	    customer.setAccountId(accountId);
		return dbSelectOne(customer);
	}


    public String selectCustomerByUnionId(String unionId) {
	    Customer customer = new Customer();
	    customer.setUnionId(unionId);
        Customer customer1 = dbSelectOne(customer);
        return customer1==null?null:customer1.getId();
    }
}

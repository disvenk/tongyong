package com.resto.service.shop.service;

import com.resto.api.brand.dto.MemberUserDto;
import com.resto.api.brand.dto.ShareSettingDto;
import com.resto.conf.util.ApplicationUtils;
import com.resto.conf.util.DateUtil;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.AccountLogType;
import com.resto.service.shop.constant.RedType;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService extends BaseService<Customer, String>{

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RedPacketService redPacketService;
	@Autowired
	ThirdCustomerService thirdCustomerService;
	@Autowired
	AccountLogService accountLogService;

    @Override
    public BaseDao<Customer, String> getDao() {
        return customerMapper;
    }

	public Customer login(String openid) {
		Customer customer = selectByOpenId(openid);
		if(customer!=null){
			Customer change = new Customer();
			change.setId(customer.getId());
			change.setLastLoginTime(new Date());
			update(change);
		}
		return customer;
	}
	
	private Customer selectByOpenId(String openid) {
		Customer cus = customerMapper.selectByOpenId(openid);
		if(cus!=null){
			Account account = accountService.selectById(cus.getAccountId());
			cus.setAccount(account == null ? new BigDecimal(0) : account.getRemain());
		}
		return cus;
	}
	
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
	
	public Customer register(Customer customer) {
		String customerId = ApplicationUtils.randomUUID();
		Customer cus = customerMapper.selectByOpenId(customer.getWechatId());
		if(cus != null){
			return cus;
		}
		customer.setId(customerId);
		Account account = new Account();
		account.setId(ApplicationUtils.randomUUID());
		account.setRemain(BigDecimal.ZERO);
		accountService.insert(account);
		customer.setAccountId(account.getId());
		customer.setIsBindPhone(false);
		customer.setLastLoginTime(new Date());
		customer.setRegiestTime(new Date());
        customer.setCreateTime(new Date());
		customer.setAccount(account.getRemain());
		insert(customer);
		return customer;
	}

	public Customer registerCard(Customer customer) {
		String customerId = ApplicationUtils.randomUUID();
		Customer cus = customerMapper.selectByOpenId(customer.getWechatId());
		if(cus != null){
			return cus;
		}
		customer.setId(customerId);
		Account account = new Account();
		account.setId(ApplicationUtils.randomUUID());
		account.setRemain(BigDecimal.ZERO);
		accountService.insert(account);
		customer.setAccountId(account.getId());
		customer.setLastLoginTime(new Date());
		customer.setRegiestTime(new Date());
		customer.setCreateTime(new Date());
		customer.setAccount(account.getRemain());
		insert(customer);
		return customer;
	}

	public void updateCustomer(Customer customer) {
		update(customer);
	}

	public Customer bindPhone(String phone, String currentCustomerId,Integer couponType,String shopId,String shareCustomer,String shareOrderId) throws AppException {
		Customer customer = customerMapper.selectByPhone(phone);
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
			redPacketService.insert(redPacket);
			//修改余额
			Account account = accountService.selectById(customer.getAccountId());
			account.setRemain(account.getRemain().add(thirdCustomer.getMoney()));
			accountService.update(account);
			//修改tb_third_customer表
			thirdCustomer.setType(0);
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

	public Customer selectNickNameAndTelephone(String customerId) {
		return customerMapper.selectNickNameAndTelephone(customerId);
	}

	public List<Customer> selectListByBrandId(String currentBrandId) {
		return customerMapper.selectListByBrandId(currentBrandId);
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
		return customerMapper.selectByOpenId(openId);
	}

    public List<Customer> selectListByBrandIdHasRegister(String beginDate, String endDate,String brandId) {
        Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
        return  customerMapper.selectListByBrandIdHasRegister(begin,end,brandId);
    }
    
    public Customer selectCustomerAccount(String telephone) {
    	return customerMapper.selectCustomerAccount(telephone);
    }

	public Map<String, Object> selectListMember(String beginDate, String endDate, String brandId) {
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
		return customerMapper.selectListMember(begin,end,brandId);
	}

	public List<MemberUserDto> callListMemberUser(String beginDate, String endDate) {
		Date begin = DateUtil.getformatBeginDate(beginDate);
        Date end = DateUtil.getformatEndDate(endDate);
		return customerMapper.selectListMemberUser(begin,end);
	}
	
	public String selectBrandUser() {
		return customerMapper.selectBrandUser();
	}

	public Integer selectByShareCustomer(String customerId) {
		return customerMapper.selectByShareCustomer(customerId);
	}

    public List<Customer> selectBirthUser() {
        return customerMapper.selectBirthUser();
    }

    public Customer selectByTelePhone(String telePhone) {
        return customerMapper.selectByTelePhone(telePhone);
    }

	public Customer selectBySerialNumber(String number) {
		return customerMapper.selectBySerialNumber(number);
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

	public List<Customer> selectBySelectMap(Map<String, Object> selectMap) {
		return customerMapper.selectBySelectMap(selectMap);
	}

	public int updateCustomerWechatId(Customer customer) {
		return customerMapper.updateCustomerWechatId(customer);
	}

	public Customer selectByAccountId(String accountId) {
		return customerMapper.selectByAccountId(accountId);
	}
}

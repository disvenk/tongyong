package com.resto.shop.web.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.MemberUserDto;
import com.resto.brand.web.model.ShareSetting;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.Customer;
import com.resto.shop.web.model.Order;

public interface CustomerService extends GenericService<Customer, String> {

	com.resto.api.customer.entity.Customer login(String brandId,String openid);

	Customer getCustomerLimitOne();

	com.resto.api.customer.entity.Customer register(com.resto.api.customer.entity.Customer  customer);

	com.resto.api.customer.entity.Customer  registerCard(com.resto.api.customer.entity.Customer  customer);

    void updateCustomer(Customer customer);

	com.resto.api.customer.entity.Customer bindPhone(String phone, String currentCustomerId,Integer couponType,String shopId,String shareCustomer,String shareOrderId) throws AppException;
    
	/**
	 * 根据ID才查询用户昵称和手机号码
	 * @param customerId
	 * @return
	 */
	Customer selectNickNameAndTelephone(String customerId);

	List<Customer> selectListByBrandId(String currentBrandId);

	List<String> selectTelephoneList();


	void changeLastOrderShop(String shopDetailId, String customerId);
	
	/**
	 * 解绑手机号码
	 * @param currentCustomerId
	 */
	void unbindphone(String currentCustomerId);

	void updateNewNoticeTime(String id);

	void updateFirstOrderTime(String id);

	BigDecimal rewareShareCustomer(ShareSetting shareSetting, Order order, Customer shareCustomer, Customer customer);

	BigDecimal rewareShareCustomerAgain(ShareSetting shareSetting, Order order, Customer shareCustomer, Customer customer);

	Boolean checkRegistered(String id);

	Customer selectByOpenIdInfo(String openId);

    /**
     * 查询时间段内的注册用户
     * @param beginDate
     * @param endDate
     * @return
     */
    List<Customer> selectListByBrandIdHasRegister(String beginDate, String endDate,String brandId);
    
    
    /**
     * 得到某个时间段店铺的会员信息和订单情况
     * @param beginDate
     * @param endDate
     * @return
     */
    List<MemberUserDto> callListMemberUser(String beginDate,String endDate);
    /**
     * 查询某个时间段的店铺会员信息
     * @param beginDate
     * @param endDate
     * @param brandId
     * @return
     */
	Map<String,Object> selectListMember(String beginDate, String endDate,String brandId);
    
    Customer selectCustomerAccount(String telephone);
    
    //得到品牌用户信息
    String selectBrandUser();

	Integer selectByShareCustomer(String customerId);

    List<Customer> selectBirthUser(int pageNum,int pageSize);

    /**
     * 通过电话号码查询用户信息
     * @param s
     * @return
     */
    Customer selectByTelePhone(String s);

	Customer selectBySerialNumber(String number);


    List<Customer> selectByTelePhones(List<String> telePhones);

    List<Customer> getCommentCustomer(String startTime,Integer time,Integer type);

	List<Customer> selectShareCustomerList(String customerId, Integer currentPage, Integer showCount);

	List<Customer> selectBySelectMap(Map<String, Object> selectMap);

	 int updateCustomerWechatId(Customer customer);

	/**
	 * 根据账户余额Id查询用户信息
	 * @param accountId
	 * @return
	 */
	Customer selectByAccountId(String accountId);

	/**
	 * 获取客户消费信息， pos 2.0 小票使用
	 * @param shopId
	 * @param customerId
	 * @return
	 */
	Map getCustomerConsumeInfo(String shopId, String customerId);

	Map<String, Object> queryCustomerPaging(Map<String, Object> selectMap);

    /**
     * 根据unionId查看用户
     * @param unionId
     * @return
     */
	String selectCustomerByUnionId(String unionId);

	/**
	 * 获取除了这个customerId的所有unionId相同的用户
	 * @param unionId
	 * @param customerId
	 * @return
	 */
	List<Customer> selectByUnionIdNotCustomerId(String unionId, String customerId);

	/**
	 * 查询生日优惠券数量
	 * @return
	 */
	double selectBirthdayCoupons();
}

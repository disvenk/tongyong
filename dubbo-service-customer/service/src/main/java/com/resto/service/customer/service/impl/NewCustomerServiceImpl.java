package com.resto.service.customer.service.impl;

import com.resto.api.customer.entity.Account;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.exception.AppException;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.service.customer.service.impl.service.AccountService;
import com.resto.service.customer.service.impl.service.CustomerService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-17 11:06
 */

@RestController
public class NewCustomerServiceImpl implements NewCustomerService {

    @Resource
    CustomerService customerService;

    @Resource
    AccountService accountService;

    @Override
    public Customer dbSave(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Customer customer) {
        customerService.dbSave(customer);
        return customer;
    }

    @Override
    public int dbInsertList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody List<Customer> list) {
        return customerService.dbInsertList(list);
    }

    @Override
    public int dbDelete(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer customer) {
        return customerService.dbDelete(customer);
    }

    @Override
    public int dbDeleteByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object var) {
        return customerService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String var) {
        return customerService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer customer) {
        return customerService.dbUpdate(customer);
    }

    @Override
    public List<Customer> dbSelect(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId, @RequestBody Customer t) {
        return customerService.dbSelect(t);
    }

    @Override
    public List<Customer> dbSelectPage(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer t, Integer pageNum, Integer pageSize) {
        return customerService.dbSelectPage(t,pageNum, pageSize );
    }

    @Override
    public Customer dbSelectByPrimaryKey(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Object key) {
        return customerService.dbSelectByPrimaryKey(key);
    }

    @Override
    public Customer dbSelectOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer record) {
        return customerService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,@RequestBody Customer record) {
        return customerService.dbSelectCount(record);
    }

    @Override
    public List<Customer> dbSelectByIds(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String ids) {
        return customerService.dbSelectByIds(ids);
    }

    @Override
    public Customer login(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String openid) {
       return customerService.login(openid);
    }

    @Override
    public Customer selectById(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id){
        return customerService.selectById(id);
    }

    @Override
    public Customer register(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer){
        return customerService.register(customer);
    }

    @Override
    public Customer registerCard(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer){
       return customerService.registerCard(customer);
    }

    @Override
    public void updateCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer){
        customerService.updateCustomer(customer);
    }

    @Override
    public Customer bindPhone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String phone, String currentCustomerId,Integer couponType,String shopId,String shareCustomer,String shareOrderId) throws AppException{
        return customerService.bindPhone(phone,currentCustomerId ,couponType ,shopId ,shareCustomer ,shareOrderId );
    }

    @Override
    public Customer selectNickNameAndTelephone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId){
        return customerService.selectNickNameAndTelephone(customerId);
    }

    @Override
    public List<Customer> selectListByBrandId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentBrandId) {
        return customerService.selectListByBrandId(currentBrandId);
    }

    @Override
    public List<String> selectTelephoneList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId){
        return customerService.selectTelephoneList();
    }

    @Override
    public void unbindphone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String currentCustomerId){
         customerService.unbindphone(currentCustomerId);
    }

    @Override
    public void updateNewNoticeTime(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id){
        customerService.updateNewNoticeTime(id);
    }

    @Override
    public void updateFirstOrderTime(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id){
        customerService.updateFirstOrderTime(id);
    }

    @Override
    public Boolean checkRegistered(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String id){
       return customerService.checkRegistered(id);
    }

    @Override
    public Customer selectByOpenIdInfo(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String openId){
       return customerService.selectByOpenIdInfo(openId);
    }

    @Override
    public List<Customer> selectListByBrandIdHasRegister(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String beginDate, String endDate){
        return customerService.selectListByBrandIdHasRegister(beginDate,endDate ,brandId );
    }

    @Override
    public Customer selectCustomerAccount(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String telephone){
        return customerService.selectCustomerAccount(telephone);
    }

    @Override
    public Integer selectByShareCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId){
        return customerService.selectByShareCustomer(customerId);
    }

    @Override
    public List<Customer> selectBirthUser(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId){
        return customerService.selectBirthUser();
    }

    @Override
    public Customer selectByTelePhone(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String telePhone){
        return customerService.selectByTelePhone(telePhone);
    }

    @Override
    public Customer selectBySerialNumber(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String number){
        return customerService.selectBySerialNumber(number);
    }

    @Override
    public Customer getCustomerLimitOne(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId){
        return customerService.getCustomerLimitOne();
    }

    @Override
    public List<Customer> selectByTelePhones(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,List<String> telePhones){
        return customerService.selectByTelePhones(telePhones);
    }

    @Override
    public List<Customer> getCommentCustomer(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String startTime, Integer time,Integer type){
        return customerService.getCommentCustomer(startTime,time ,type );
    }

    @Override
    public List<Customer> selectShareCustomerList(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String customerId, Integer currentPage, Integer showCount){
        return customerService.selectShareCustomerList(customerId,currentPage ,showCount );
    }

    @Override
    public int updateCustomerWechatId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,Customer customer){
        return customerService.updateCustomerWechatId(customer);
    }

    @Override
    public Customer selectByAccountId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String accountId){
        return customerService.selectByAccountId(accountId);
    }

    @Override
    public String selectCustomerByUnionId(@ApiParam(value = "品牌id", required = true) @RequestParam(value ="brandId") String brandId,String unionId){
        return customerService.selectCustomerByUnionId(unionId);
    }
}

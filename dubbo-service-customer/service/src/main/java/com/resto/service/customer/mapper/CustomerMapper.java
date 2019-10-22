package com.resto.service.customer.mapper;

import com.resto.api.customer.entity.Customer;
import com.resto.conf.mybatis.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
    int registerCustomer(Customer customer);
    List<String> selectCustomerList();
    void changeLastOrderShop(String shopDetailId, String customerId);
    void updateNewNoticeTime(String customerId);
    void updateFirstOrderTime(String id);
    Integer checkRegistered(String id);
    /**
     * 查询某个时间段内的注册用户
     * @param begin
     * @param end
     * @return
     */
    List<Customer> selectListByBrandIdHasRegister(@Param("beginDate") Date begin, @Param("endDate") Date end, @Param("brandId") String brandId);

    Customer selectCustomerAccount(@Param("telephone") String telephone);

    Integer selectByShareCustomer(String customerId);

    List<Customer> selectBirthUser();

    Customer getCustomerLimitOne();

    List<Customer> selectByTelePhones(List<String> telePhones);

    List<Customer> getCommentCustomer(@Param("startTime") String startTime,@Param("time") Integer time,@Param("type") Integer type);

    List<Customer> selectShareCustomerList(@Param("customerId")String customerId,@Param("currentPage") Integer currentPage,@Param("showCount") Integer showCount);

    int updateCustomerWechatId(Customer customer);
}
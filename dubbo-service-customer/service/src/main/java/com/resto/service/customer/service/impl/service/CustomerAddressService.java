package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.entity.CustomerAddress;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountLogMapper;
import com.resto.service.customer.mapper.CustomerAddressMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by xielc on 2017/6/12.
 */
@Service
public class CustomerAddressService extends BaseServiceResto<CustomerAddress, CustomerAddressMapper> {


    @Resource
    CustomerAddressMapper customerAddressMapper;

    public int deleteByPrimaryKey(String id){
       return customerAddressMapper.deleteByPrimaryKey(id);
    }

    public int insert(CustomerAddress record){
        return dbSave(record);
    }

    public int insertSelective(CustomerAddress record){
        record.setId(ApplicationUtils.randomUUID());
        return  dbSave(record);
    }

    public CustomerAddress selectByPrimaryKey(String id){
        return dbSelectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(CustomerAddress record){
        if(record.getState()!=null){
            customerAddressMapper.updateState(record.getCustomerId());
        }
        record.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        return customerAddressMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(CustomerAddress record){
        return customerAddressMapper.updateByPrimaryKey(record);
    }

    public List<CustomerAddress> selectOneList(String customer_id) {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomerId(customer_id);
        return dbSelect(customerAddress);
    }

}

package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.shop.web.dao.CustomerAddressMapper;
import com.resto.shop.web.model.CustomerAddress;
import com.resto.shop.web.service.CustomerAddressService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by xielc on 2017/6/12.
 */
@Component
@Service
public class CustomerAddressServiceImpl extends GenericServiceImpl<CustomerAddress, String> implements CustomerAddressService {


    @Resource
    CustomerAddressMapper customerAddressMapper;

    @Override
    public GenericDao<CustomerAddress, String> getDao() {
        return customerAddressMapper;
    }

    @Override
    public int deleteByPrimaryKey(String id){
       return customerAddressMapper.deleteByPrimaryKey(id);
    }
    @Override
    public int insert(CustomerAddress record){
        return customerAddressMapper.insert(record);
    }
    @Override
    public int insertSelective(CustomerAddress record){
        record.setId(ApplicationUtils.randomUUID());
        return  customerAddressMapper.insertSelective(record);
    }
    @Override
    public CustomerAddress selectByPrimaryKey(String id){
        return customerAddressMapper.selectByPrimaryKey(id);
    }
    @Override
    public int updateByPrimaryKeySelective(CustomerAddress record){
        if(record.getState()!=null){
            customerAddressMapper.updateState(record.getCustomerId());
        }
        record.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        return customerAddressMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(CustomerAddress record){
        return customerAddressMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<CustomerAddress> selectOneList(String customer_id) {
        return customerAddressMapper.selectOneList(customer_id);
    }

}

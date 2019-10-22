package com.resto.service.shop.service;


import com.alibaba.fastjson.JSON;
import com.resto.api.shop.dto.CustomerAddressDto;
import com.resto.conf.util.ApplicationUtils;
import com.resto.core.OrikaBeanMapper;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.CustomerAddress;
import com.resto.service.shop.mapper.CustomerAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xielc on 2017/6/12.
 */
@Service
public class CustomerAddressService extends BaseService<CustomerAddress, String> {

    @Autowired
    CustomerAddressMapper customerAddressMapper;

    @Autowired
    private OrikaBeanMapper mapper;

    public BaseDao<CustomerAddress, String> getDao() {
        return customerAddressMapper;
    }

    public int insertSelective(CustomerAddressDto record){
        record.setId(ApplicationUtils.randomUUID());
        CustomerAddress customerAddress = JSON.parseObject(JSON.toJSONString(record), CustomerAddress.class);
        return  customerAddressMapper.insertSelective(customerAddress);
    }

    public CustomerAddress selectByPrimaryKey(String id){
        return customerAddressMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(CustomerAddressDto record){
        if(record.getState()!=null){
            customerAddressMapper.updateState(record.getCustomerId());
        }
        record.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        CustomerAddress customerAddress = JSON.parseObject(JSON.toJSONString(record), CustomerAddress.class);
        return customerAddressMapper.updateByPrimaryKeySelective(customerAddress);
    }

    public List<CustomerAddress> selectOneList(String customer_id) {
        return customerAddressMapper.selectOneList(customer_id);
    }

}

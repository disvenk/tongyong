package com.resto.service.customer.service.impl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.customer.entity.AccountLog;
import com.resto.api.customer.entity.CustomerGroup;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.conf.mybatis.base.BaseServiceResto;
import com.resto.service.customer.mapper.AccountLogMapper;
import com.resto.service.customer.mapper.CustomerGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by carl on 2017/9/25.
 */
@Component
@Service
public class CustomerGroupService extends BaseServiceResto<CustomerGroup, CustomerGroupMapper> {

    @Resource
    CustomerGroupMapper customerGroupMapper;

    public List<CustomerGroup> getGroupByGroupId(String groupId) {
        return customerGroupMapper.getGroupByGroupId(groupId);
    }

    public CustomerGroup getGroupByCustomerId(String customerId, String shopId, String tableNumber) {
            return customerGroupMapper.getGroupByCustomerId(customerId, shopId, tableNumber);
    }

    public List<CustomerGroup> getGroupByShopCart(String groupId) {
        return customerGroupMapper.getGroupByShopCart(groupId);
    }

    public void removeByGroupId(String groupId) {
        CustomerGroup customerGroup = new CustomerGroup();
        customerGroup.setGroupId(groupId);
        dbDelete(customerGroup);
    }
}

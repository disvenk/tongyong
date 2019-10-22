package com.resto.service.customer.service.impl;

import com.resto.api.customer.entity.CustomerAddress;
import com.resto.api.customer.service.NewCustomerAddressService;
import com.resto.service.customer.service.impl.service.CustomerAddressService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by disvenk.dai on 2018-10-31 14:48
 */

@RestController
public class NewCustomerAddressServiceImpl implements NewCustomerAddressService {

    @Resource
    CustomerAddressService customerAddressService;

    @Override
    public CustomerAddress dbSave(String brandId, CustomerAddress customerAddress) {
        customerAddressService.dbSave(customerAddress);
        return customerAddress;
    }

    @Override
    public int dbInsertList(String brandId, List<CustomerAddress> list) {
        return  customerAddressService.dbInsertList(list);
    }

    @Override
    public int dbDelete(String brandId, CustomerAddress customerAddress) {
        return customerAddressService.dbDelete(customerAddress);
    }

    @Override
    public int dbDeleteByPrimaryKey(String brandId, Object var) {
        return customerAddressService.dbDeleteByPrimaryKey(var.toString());
    }

    @Override
    public int dbDeleteByIds(String brandId, String var) {
        return customerAddressService.dbDeleteByIds(var);
    }

    @Override
    public int dbUpdate(String brandId, CustomerAddress customerAddress) {
        return customerAddressService.dbUpdate(customerAddress);
    }

    @Override
    public List<CustomerAddress> dbSelect(String brandId, CustomerAddress t) {
        return customerAddressService.dbSelect(t);
    }

    @Override
    public List<CustomerAddress> dbSelectPage(String brandId, CustomerAddress t, Integer pageNum, Integer pageSize) {
        return customerAddressService.dbSelectPage(t,pageNum ,pageSize );
    }

    @Override
    public CustomerAddress dbSelectByPrimaryKey(String brandId, Object key) {
        return customerAddressService.dbSelectByPrimaryKey(key);
    }

    @Override
    public CustomerAddress dbSelectOne(String brandId, CustomerAddress record) {
        return customerAddressService.dbSelectOne(record);
    }

    @Override
    public int dbSelectCount(String brandId, CustomerAddress record) {
        return customerAddressService.dbSelectCount(record);
    }

    @Override
    public List<CustomerAddress> dbSelectByIds(String brandId, String ids) {
        return customerAddressService.dbSelectByIds(ids);
    }

    @Override
    public int deleteByPrimaryKey(String brandId, String id) {
        return customerAddressService.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(String brandId, CustomerAddress record) {
        return customerAddressService.insert(record);
    }

    @Override
    public int insertSelective(String brandId, CustomerAddress record) {
        return customerAddressService.insertSelective(record);
    }

    @Override
    public CustomerAddress selectByPrimaryKey(String brandId, String id) {
        return customerAddressService.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(String brandId, CustomerAddress record) {
        return customerAddressService.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(String brandId, CustomerAddress record) {
        return customerAddressService.updateByPrimaryKey(record);
    }

    @Override
    public List<CustomerAddress> selectOneList(String brandId, String customer_id) {
        return customerAddressService.selectOneList(customer_id);
    }
}

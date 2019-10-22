package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.EmployeeMapper;
import com.resto.brand.web.model.Employee;
import com.resto.brand.web.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2016/9/5.
 */
@Component
@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, String> implements EmployeeService {


    @Autowired
    private EmployeeMapper employeeMapper;


    @Override
    public Employee getEmployeeByPhone(String phone) {
        return employeeMapper.getEmployeeByPhone(phone);
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeMapper.getEmployeeById(id);
    }

    @Override
    public Employee selectOneBytelephone(String phone) {
        return employeeMapper.selectOneBytelephone(phone);
    }

    @Override
    public int updateSelect(Employee employee) {
         return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    @Override
    public GenericDao<Employee, String> getDao() {
        return employeeMapper;
    }
}

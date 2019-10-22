package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Employee;

/**
 * Created by KONATA on 2016/9/5.
 */
public interface EmployeeService extends GenericService<Employee, String> {
    Employee getEmployeeByPhone(String phone);

    Employee getEmployeeById(String id);

    /**
     * 根据电话号码查询(无需state=1)
     * @param phone
     * @return
     */
    Employee selectOneBytelephone(String phone);

    /**
     * 更新用户信息
     * @param employee
     */
    int   updateSelect(Employee employee);
}

package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Employee;

/**
 * Created by KONATA on 2016/9/5.
 */
public interface EmployeeMapper  extends BaseDao<Employee,String> {

    Employee getEmployeeByPhone(String phone);

    Employee getEmployeeById(String id);

    /**
     * 根据电话号码查询(不需要状态=1)
     * @param phone
     * @return
     */
    Employee selectOneBytelephone(String phone);

    /**
     * 更新用户信息
     * @param employee
     */
    void updateSelect(Employee employee);
}

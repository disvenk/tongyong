package com.resto.shop.web.dao;

import com.resto.shop.web.model.Employee;
import com.resto.brand.core.generic.GenericDao;

public interface EmployeeMapper  extends GenericDao<Employee,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Long id);

    Employee selectEmployeeByTel(String tel);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    Employee selectOneById(Long id);
}

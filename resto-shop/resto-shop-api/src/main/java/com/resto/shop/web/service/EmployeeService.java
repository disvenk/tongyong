package com.resto.shop.web.service;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.BrandUser;
import com.resto.shop.web.model.Employee;


public interface EmployeeService extends GenericService<Employee, Long> {

    /**
     * 创建一个员工的信息
     * @param employee
     * @param currentBrandUser
     */
    Result insertOne(Employee employee, BrandUser currentBrandUser,String brandId);

    /**
     * 获取员工信息
     * @param id
     * @return
     */
    Employee selectEmployeeInfo(Long id);

    Employee selectEmployeeByTel(String tel);

    /**
     * 获取员工的店铺角色信息
     * @param employeeId
     * @return
     */
    Employee selectOneById(Long employeeId);

    //更新员工的信息(包含其角色信息)
    void updateSelected(Long employeeId, String id, BrandUser currentBrandUser);

    //判断手机号是否被注册
    Result checkeTelephone(String telephone);

     Result updateEmployee(Long id);
}

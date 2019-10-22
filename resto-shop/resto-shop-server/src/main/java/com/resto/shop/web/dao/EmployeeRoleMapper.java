package com.resto.shop.web.dao;

import com.resto.shop.web.model.EmployeeRole;

import java.util.List;

public interface EmployeeRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EmployeeRole record);

    int insertSelective(EmployeeRole record);

    EmployeeRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmployeeRole record);

    int updateByPrimaryKey(EmployeeRole record);

    int deleteByIds(List<Long> ids);

}
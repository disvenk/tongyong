package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.BrandUser;
import com.resto.shop.web.dao.EmployeeMapper;
import com.resto.shop.web.dao.EmployeeRoleMapper;
import com.resto.shop.web.model.Employee;
import com.resto.shop.web.model.EmployeeRole;
import com.resto.shop.web.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 *
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Long> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private EmployeeRoleMapper employeeRoleMapper;

    @Resource
    private com.resto.brand.web.service.EmployeeService employeeBrandService;

    @Override
    public GenericDao<Employee, Long> getDao() {
        return employeeMapper;
    }

    @Override
    public Result insertOne(Employee employee, BrandUser brandUser,String brandId) {
        Result r = new Result();
       //判断是否是手机号码是否被注册过
        com.resto.brand.web.model.Employee employee2 = employeeBrandService.selectOneBytelephone(employee.getTelephone());
        if(null!=employee2){
            r.setSuccess(false);
            r.setMessage("该手机号码已经注册过请重新填写");
        }else {
            com.resto.brand.web.model.Employee employee3 = new com.resto.brand.web.model.Employee();
            employee3.setId(ApplicationUtils.randomUUID());
            employee3.setState(1);
            employee3.setBrandId(brandId);
            employee3.setTelephone(employee.getTelephone());
            //1在brand端插入数据
            employeeBrandService.insert(employee3);

            //在shop端插入数据
            //设置创建时间
            employee.setCreateTime(new Date());
            //设置创建人
            employee.setCreateUser(brandUser.getUsername());
            //设置状态为正常
            employee.setState((byte)1);
            //设置qr
            employee.setQrCode(employee3.getId());

            //保存员工信息
            employeeMapper.insertSelective(employee);
            r.setSuccess(true);
        }
        return  r;

    }


    @Override
    public Employee selectEmployeeInfo(Long id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    @Override
    public Employee selectEmployeeByTel(String tel) {
        return employeeMapper.selectEmployeeByTel(tel);
    }

    @Override
    public Employee selectOneById(Long id) {
        return employeeMapper.selectOneById(id);
    }

    @Override
    public void updateSelected(Long employeeId, String id, BrandUser brandUser) {

        //查询员工信息
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        employee.setUpdateTime(new Date());
        employee.setUpdateUser(brandUser.getUsername());
        //更新员工基本信息
        employeeMapper.updateByPrimaryKeySelective(employee);

        //查询员工对应的员工角色
        Employee employee3 = employeeMapper.selectOneById(employeeId);
        //拼接店铺角色(如果有角色信息)
        List<Long> ids = new ArrayList<>();

        if(employee3!=null){
            if (employee3.getEmployeeRoleList() != null && employee3.getEmployeeRoleList().size() > 0) {
                for (EmployeeRole er : employee3.getEmployeeRoleList()) {
                    Long key = er.getId();
                    ids.add(key);
                }
                //删除掉所有该用户有的店铺角色
                employeeRoleMapper.deleteByIds(ids);
            }
        }

            //更新员工的角色信息
            if(id!=null||"".equals(id)){
                String[]  arr2 =  StringUtils.split(id,",");
                for(String shopId_roleId : arr2){
                    String[] arr3 =StringUtils.split(shopId_roleId,"_");
                    List<String> arr4 = Arrays.asList(arr3);
                    EmployeeRole er = new EmployeeRole();
                    for (int i = 0; i <arr4.size(); i++) {
                        er.setEmployeeId(employeeId);
                        er.setShopId(arr4.get(0));
                        er.setRoleId(Long.parseLong(arr4.get(1)));
                    }
                    employeeRoleMapper.insertSelective(er);

                }
                //设置brand端的employee状态为1
                com.resto.brand.web.model.Employee employee4 = employeeBrandService.selectOneBytelephone(employee.getTelephone());
                if (employee4 != null) {
                    employee4.setState(1);
                    employeeBrandService.updateSelect(employee4);
                }
            }
    }

    @Override
    public Result checkeTelephone(String telephone) {
         Result r = new Result();
         com.resto.brand.web.model.Employee employee= employeeBrandService.selectOneBytelephone(telephone);
        if(null!=employee){
            r.setSuccess(false);
            r.setMessage("该手机号已经注册");
        }else {
            r.setSuccess(true);
        }
        return r;
    }

    @Override
    public Result updateEmployee(Long id) {
        Result r = new Result();
        //查询
        Employee employee = employeeMapper.selectOneById(id);
        if(null==employee){
            r.setSuccess(false);
            r.setMessage("该用户不存在删除失败");
        }else {
            //更新shop端
            employee.setState((byte)0);
            employeeMapper.updateByPrimaryKey(employee);
            //更新brand端
            com.resto.brand.web.model.Employee employee2 = employeeBrandService.selectOneBytelephone(employee.getTelephone());
            employee2.setState(0);
            r.setSuccess(true);
        }

        return r;
    }

}

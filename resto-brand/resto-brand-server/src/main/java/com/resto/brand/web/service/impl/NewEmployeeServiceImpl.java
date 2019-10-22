package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.NewEmployeeMapper;
import com.resto.brand.web.model.NewEmployee;
import com.resto.brand.web.service.NewEmployeeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class NewEmployeeServiceImpl extends GenericServiceImpl<NewEmployee, String> implements NewEmployeeService {

    @Resource
    private NewEmployeeMapper newemployeeMapper;

    @Override
    public GenericDao<NewEmployee, String> getDao() {
        return newemployeeMapper;
    }

    @Override
    public List<NewEmployee> selectByIds(List<String> ids) {
        return newemployeeMapper.selectByIds(ids);
    }

    @Override
    public List<NewEmployee> selectByShopId(String shopId) {
        return newemployeeMapper.selectByShopId(shopId);
    }

    @Override
    public List<NewEmployee> selectByBrandId(String brandId) {
        return newemployeeMapper.selectByBrandId(brandId);
    }

    @Override
    public NewEmployee selectByTelephone(String telePhone) {
        return newemployeeMapper.selectByTelephone(telePhone);
    }
}

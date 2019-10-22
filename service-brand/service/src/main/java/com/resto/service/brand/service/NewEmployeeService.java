package com.resto.service.brand.service;


import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.NewEmployee;
import com.resto.service.brand.mapper.NewEmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class NewEmployeeService extends BaseService<NewEmployee, String> {

    @Autowired
    private NewEmployeeMapper newemployeeMapper;

    public BaseDao<NewEmployee, String> getDao() {
        return newemployeeMapper;
    }

    public NewEmployee selectByTelephone(String telePhone) {
        return newemployeeMapper.selectByTelephone(telePhone);
    }
}

package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.SysErrorMapper;
import com.resto.brand.web.model.SysError;
import com.resto.brand.web.service.SysErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2016/11/18.
 */
@Component
@Service
public class SysErrorServiceImpl extends GenericServiceImpl<SysError, Long> implements SysErrorService  {


    @Autowired
    private SysErrorMapper sysErrorMapper;

    @Override
    public GenericDao<SysError, Long> getDao() {
        return sysErrorMapper;
    }
}

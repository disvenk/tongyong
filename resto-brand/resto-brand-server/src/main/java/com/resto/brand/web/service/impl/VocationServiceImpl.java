package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.VocationMapper;
import com.resto.brand.web.model.Vocation;
import com.resto.brand.web.service.VocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by carl on 2016/12/28.
 */
@Component
@Service
public class VocationServiceImpl extends GenericServiceImpl<Vocation, Integer> implements VocationService {

    @Autowired
    private VocationMapper vocationMapper;


    @Override
    public GenericDao<Vocation, Integer> getDao() {
        return vocationMapper;
    }
}

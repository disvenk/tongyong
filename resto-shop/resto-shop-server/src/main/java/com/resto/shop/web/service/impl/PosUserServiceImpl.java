package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.PosUserMapper;
import com.resto.shop.web.model.PosUser;
import com.resto.shop.web.service.PosUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class PosUserServiceImpl extends GenericServiceImpl<PosUser, Long> implements PosUserService {

    @Resource
    private PosUserMapper posuserMapper;

    @Override
    public GenericDao<PosUser, Long> getDao() {
        return posuserMapper;
    } 

}

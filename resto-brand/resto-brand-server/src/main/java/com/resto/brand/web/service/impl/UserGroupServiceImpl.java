package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.UserGroupMapper;
import com.resto.brand.web.model.UserGroup;
import com.resto.brand.web.service.UserGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class UserGroupServiceImpl extends GenericServiceImpl<UserGroup, Long> implements UserGroupService {

    @Resource
    private UserGroupMapper usergroupMapper;

    @Override
    public GenericDao<UserGroup, Long> getDao() {
        return usergroupMapper;
    } 

}

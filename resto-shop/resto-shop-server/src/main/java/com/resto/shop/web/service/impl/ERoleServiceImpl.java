package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ERoleMapper;
import com.resto.shop.web.model.ERole;
import com.resto.shop.web.service.ERoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
class ERoleServiceImpl extends GenericServiceImpl<ERole, Long> implements ERoleService {

    @Resource
    private ERoleMapper eroleMapper;

    @Override
    public GenericDao<ERole, Long> getDao() {
        return eroleMapper;
    }


    @Override
    public List<ERole> selectByStauts() {
        return eroleMapper.selectByStauts();
    }
}

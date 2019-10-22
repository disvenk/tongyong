package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.PosUserMapper;
import com.resto.brand.web.model.PosUser;
import com.resto.brand.web.service.PosUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2017/6/20.
 */
@Component
@Service
public class PosUserServiceImpl extends GenericServiceImpl<PosUser, Long> implements PosUserService {

    @Autowired
    private PosUserMapper posUserMapper;

    @Override
    public GenericDao<PosUser, Long> getDao() {
        return posUserMapper;
    }

    @Override
    public PosUser getUserByIp(String ip) {
        return posUserMapper.getUserByIp(ip);
    }

    @Override
    public void clearAll() {
        posUserMapper.clearAll();
    }

    @Override
    public void deleteUserByIp(String ip) {
        posUserMapper.deleteUserByIp(ip);
    }

    @Override
    public void insertPosUser(PosUser posUser) {
        posUserMapper.insertPosUser(posUser);
    }
}

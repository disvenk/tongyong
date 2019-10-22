package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.TokenMapper;
import com.resto.brand.web.model.SysLoginLog;
import com.resto.brand.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KONATA on 2016/9/5.
 */
@Component
@Service
public class TokenServiceImpl extends GenericServiceImpl<SysLoginLog, Integer> implements TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public SysLoginLog queryToken(String userid) {
        return tokenMapper.queryToken(userid);
    }

    @Override
    public int deleteToken(String userId) {
        return tokenMapper.deleteToken(userId);
    }

    @Override
    public int insertToken(SysLoginLog sysLoginLog) {
        return tokenMapper.insertToken(sysLoginLog);
    }

    @Override
    public int updateToken(SysLoginLog sysLoginLog) {
        return tokenMapper.updateToken(sysLoginLog);
    }

    @Override
    public GenericDao<SysLoginLog, Integer> getDao() {
        return tokenMapper;
    }
}

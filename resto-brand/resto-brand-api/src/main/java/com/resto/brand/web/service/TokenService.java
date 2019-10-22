package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.SysLoginLog;

/**
 * Created by KONATA on 2016/9/5.
 */
public interface TokenService extends GenericService<SysLoginLog, Integer> {

    SysLoginLog queryToken(String userid);

    int deleteToken(String userId);

    int insertToken(SysLoginLog sysLoginLog);

    int updateToken(SysLoginLog sysLoginLog);
}

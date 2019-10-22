package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.SysLoginLog;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KONATA on 2016/9/5.
 */
public interface TokenMapper extends GenericDao<SysLoginLog,Integer> {
    SysLoginLog queryToken(@Param("userid") String userid);

    int deleteToken(@Param("userid") String userid);

    int insertToken(SysLoginLog sysLoginLog);

    int updateToken(SysLoginLog sysLoginLog);


}

package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.SysLoginLog;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KONATA on 2016/9/5.
 */
public interface TokenMapper extends BaseDao<SysLoginLog,Integer> {
    SysLoginLog queryToken(@Param("userid") String userid);

    int deleteToken(@Param("userid") String userid);

    int insertToken(SysLoginLog sysLoginLog);

    int updateToken(SysLoginLog sysLoginLog);


}

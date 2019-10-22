package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.PosUser;

/**
 * Created by KONATA on 2017/6/20.
 */
public interface PosUserMapper extends BaseDao<PosUser, Long> {
    PosUser getUserByIp(String ip);

    void clearAll();

    void deleteUserByIp(String ip);
}

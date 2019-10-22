package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.PosUser;

/**
 * Created by KONATA on 2017/6/20.
 */
public interface PosUserMapper extends GenericDao<PosUser, Long> {
    PosUser getUserByIp(String ip);

    void clearAll();

    void deleteUserByIp(String ip);

    /**
     * 插入PosUser
     * @param posUser
     */
    void insertPosUser(PosUser posUser);
}

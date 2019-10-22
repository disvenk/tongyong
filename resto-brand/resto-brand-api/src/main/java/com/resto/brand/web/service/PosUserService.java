package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.PosUser;

/**
 * Created by KONATA on 2017/6/20.
 */
public interface PosUserService extends GenericService<PosUser, Long> {
    PosUser getUserByIp(String ip);

    void clearAll();

    void deleteUserByIp(String ip);

    /**
     * 插入PosUser
     * @param posUser
     */
    void insertPosUser(PosUser posUser);

}

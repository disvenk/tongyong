package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.VCode;

/**
 * Created by carl on 2016/9/5.
 */
public interface VCodeMapper extends GenericDao<VCode, Long> {

    int insert(VCode vcode);

    int insertVCode(VCode vcode);

    VCode selectPhoneList(String phone);
}

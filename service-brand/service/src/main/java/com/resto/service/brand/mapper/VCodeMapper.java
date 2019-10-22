package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.VCode;

/**
 * Created by carl on 2016/9/5.
 */
public interface VCodeMapper extends BaseDao<VCode, Long> {

    int insert(VCode vcode);

    int insertVCode(VCode vcode);

    VCode selectPhoneList(String phone);
}

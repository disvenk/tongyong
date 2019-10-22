package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.VCode;


/**
 * Created by carl on 2016/9/5.
 */
public interface VCodeService extends GenericService<VCode, Long> {

    String insertVCode(String phone);

    VCode selectLastPhoneInfo(String phone);
}

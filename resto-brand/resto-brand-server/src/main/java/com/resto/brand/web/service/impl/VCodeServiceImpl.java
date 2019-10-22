package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.VCodeMapper;
import com.resto.brand.web.model.VCode;
import com.resto.brand.web.service.VCodeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by carl on 2016/9/5.
 */
@Component
@Service
public class VCodeServiceImpl extends GenericServiceImpl<VCode, Long> implements VCodeService {

    @Resource
    private VCodeMapper vCodeMapper;

    @Override
    public GenericDao<VCode, Long> getDao() { return vCodeMapper; }

    @Override
    public String insertVCode(String phone) {
        VCode vcode = new VCode();
        vcode.setTel(phone);
        vcode.setCreateTime(new Date());

        String code = RandomStringUtils.randomNumeric(4);

        vcode.setvCode(code+"");
        vCodeMapper.insertVCode(vcode);
        return vcode.getvCode();
    }

    @Override
    public VCode selectLastPhoneInfo(String phone) {
        return vCodeMapper.selectPhoneList(phone);
    }
}

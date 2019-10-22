package com.resto.shop.web.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.SvipActivityMapper;
import com.resto.shop.web.model.SvipActivity;
import com.resto.shop.web.service.SvipActivityService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Service
@Component
public class SvipActivityServiceImpl extends GenericServiceImpl<SvipActivity, String> implements SvipActivityService {

    @Resource
    private SvipActivityMapper svipactivityMapper;

    @Override
    public GenericDao<SvipActivity, String> getDao() {
        return svipactivityMapper;
    }

    @Override
    public Long getOpendAct() {
        return svipactivityMapper.getOpendAct();
    }

    @Override
    public SvipActivity getAct() {
        //
        return svipactivityMapper.getAct();
    }

    @Override
    public Long getMyself(String id) {
        return svipactivityMapper.getMyself(id);
    }


}

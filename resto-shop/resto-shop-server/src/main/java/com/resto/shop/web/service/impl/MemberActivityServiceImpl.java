package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MemberActivityMapper;
import com.resto.shop.web.model.MemberActivity;
import com.resto.shop.web.service.MemberActivityService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Service
public class MemberActivityServiceImpl extends GenericServiceImpl<MemberActivity, Integer> implements MemberActivityService {

    @Resource
    private MemberActivityMapper memberActivityMapper;

    @Override
    public GenericDao<MemberActivity, Integer> getDao() {
        return memberActivityMapper;
    }
}

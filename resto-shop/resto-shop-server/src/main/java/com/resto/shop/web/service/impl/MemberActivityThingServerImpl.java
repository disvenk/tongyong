package com.resto.shop.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.MemberActivityThingMapper;
import com.resto.shop.web.model.MemberActivityThing;
import com.resto.shop.web.service.MemberActivityThingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Service
public class MemberActivityThingServerImpl extends GenericServiceImpl<MemberActivityThing, Integer> implements MemberActivityThingService {

    @Resource
    private MemberActivityThingMapper memberActivityThingMapper;

    @Override
    public GenericDao<MemberActivityThing, Integer> getDao() {
        return memberActivityThingMapper;
    }

    @Override
    public MemberActivityThing selectByTelephone(String telephone) {
        return memberActivityThingMapper.selectByTelephone(telephone);
    }

    @Override
    public List<JSONObject> selectCustomerInfo(Integer memberActivityId) {
        return memberActivityThingMapper.selectCustomerInfo(memberActivityId);
    }
}

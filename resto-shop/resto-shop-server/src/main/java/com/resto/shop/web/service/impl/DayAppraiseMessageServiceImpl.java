package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.DayAppraiseMessageMapper;
import com.resto.shop.web.model.DayAppraiseMessage;
import com.resto.shop.web.service.DayAppraiseMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
@Service
public class DayAppraiseMessageServiceImpl extends GenericServiceImpl<DayAppraiseMessage, String> implements DayAppraiseMessageService {

    @Resource
    private DayAppraiseMessageMapper dayappraisemessageMapper;

    @Override
    public GenericDao<DayAppraiseMessage, String> getDao() {
        return dayappraisemessageMapper;
    } 

}

package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.RefundRemarkMapper;
import com.resto.brand.web.model.RefundRemark;
import com.resto.brand.web.service.RefundRemarkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class RefundRemarkServiceImpl extends GenericServiceImpl<RefundRemark, Integer> implements RefundRemarkService {

    @Resource
    private RefundRemarkMapper refundremarkMapper;

    @Override
    public GenericDao<RefundRemark, Integer> getDao() {
        return refundremarkMapper;
    } 

}

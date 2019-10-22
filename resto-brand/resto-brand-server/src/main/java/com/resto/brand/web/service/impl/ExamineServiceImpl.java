package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.ExamineMapper;
import com.resto.brand.web.model.Examine;
import com.resto.brand.web.service.ExamineService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 */
@Component
@Service
public class ExamineServiceImpl extends GenericServiceImpl<Examine, Long> implements ExamineService {

    @Resource
    private ExamineMapper examineMapper;

    @Override
    public GenericDao<Examine, Long> getDao() {
        return examineMapper;
    }

    @Override
    public Examine selectByContractIdAndType(Long contractId, Integer type) {
        return examineMapper.selectByContractIdAndType(contractId,type);

    }
}

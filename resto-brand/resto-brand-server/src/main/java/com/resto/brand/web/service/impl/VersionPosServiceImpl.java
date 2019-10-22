package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.VersionPosMapper;
import com.resto.brand.web.model.VersionPos;
import com.resto.brand.web.service.VersionPosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class VersionPosServiceImpl extends GenericServiceImpl<VersionPos,Integer> implements VersionPosService {

    @Autowired
    VersionPosMapper versionPosMapper;
    @Override
    public GenericDao<VersionPos, Integer> getDao() {
        return versionPosMapper;
    }
}

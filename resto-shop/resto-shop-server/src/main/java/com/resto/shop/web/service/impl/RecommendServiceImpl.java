package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.RecommendMapper;
import com.resto.shop.web.model.Recommend;
import com.resto.shop.web.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service
public class RecommendServiceImpl extends GenericServiceImpl<Recommend,String>implements RecommendService {
    @Autowired
    RecommendMapper recommendMapper;
    @Override
    public GenericDao<Recommend, String> getDao() {
        return recommendMapper;
    }
}

package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.WeBrandScoreMapper;
import com.resto.brand.web.model.WeBrandScore;
import com.resto.brand.web.service.WeBrandScoreService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 */
@Component
@Service
public class WeBrandScoreServiceImpl extends GenericServiceImpl<WeBrandScore, Integer> implements WeBrandScoreService {

    @Resource
    private WeBrandScoreMapper webrandscoreMapper;

    @Override
    public GenericDao<WeBrandScore, Integer> getDao() {
        return webrandscoreMapper;
    }

    @Override
    public WeBrandScore selectByBrandIdAndCreateTime(String brandId, Date createTime) {
        return webrandscoreMapper.selectByBrandIdAndCreateTime(brandId,createTime);
    }

}

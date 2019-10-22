package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ExperienceMapper;
import com.resto.shop.web.model.Experience;
import com.resto.shop.web.service.ExperienceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ExperienceServiceImpl extends GenericServiceImpl<Experience, Integer> implements ExperienceService {

    @Resource
    private ExperienceMapper experienceMapper;

    @Override
    public GenericDao<Experience, Integer> getDao() {
        return experienceMapper;
    }

    @Override
    public int deleteByTitle(String title, String shopId) {
        return experienceMapper.deleteByTitle(title, shopId);
    }

    @Override
    public List<Experience> selectListByShopId(String shopId) {
        return experienceMapper.selectListByShopId(shopId);
    }
}

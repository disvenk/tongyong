package com.resto.service.shop.service;

import com.resto.core.OrikaBeanMapper;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.entity.Experience;
import com.resto.service.shop.mapper.ExperienceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class ExperienceService extends BaseService<Experience, Integer> {

    @Autowired
    private ExperienceMapper experienceMapper;

    @Autowired
    private OrikaBeanMapper mapper;

    public BaseDao<Experience, Integer> getDao() {
        return experienceMapper;
    }

    public List<Experience> selectListByShopId(String shopId) {
        return experienceMapper.selectListByShopId(shopId);
    }
}

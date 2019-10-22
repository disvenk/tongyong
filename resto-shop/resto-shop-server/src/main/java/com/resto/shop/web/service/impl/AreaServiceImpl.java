package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.AreaMapper;
import com.resto.shop.web.model.Area;
import com.resto.shop.web.service.AreaService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by KONATA on 2017/4/5.
 */
@Component
@Service
public class AreaServiceImpl extends GenericServiceImpl<Area, Long> implements AreaService {


    @Resource
    private AreaMapper areaMapper;

    @Override
    public GenericDao<Area, Long> getDao() {
        return areaMapper;
    }


    @Override
    public List<Area> getAreaList(String shopId) {
        return areaMapper.getAreaList(shopId);
    }



    @Override
    public void deleteArea(String printId) {
        areaMapper.deleteArea(printId);
    }
}

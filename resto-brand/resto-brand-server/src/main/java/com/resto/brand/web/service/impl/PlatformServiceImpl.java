package com.resto.brand.web.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.dao.PlatformMapper;
import com.resto.brand.web.model.Platform;
import com.resto.brand.web.model.ThirdParam;
import com.resto.brand.web.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by KONATA on 2016/11/1.
 * 第三方平台设置实现
 */
@Component
@Service
public class PlatformServiceImpl  extends GenericServiceImpl<Platform, Long> implements PlatformService {

    @Autowired
    private PlatformMapper platformMapper;

    @Override
    public GenericDao<Platform, Long> getDao() {
        return platformMapper;
    }

    @Override
    public List<Platform> selectAll() {
        return platformMapper.selectAll();
    }


    @Override
    public void deleteConfig(String brandId) {
        platformMapper.deleteConfig(brandId);
    }

    @Override
    public int insertConfig(String brandId, String platformId) {
        return platformMapper.insertConfig(brandId, platformId);
    }


    @Override
    public List<Platform> selectByBrandId(String brandId) {
        return platformMapper.selectByBrandId(brandId);
    }
    
    @Override
    public List<Platform> selectPlatformByBrandId(String brandId) {
    	return platformMapper.selectPlatformByBrandId(brandId);
    }

    @Override
    public List<ThirdParam> selectParamList() {
        return platformMapper.selectParamList();
    }

    @Override
    public int insertParam(ThirdParam param) {
        return platformMapper.insertParam(param);
    }

    @Override
    public void deleteParam(Long id) {
        platformMapper.deleteParam(id);
    }

    @Override
    public String[] getParamByBrandId(String brandId) {
        return platformMapper.getParamByBrandId(brandId);
    }
}

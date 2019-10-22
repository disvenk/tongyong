package com.resto.brand.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.model.Platform;
import com.resto.brand.web.model.ThirdParam;

import java.util.List;

/**
 * Created by KONATA on 2016/11/1.
 * 第三方平台设置
 */

public interface PlatformService extends GenericService<Platform, Long> {

    List<Platform> selectAll();

    int insertConfig(String brandId,String platformId);

    void deleteConfig(String brandId);

    List<Platform> selectByBrandId(String brandId);
    
    List<Platform> selectPlatformByBrandId(String brandId);

    List<ThirdParam> selectParamList();

    int insertParam(ThirdParam param);

    void deleteParam(Long id);

    String [] getParamByBrandId(String brandId);
}

package com.resto.service.brand.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.Platform;
import com.resto.service.brand.entity.ThirdParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KONATA on 2016/11/1.
 */
public interface PlatformMapper extends BaseDao<Platform,Long> {
    List<Platform> selectAll();

    int insertConfig(@Param("brandId") String brandId, @Param("platformId") String platformId);

    void deleteConfig(String brandId);

    List<Platform> selectByBrandId(String brandId);
    
    List<Platform> selectPlatformByBrandId(@Param("brandId") String brandId);

    List<ThirdParam> selectParamList();

    int insertParam(ThirdParam thirdParam);

    void deleteParam(Long id);

    String [] getParamByBrandId(String brandId);
}

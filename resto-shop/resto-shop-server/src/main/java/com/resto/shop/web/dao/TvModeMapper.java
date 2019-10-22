package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.TvMode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TvModeMapper  extends GenericDao<TvMode,Integer> {
    int insert(TvMode record);

    int insertSelective(TvMode record);

    TvMode selectByDeviceShopIdSource(@Param("shopId") String shopId, @Param("deviceToken") String deviceToken, @Param("appSource") Integer appSource);

    List<TvMode> selectByShopIdSource(@Param("shopId") String shopId, @Param("appSource") Integer appSource);
}

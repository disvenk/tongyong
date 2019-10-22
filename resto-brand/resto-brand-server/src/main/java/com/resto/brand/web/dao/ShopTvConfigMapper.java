package com.resto.brand.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.ShopTvConfig;

/**
 * Created by carl on 2017/7/14.
 */
public interface ShopTvConfigMapper extends GenericDao<ShopTvConfig,Long> {
    int deleteByPrimaryKey(String id);

    int insert(ShopDetail record);

    int insertSelective(ShopDetail record);

    ShopDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShopDetail record);

    int updateByPrimaryKey(ShopDetail record);

    ShopTvConfig selectByShopId(String shopId);

}

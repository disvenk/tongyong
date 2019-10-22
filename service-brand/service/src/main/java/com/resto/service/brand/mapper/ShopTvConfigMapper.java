package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.ShopDetail;
import com.resto.service.brand.entity.ShopTvConfig;

/**
 * Created by carl on 2017/7/14.
 */
public interface ShopTvConfigMapper extends BaseDao<ShopTvConfig,Long> {
    int deleteByPrimaryKey(String id);

    int insert(ShopDetail record);

    int insertSelective(ShopDetail record);

    ShopDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShopDetail record);

    int updateByPrimaryKey(ShopDetail record);

    ShopTvConfig selectByShopId(String shopId);

}

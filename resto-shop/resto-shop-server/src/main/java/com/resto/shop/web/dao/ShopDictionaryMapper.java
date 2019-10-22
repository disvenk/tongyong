package com.resto.shop.web.dao;

import com.resto.shop.web.model.ShopDictionary;
import org.apache.ibatis.annotations.Param;

public interface ShopDictionaryMapper {
    int insert(ShopDictionary record);

    int insertSelective(ShopDictionary record);

    ShopDictionary selectShopDictionaryByShopId(@Param("shopId") String shopId);
}
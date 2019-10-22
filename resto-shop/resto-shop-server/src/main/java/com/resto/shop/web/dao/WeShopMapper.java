package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeShop;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WeShopMapper  extends GenericDao<WeShop,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeShop record);

    int insertSelective(WeShop record);

    WeShop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeShop record);

    int updateByPrimaryKey(WeShop record);

    List<WeShop> selectWeShopListByBrandIdAndTime(@Param("brandId") String brandId, @Param("createTime") Date date);

    WeShop selectWeShopByShopIdAndTime(@Param("shopId") String shopId, @Param("createTime")Date date);
}

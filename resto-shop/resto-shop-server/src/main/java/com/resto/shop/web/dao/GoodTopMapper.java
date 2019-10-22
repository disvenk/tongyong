package com.resto.shop.web.dao;

import com.resto.shop.web.model.GoodTop;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface GoodTopMapper  extends GenericDao<GoodTop,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(GoodTop record);

    int insertSelective(GoodTop record);

    GoodTop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodTop record);

    int updateByPrimaryKey(GoodTop record);

    int deleteByTodayAndShopId(@Param("brandId") String brandId,@Param("shopId") String shopId, @Param("type") int dayMessage,@Param("date") Date date);
}

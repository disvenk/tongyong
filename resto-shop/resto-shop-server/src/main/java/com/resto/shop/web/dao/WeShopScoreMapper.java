package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeShopScore;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WeShopScoreMapper  extends GenericDao<WeShopScore,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeShopScore record);

    int insertSelective(WeShopScore record);

    WeShopScore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeShopScore record);

    int updateByPrimaryKey(WeShopScore record);

    WeShopScore selectByShopIdAndDate(@Param("shopId") String id, @Param("createTime") Date beforeYesterDay);
}

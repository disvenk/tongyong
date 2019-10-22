package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeItem;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeItemMapper  extends GenericDao<WeItem,Long> {
    int deleteByPrimaryKey(Long id);

    int insert(WeItem record);

    int insertSelective(WeItem record);

    WeItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeItem record);

    int updateByPrimaryKey(WeItem record);

    List<WeItem> selectByShopIdAndTime(@Param("beginTime")String  beginTime, @Param("shopId") String shopId);
    //List<WeItem> selectByShopIdAndTime(@Param("createTime") Date createTime, @Param("shopId") String shopId);


    void deleteByIds(List<Long> ids);
}

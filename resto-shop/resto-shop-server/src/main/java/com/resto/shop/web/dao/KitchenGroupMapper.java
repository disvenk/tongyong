package com.resto.shop.web.dao;

import com.resto.shop.web.model.KitchenGroup;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KitchenGroupMapper  extends GenericDao<KitchenGroup,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(KitchenGroup record);

    int insertSelective(KitchenGroup record);

    KitchenGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KitchenGroup record);

    int updateByPrimaryKey(KitchenGroup record);

    List<KitchenGroup> selectByShopDetailId(@Param("shopDetailId") String shopDetailId);

    List<KitchenGroup> selectByShopDetailIdByStatus(@Param("shopDetailId") String shopDetailId);
}

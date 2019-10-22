package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.Kitchen;
import com.resto.shop.web.model.KitchenGroupDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KitchenGroupDetailMapper  extends GenericDao<KitchenGroupDetail,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(KitchenGroupDetail record);

    int insertSelective(KitchenGroupDetail record);

    KitchenGroupDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KitchenGroupDetail record);

    int updateByPrimaryKey(KitchenGroupDetail record);

    List<KitchenGroupDetail> selectByShoDetailId(@Param("shopDetailId") String shopDetailId);

    List<Kitchen>selectByGroupIdAndShopDeailId(@Param("kitchenGroupId") Integer kitchenGroupId,@Param("shopDetailId")String shopDetailId);

     void deleteByShopDetailIdAndKitchenGroupId(@Param("shopDetailId")String shopDetailId,@Param("kitchenGroupId") Integer kitchenGroupId);

     void deleteByShopDetailIdAndKitchenId(@Param("shopDetailId")String shopDetailId,@Param("kitchenId") Integer kitchenId);
}

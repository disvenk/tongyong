package com.resto.shop.web.dao;

import com.resto.shop.web.model.WeOrderDetail;
import com.resto.brand.core.generic.GenericDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WeOrderDetailMapper  extends GenericDao<WeOrderDetail,Integer> {
    int deleteByPrimaryKey(Long id);

    int insert(WeOrderDetail record);

    int insertSelective(WeOrderDetail record);

    WeOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WeOrderDetail record);

    int updateByPrimaryKey(WeOrderDetail record);

    WeOrderDetail selectWeOrderByShopIdAndTime(@Param("shopId") String shopId, @Param("createTime") Date date);
}

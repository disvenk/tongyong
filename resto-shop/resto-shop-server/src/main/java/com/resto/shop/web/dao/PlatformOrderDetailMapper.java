package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.PlatformOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformOrderDetailMapper  extends GenericDao<PlatformOrderDetail,String> {
    int deleteByPrimaryKey(String id);

    int insert(PlatformOrderDetail record);

    int insertSelective(PlatformOrderDetail record);

    PlatformOrderDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PlatformOrderDetail record);

    int updateByPrimaryKey(PlatformOrderDetail record);

    List<PlatformOrderDetail> selectByPlatformOrderId(@Param("platformOrderId") String platformOrderId);
}

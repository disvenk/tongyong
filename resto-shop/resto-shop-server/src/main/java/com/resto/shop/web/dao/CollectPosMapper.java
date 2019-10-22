package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.model.CollectPos;
import com.resto.shop.web.model.CollectPosExample;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CollectPosMapper extends GenericDao<CollectPos,String> {
    long countByExample(CollectPosExample example);

    int deleteByExample(CollectPosExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CollectPos record);

    int insertSelective(CollectPos record);

    List<CollectPos> selectByExample(CollectPosExample example);

    CollectPos selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CollectPos record, @Param("example") CollectPosExample example);

    int updateByExample(@Param("record") CollectPos record, @Param("example") CollectPosExample example);

    int updateByPrimaryKeySelective(CollectPos record);

    int updateByPrimaryKey(CollectPos record);

    List<CollectPos> selectFailureOrders(@Param("staDate") Date staDate, @Param("endDate") Date endDate, @Param("shopId") String shopId);
}
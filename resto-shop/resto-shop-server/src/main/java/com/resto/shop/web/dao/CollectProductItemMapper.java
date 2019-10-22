package com.resto.shop.web.dao;

import com.resto.shop.web.model.CollectProductItem;
import com.resto.shop.web.model.CollectProductItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CollectProductItemMapper {
    long countByExample(CollectProductItemExample example);

    int deleteByExample(CollectProductItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CollectProductItem record);

    int insertSelective(CollectProductItem record);

    List<CollectProductItem> selectByExample(CollectProductItemExample example);

    CollectProductItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CollectProductItem record, @Param("example") CollectProductItemExample example);

    int updateByExample(@Param("record") CollectProductItem record, @Param("example") CollectProductItemExample example);

    int updateByPrimaryKeySelective(CollectProductItem record);

    int updateByPrimaryKey(CollectProductItem record);
}